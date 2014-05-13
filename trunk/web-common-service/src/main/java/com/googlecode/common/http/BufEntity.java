
package com.googlecode.common.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;


/**
 * A self contained, repeatable entity that obtains its content from a byte array.
 */
public class BufEntity extends AbstractHttpEntity implements Cloneable {

    protected final byte[]  content;
    protected final int     offset;
    protected final int     count;

    
    public BufEntity(byte[] buf) {
        this(buf, 0, buf == null ? 0 : buf.length);
    }
    
    public BufEntity(byte buf[], int offset, int length) {
        if (buf == null) {
            throw new IllegalArgumentException("Source byte array may not be null");
        }
        
        this.content = buf;
        this.offset  = offset;
        this.count   = length;
    }

    public boolean isRepeatable() {
        return true;
    }

    public long getContentLength() {
        return count;
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(content, offset, count);
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        
        outstream.write(content, offset, count);
        outstream.flush();
    }


    /**
     * Tells that this entity is not streaming.
     *
     * @return <code>false</code>
     */
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
