
package com.googlecode.common.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * Helper class for working with byte buffer OutputStream.
 */
public final class BufOutputStream extends ByteArrayOutputStream {

    
    public BufOutputStream() {
    }
    
    public BufOutputStream(int size) {
        super(size);
    }
    
    /**
     * Convenient method to get internal buffer reference.
     * 
     * <p>Note: use <code>size()</code> method to determine valid bytes count.
     * 
     * @return internal buffer reference
     * 
     * @see #size()
     */
    public byte[] getBuf() {
        return buf;
    }
    
    public InputStream toInputStream() {
        return new ByteArrayInputStream(buf, 0, size());
    }

}
