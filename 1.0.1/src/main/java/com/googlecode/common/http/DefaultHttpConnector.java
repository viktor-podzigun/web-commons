
package com.googlecode.common.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BufferedHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.googlecode.common.io.ProcessDataCallback;
import com.googlecode.common.util.StringHelpers;


/**
 * Default implementation for HttpConnector interface.
 * 
 * @see HttpConnector
 */
public final class DefaultHttpConnector implements HttpConnector {
    
    private static final Logger log = LoggerFactory.getLogger(
            DefaultHttpConnector.class);

    private final DefaultHttpClient         httpClient;
    private IdleConnectionMonitorThread     monitorThread;
    
    
    /**
     * Creates DefaultHttpService with default settings.
     */
    public DefaultHttpConnector() {
        this(new HttpConnectorParams());
    }
    
    /**
     * Creates and initializes new DefaultHttpService with the given settings.
     */
    public DefaultHttpConnector(HttpConnectorParams params) {
        if (params == null) {
            throw new NullPointerException("params");
        }
        
        httpClient = create(params);
    
        if (params.isRunConnectionMonitor()) {
            monitorThread = new IdleConnectionMonitorThread(
                    httpClient.getConnectionManager());
            monitorThread.start();
        }
    }
    
    private static DefaultHttpClient create(HttpConnectorParams params) {
        // Setup SSL certificate ignorance
        // Create a trust manager that does not validate certificate chains
        SSLContext sslContext;
        try {
            //sslContext = SSLContext.getInstance("ssl");
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { new DumbTrustManager() }, 
                    null);//new SecureRandom());
            
        } catch (GeneralSecurityException x) {
            throw new RuntimeException(x);
        }
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, 
                PlainSocketFactory.getSocketFactory()));
        
        schemeRegistry.register(new Scheme("https", 443, 
                new SSLv3SocketFactory(sslContext, 
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
        
        final int maxTotal           = params.getMaxTotal();
        final int defaultMaxPerRoute = params.getDefaultMaxPerRoute();
        final int socketTimeout      = params.getSocketTimeout();
        final int connectionTimeout  = params.getConnectionTimeout();
        final int connectionLifetime = params.getConnectionLifetime();
        
        log.info("ConnectionManager params:"
                + "\n\tmaxTotal             = " + maxTotal
                + "\n\tdefaultMaxPerRoute   = " + defaultMaxPerRoute
                + "\n\tsocketTimeout        = " + socketTimeout
                + "\n\tconnectionTimeout    = " + connectionTimeout
                + "\n\tconnectionLifetime   = " + connectionLifetime
                + "\n\trunConnectionMonitor = " + params.isRunConnectionMonitor());
        
        // ThreadSafeClientConnManager is a more complex implementation that
        // manages a pool of client connections and is able to service
        // connection requests from multiple execution threads. Connections 
        // are pooled on a per route basis. A request for a route for which 
        // the manager already has a persistent connection available in the 
        // pool will be serviced by leasing a connection from the pool rather 
        // than creating a brand new connection.
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
                schemeRegistry, connectionLifetime, TimeUnit.MILLISECONDS);
        
        // Increase max total connection to 200
        cm.setMaxTotal(maxTotal);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        // Increase max connections for localhost:80 to 50
        //HttpHost localhost = new HttpHost("locahost", 80);
        //cm.setMaxForRoute(new HttpRoute(localhost), 50);
        
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
        
        DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);
        setProxySettings(httpClient, params);
        return httpClient;
    }
    
    private static void setProxySettings(DefaultHttpClient httpClient, 
            HttpConnectorParams params) {
        
        String host = params.getProxyHost();
        final int port = params.getProxyPort();
        if (host == null || host.isEmpty()) {
            return;
        }
        
        HttpHost proxy = new HttpHost(host, port);
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);

        String user   = params.getProxyUser();
        String domain = params.getProxyDomain();
        
        if (user != null && !user.isEmpty()) {
            if (domain == null) {
                domain = "";
            }
            
            NTCredentials creds = new NTCredentials(user, 
                    params.getProxyPassword(), host + ":" + port, domain);
            httpClient.getCredentialsProvider().setCredentials(
                    AuthScope.ANY, creds);
        }
    }
    
    /**
     * Stops the HTTP client service.
     * <p>
     * When an HttpClient instance is no longer needed and is about to go out of
     * scope it is important to shut down its connection manager to ensure that
     * all connections kept alive by the manager get closed and system resources
     * allocated by those connections are released.
     */
    public void shutdown() {
        log.debug("Shutdown");
        
        if (monitorThread != null) {
            monitorThread.shutdownAndWait();
            monitorThread = null;
        }
        
        // release all connections
        httpClient.getConnectionManager().shutdown();
    }

    private String resolveUrl(RequestParams params, String url) {
        String urlPrefix = (params != null ? params.getUrlPrefix() : null);
        return (urlPrefix != null ? urlPrefix + url : url);
    }
    
    @Override
    public void uploadParams(RequestParams reqParams, String url, 
            Map<String, ?> params, ProcessDataCallback callback) 
            throws IOException {
        
        log.debug("Multipart upload: " + params);
        
        HttpPost post = new HttpPost(resolveUrl(reqParams, url));
        MultipartEntity mpEntity = new MultipartEntity();
        
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            String name  = entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof File) {
                mpEntity.addPart(name, new FileBody((File)value, 
                        "application/octet-stream"));
            } else {
                mpEntity.addPart(name, new StringBody(value.toString(), 
                        Charset.forName("UTF-8")));
            }
        };
        
        post.setEntity(mpEntity);
        execute(reqParams, null, post, callback);
    }
    
    @Override
    public void executeGet(RequestParams params, String url, 
            ProcessDataCallback callback) throws IOException {
        
        execute(params, new HttpGet(resolveUrl(params, url)), callback);
    }
    
    @Override
    public void executePost(RequestParams params, String url, 
            byte[] data, int offset, int length, 
            ProcessDataCallback callback) 
            throws IOException {
        
        HttpPost request = new HttpPost(resolveUrl(params, url));
        if (data != null) {
            request.setEntity(new BufEntity(data, offset, length));
        }
        
        execute(params, request, callback);
    }

    @Override
    public void executeDelete(RequestParams params, String url, 
            ProcessDataCallback callback) 
            throws IOException {
        
        execute(params, new HttpDelete(resolveUrl(params, url)), callback);
    }

    @Override
    public void executePut(RequestParams params, String url, 
            byte[] data, int offset, int length, 
            ProcessDataCallback callback) 
            throws IOException {
        
        HttpPut request = new HttpPut(resolveUrl(params, url));
        if (data != null) {
            request.setEntity(new BufEntity(data, offset, length));
        }
        
        execute(params, request, callback);
    }

    private void execute(RequestParams params, HttpRequestBase request, 
            ProcessDataCallback callback) 
            throws IOException {
        
        String contentType = null;
        if (params != null) {
            contentType = params.getContentType();
        }
        
        execute(params, contentType, request, callback);
    }
    
    protected void execute(RequestParams params, String contentType, 
            HttpRequestBase request, ProcessDataCallback callback) 
            throws IOException {
        
        // initialize parameters
        request.setHeader("Accept", "*");
        if (params != null) {
            checkAddAuthHeader(request, params);
            
            int socketTimeout = params.getSocketTimeout();
            if (socketTimeout > 0) {
                request.getParams().setIntParameter("http.socket.timeout", 
                        socketTimeout);
            }
        
            String acceptLanguage = params.getAcceptLanguage();
            if (acceptLanguage != null) {
                request.setHeader("Accept-Language", acceptLanguage);
            }
            
            Map<String, String> headers = params.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.setHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        
        if (contentType != null) {
            request.setHeader("Content-Type", contentType);
        }
        
        HttpEntity  respEntity = null;
        InputStream respStream = null;
        
        try {
            // execute the method and check response code
            HttpResponse response = httpClient.execute(request);
            
            // get the response body
            respEntity = response.getEntity();
            
            // get and check the status
            StatusLine statusLine = response.getStatusLine();
            int httpStatus = statusLine.getStatusCode();
            if (httpStatus != HttpStatus.SC_OK) {
                throw new RequestException(httpStatus, 
                        statusLine.getReasonPhrase(),
                        respEntity != null ?
                                EntityUtils.toString(respEntity, "UTF-8") 
                                : null);
            }
            
            if (respEntity == null) {
                throw new IOException("No response entity from the server");
            }
    
            respStream = respEntity.getContent();
            if (respStream == null) {
                throw new IOException("No response stream from the server");
            }
    
            // process the response data
            if (callback != null) {
                callback.processData(respStream);
            }
        } catch (IOException x) {
            //request.abort();
            throw x;
            
        } catch (Exception x) {
            //request.abort();
            log.error("Request failed: " + request.getRequestLine() 
                    + ", error: " + x);
            throw new IOException(x);
            
        } finally {
            if (respStream != null) {
                respStream.close();
            }
            
            // ensure the connection gets released to the manager
            EntityUtils.consume(respEntity);
        }
    }
    
    private Header createTokenHeader(String userToken, boolean proxy) {
        CharArrayBuffer buffer = new CharArrayBuffer(32);
        if (proxy) {
            buffer.append(AUTH.PROXY_AUTH_RESP);
        } else {
            buffer.append(AUTH.WWW_AUTH_RESP);
        }
        
        buffer.append(": Token ");
        buffer.append(userToken);
        return new BufferedHeader(buffer);
    }
    
    private void checkAddAuthHeader(HttpRequestBase request,
            RequestParams params) {
        
        if (params.hasUserToken()) {
            request.addHeader(createTokenHeader(params.getUserToken(), false));
            return;
        }
        
        String name = params.getUserName();
        String pass = params.getUserPass();
        if (StringHelpers.isNullOrEmpty(name) 
                || StringHelpers.isNullOrEmpty(pass)) {
            
            return;
        }
        
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                name, pass);
        
        Header authHeader = BasicScheme.authenticate(creds, "UTF-8", false);
        if (authHeader != null) {
            request.addHeader(authHeader);
        }
    }
    
    
    private static class DumbTrustManager implements X509TrustManager {
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

    
    private static class SSLv3SocketFactory extends SSLSocketFactory {

        public SSLv3SocketFactory(SSLContext sslContext, X509HostnameVerifier verifier) {
            super(sslContext, verifier);
        }
    

        @Override
        public Socket createSocket(HttpParams params) throws IOException {
            SSLSocket socket = (SSLSocket) super.createSocket(params);
            socket.setEnabledProtocols(new String[] {"SSLv3"});

            return socket;
        }
    }

    /**
     * Got from here: 
     * http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d5e652
     * 
     * <p>One of the major shortcomings of the classic blocking I/O model is that
     * the network socket can react to I/O events only when blocked in an I/O
     * operation. When a connection is released back to the manager, it can be
     * kept alive however it is unable to monitor the status of the socket and
     * react to any I/O events. If the connection gets closed on the server
     * side, the client side connection is unable to detect the change in the
     * connection state (and react appropriately by closing the socket on its
     * end).
     * 
     * <p>HttpClient tries to mitigate the problem by testing whether the
     * connection is 'stale', that is no longer valid because it was closed on
     * the server side, prior to using the connection for executing an HTTP
     * request. The stale connection check is not 100% reliable and adds 10 to
     * 30 ms overhead to each request execution. The only feasible solution that
     * does not involve a one thread per socket model for idle connections is a
     * dedicated monitor thread used to evict connections that are considered
     * expired due to a long period of inactivity. The monitor thread can
     * periodically call ClientConnectionManager#closeExpiredConnections()
     * method to close all expired connections and evict closed connections from
     * the pool. It can also optionally call
     * ClientConnectionManager#closeIdleConnections() method to close all
     * connections that have been idle over a given period of time.
     */
    private class IdleConnectionMonitorThread extends Thread {
        
        private final ClientConnectionManager   connMgr;
        
        private final Object                    stopLock = new Object();
        private boolean                         stopRequest;
        
        
        public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
            super("HttpConn-Monitor");
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                log.debug("Started IdleConnectionMonitorThread");
                
                synchronized (stopLock) {
                    while (!stopRequest) {
                        stopLock.wait(20000);
                        
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 3 minutes
                        connMgr.closeIdleConnections(3, TimeUnit.MINUTES);
                    }
                }
            } catch (Exception x) {
                log.error("IdleConnectionMonitorThread", x);
            } finally {
                log.debug("Stopped IdleConnectionMonitorThread");
            }
        }
        
        public void shutdownAndWait() {
            if (isAlive()) {
                log.debug("Stopping IdleConnectionMonitorThread ...");
                
                synchronized (stopLock) {
                    stopRequest = true;
                    stopLock.notifyAll();
                }
                
                try {
                    join();
                
                } catch (InterruptedException x) {
                    throw new RuntimeException(x);
                }
            }
        }
    }

}
