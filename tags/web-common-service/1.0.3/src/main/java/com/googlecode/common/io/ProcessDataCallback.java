
package com.googlecode.common.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * Interface to process InputStream data.
 */
public interface ProcessDataCallback {
    
    public void processData(InputStream istream) throws IOException;

}
