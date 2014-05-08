
package com.googlecode.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.googlecode.common.io.BaseSerializer;
import com.googlecode.common.io.BufOutputStream;
import com.googlecode.common.io.ProcessDataCallback;


/**
 * Base class for requests clients.
 */
public abstract class AbstractRequestClient implements RequestClient {

    private final HttpConnector     httpService;
    private final BaseSerializer    serializer;
    
    private final RequestParams     defaultParams;
    
    
    protected AbstractRequestClient(HttpConnector httpService, 
            BaseSerializer serializer, String contentType) {
        
        if (httpService == null) {
            throw new NullPointerException("httpService");
        }
        if (serializer == null) {
            throw new NullPointerException("serializer");
        }
        if (contentType == null) {
            throw new NullPointerException("contentType");
        }
        
        this.httpService = httpService;
        this.serializer  = serializer;
        
        defaultParams = new RequestParams().setContentType(contentType);
    }

    @Override
    public <T> T read(RequestParams params, String url, Class<T> respClass) 
            throws IOException {
        
        ResponseProcessor<T> respProcessor = new ResponseProcessor<T>(respClass);
        httpService.executeGet(params, url, respProcessor);
        return respProcessor.getResult();
    }

    @Override
    public <T> T delete(RequestParams params, String url, Class<T> respClass) 
            throws IOException {
        
        ResponseProcessor<T> respProcessor = new ResponseProcessor<T>(respClass);
        httpService.executeDelete(params, url, respProcessor);
        return respProcessor.getResult();
    }
    
    private RequestParams setReqParams(RequestParams params) {
        if (params == null) {
            return defaultParams;
        }
        
        if (params.getContentType() == null) {
            params.setContentType(defaultParams.getContentType());
        }
        
        return params;
    }

    @Override
    public <T> T create(RequestParams params, String url, Object requestObj, 
            Class<T> respClass) throws IOException {
        
        byte[] buf    = null;
        int    length = 0;
        
        if (requestObj != null) {
            BufOutputStream outStream = serialize(requestObj);
            buf    = outStream.getBuf();
            length = outStream.size();
        }
        
        ResponseProcessor<T> respProcessor = new ResponseProcessor<T>(respClass);
        httpService.executePost(setReqParams(params), url, buf, 0, length, 
                respProcessor);
        
        return respProcessor.getResult();
    }
    
    @Override
    public void create(RequestParams params, String url, Object requestObj, 
            ProcessDataCallback callback) throws IOException {
        
        byte[] buf    = null;
        int    length = 0;
        
        if (requestObj != null) {
            BufOutputStream outStream = serialize(requestObj);
            buf    = outStream.getBuf();
            length = outStream.size();
        }
        
        httpService.executePost(setReqParams(params), url, buf, 0, length, 
                callback);
    }

    @Override
    public <T> T update(RequestParams params, String url, Object requestObj, 
            Class<T> respClass) throws IOException {

        byte[] buf    = null;
        int    length = 0;
        
        if (requestObj != null) {
            BufOutputStream outStream = serialize(requestObj);
            buf    = outStream.getBuf();
            length = outStream.size();
        }
        
        ResponseProcessor<T> respProcessor = new ResponseProcessor<T>(respClass);
        httpService.executePut(setReqParams(params), url, buf, 0, length, 
                respProcessor);
        
        return respProcessor.getResult();
    }

    @Override
    public <T> T uploadParams(RequestParams reqParams, String url, 
            Map<String, Object> params, Class<T> respClass) 
            throws IOException {
        
        ResponseProcessor<T> respProcessor = new ResponseProcessor<T>(respClass);
        httpService.uploadParams(reqParams, url, params, respProcessor);
        return respProcessor.getResult();
    }

    @Override
    public void downloadFile(RequestParams params, String url, 
            ProcessDataCallback callback) throws IOException {
        
        httpService.executeGet(params, url, callback);
    }
    
    public BufOutputStream serialize(Object data) throws IOException {
        BufOutputStream outStream = new BufOutputStream(256);
        serializer.serialize(data, outStream);
        return outStream;
    }
    
    
    private class ResponseProcessor<T> implements ProcessDataCallback {
        
        private final Class<T>  respClass;
        private T               result;
        
        public ResponseProcessor(Class<T> respClass) {
            this.respClass = respClass;
        }
        
        @Override
        public void processData(InputStream istream) throws IOException {
            result = serializer.deserialize(respClass, istream);
        }
        
        public T getResult() {
            return result;
        }
    }
    
}
