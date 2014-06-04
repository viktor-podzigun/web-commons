
package com.googlecode.common.http;


/**
 * Contains HttpConnector parameters.
 */
public final class HttpConnectorParams {

    /**
     * Maximum total connections
     */
    private int     maxTotal            = 200;
    
    /**
     * Default maximum connections per route
     */
    private int     defaultMaxPerRoute  = 20;
    
    /**
     * Defines the socket timeout in milliseconds, which is the timeout for
     * waiting for data or, put differently, a maximum period inactivity between
     * two consecutive data packets). A timeout value of zero is interpreted as
     * an infinite timeout.
     */
    private int     socketTimeout       = 30000; // 30 sec.
    
    /**
     * Determines the timeout in milliseconds until a connection is established.
     * A timeout value of zero is interpreted as an infinite timeout.
     */
    private int     connectionTimeout   = 10000; // 10 sec.
    
    /**
     * Max connection lifetime in milliseconds, <=0 implies "infinity"
     */
    private int     connectionLifetime;
    
    /**
     * Indicates whether to run <code>IdleConnectionMonitorThread</code>.
     */
    private boolean runConnectionMonitor;
    
    private String  proxyHost;
    private int     proxyPort;
    private String  proxyUser;
    private String  proxyPassword;
    private String  proxyDomain;
    
    
    public HttpConnectorParams() {
    }
    
    public int getMaxTotal() {
        return maxTotal;
    }
    
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }
    
    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }
    
    public int getSocketTimeout() {
        return socketTimeout;
    }
    
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    public int getConnectionLifetime() {
        return connectionLifetime;
    }
    
    public void setConnectionLifetime(int connectionLifetime) {
        this.connectionLifetime = connectionLifetime;
    }
    
    public boolean isRunConnectionMonitor() {
        return runConnectionMonitor;
    }
    
    public void setRunConnectionMonitor(boolean runConnectionMonitor) {
        this.runConnectionMonitor = runConnectionMonitor;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{maxTotal: " + maxTotal
                + ", defaultMaxPerRoute: " + defaultMaxPerRoute
                + ", socketTimeout: " + socketTimeout
                + ", connectionTimeout: "+ connectionTimeout
                + ", connectionLifetime: "+ connectionLifetime
                + ", runConnectionMonitor: "+ runConnectionMonitor
                + (proxyHost != null ? ", proxyHost: " + proxyHost : "")
                + ", proxyPort: " + proxyPort
                + (proxyUser != null ? ", proxyUser: " + proxyUser : "")
                + (proxyDomain != null ? ", proxyDomain: " + proxyDomain : "")
                + "}";
    }

}
