
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.googlecode.common.http.JsonRequestClient;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.io.JsonSerializer;
import com.googlecode.common.io.ProcessDataCallback;
import com.googlecode.common.service.HttpService;
import com.googlecode.common.service.JsonRequestService;


/**
 * Default implementation for {@link JsonRequestService} interface.
 */
@Service
@Lazy
public class JsonRequestServiceImpl implements JsonRequestService {

    @Autowired
    private HttpService         httpService;
    
    @Autowired
    private JsonSerializer      jsonSerializer;
    
    private JsonRequestClient   client;
    
    
    @PostConstruct
    public void init() {
        client = new JsonRequestClient(httpService, jsonSerializer);
    }
    
    @Override
    public <T> T read(RequestParams params, String url, Class<T> respClass)
            throws IOException {
        
        return client.read(params, url, respClass);
    }

    @Override
    public <T> T delete(RequestParams params, String url, Class<T> respClass)
            throws IOException {
        
        return client.delete(params, url, respClass);
    }

    @Override
    public <T> T create(RequestParams params, String url, Object requestObj,
            Class<T> respClass) throws IOException {
        
        return client.create(params, url, requestObj, respClass);
    }

    @Override
    public void create(RequestParams params, String url, Object requestObj,
            ProcessDataCallback callback) throws IOException {
        
        client.create(params, url, requestObj, callback);
    }

    @Override
    public <T> T update(RequestParams params, String url, Object requestObj,
            Class<T> respClass) throws IOException {
        
        return client.update(params, url, requestObj, respClass);
    }

    @Override
    public <T> T uploadParams(RequestParams reqParams, String url,
            Map<String, Object> params, Class<T> respClass) 
            throws IOException {
        
        return client.uploadParams(reqParams, url, params, respClass);
    }

    @Override
    public void downloadFile(RequestParams params, String url,
            ProcessDataCallback callback) throws IOException {

        client.downloadFile(params, url, callback);
    }

}
