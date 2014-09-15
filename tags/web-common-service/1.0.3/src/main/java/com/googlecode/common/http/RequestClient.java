
package com.googlecode.common.http;

import java.io.IOException;
import java.util.Map;
import com.googlecode.common.io.ProcessDataCallback;


/**
 * Represents generic requests client interface.
 */
public interface RequestClient {

    public <T> T read(RequestParams params, String url, Class<T> respClass) 
            throws IOException;

    public <T> T delete(RequestParams params, String url, Class<T> respClass) 
            throws IOException;

    public <T> T create(RequestParams params, String url, Object requestObj, 
            Class<T> respClass) throws IOException;

    public void create(RequestParams params, String url, Object requestObj, 
            ProcessDataCallback callback) throws IOException;

    public <T> T update(RequestParams params, String url, Object requestObj, 
            Class<T> respClass) throws IOException;

    public <T> T uploadParams(RequestParams reqParams, String url, 
            Map<String, Object> params, Class<T> respClass) 
            throws IOException;

    public void downloadFile(RequestParams params, String url, 
            ProcessDataCallback callback) throws IOException;
}
