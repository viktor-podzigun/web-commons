package com.googlecode.common.service.impl;

import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.googlecode.common.http.DefaultHttpConnector;
import com.googlecode.common.http.HttpConnectorParams;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.io.ProcessDataCallback;
import com.googlecode.common.service.HttpService;

/**
 * Default implementation for {@link HttpService} interface.
 */
@Service
@Lazy
public class HttpServiceImpl implements HttpService {

    private DefaultHttpConnector    connector;

    @Value("${httpService.runConnectionMonitor:true}")
    private boolean runConnectionMonitor;

    @PostConstruct
    public void init() {
        // init params
        HttpConnectorParams params = new HttpConnectorParams();
        params.setMaxTotal(600);
        params.setDefaultMaxPerRoute(200);
        params.setRunConnectionMonitor(runConnectionMonitor);
        
        // create requests connector
        connector = new DefaultHttpConnector(params);
    }
    
    @PreDestroy
    public void destroy() {
        if (connector != null) {
            connector.shutdown();
            connector = null;
        }
    }

    @Override
    public void uploadParams(RequestParams reqParams, String url,
            Map<String, ?> params, ProcessDataCallback callback)
            throws IOException {
        
        connector.uploadParams(reqParams, url, params, callback);
    }

    @Override
    public void executeGet(RequestParams params, String url,
            ProcessDataCallback callback) throws IOException {
        
        connector.executeGet(params, url, callback);
    }

    @Override
    public void executePost(RequestParams params, String url, byte[] data,
            int offset, int length, ProcessDataCallback callback)
            throws IOException {
        
        connector.executePost(params, url, data, offset, length, callback);
    }

    @Override
    public void executeDelete(RequestParams params, String url,
            ProcessDataCallback callback) throws IOException {
        
        connector.executeDelete(params, url, callback);
    }

    @Override
    public void executePut(RequestParams params, String url, byte[] data,
            int offset, int length, ProcessDataCallback callback)
            throws IOException {
        
        connector.executePut(params, url, data, offset, length, callback);
    }
}
