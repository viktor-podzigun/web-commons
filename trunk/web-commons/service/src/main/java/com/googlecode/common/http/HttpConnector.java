
package com.googlecode.common.http;

import java.io.IOException;
import java.util.Map;
import com.googlecode.common.io.ProcessDataCallback;


/**
 * Generic HTTP requests service interface.
 */
public interface HttpConnector {

    public void uploadParams(RequestParams reqParams, String url, 
            Map<String, ?> params, ProcessDataCallback callback) 
            throws IOException;
    
    public void executeGet(RequestParams params, String url, 
            ProcessDataCallback callback) 
            throws IOException;

    public void executePost(RequestParams params, String url, 
            byte[] data, int offset, int length, 
            ProcessDataCallback callback) 
            throws IOException;

    public void executeDelete(RequestParams params, String url, 
            ProcessDataCallback callback) 
            throws IOException;

    public void executePut(RequestParams params, String url, 
            byte[] data, int offset, int length, 
            ProcessDataCallback callback) 
            throws IOException;

}
