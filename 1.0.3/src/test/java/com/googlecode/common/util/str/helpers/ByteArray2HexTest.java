
package com.googlecode.common.util.str.helpers;

import org.junit.Assert;
import org.junit.Test;
import com.googlecode.common.util.StringHelpers;


/**
 * 
 */
public class ByteArray2HexTest extends Assert{
    
    
    @Test(expected = NullPointerException.class)
    public void nullPointerException() {
        StringHelpers.byteArray2Hex(null);
    }
    
    @Test
    public void zeroArray() {
        String str = StringHelpers.byteArray2Hex(new byte[2]);
        assertEquals("0000", str);
    }
    
    @Test
    public void symbol() {
        byte[] byteArray = "m".getBytes();
        String str = StringHelpers.byteArray2Hex(byteArray);
        assertEquals("6d", str);
    }
    
    @Test
    public void string() {
        byte[] byteArray = "String".getBytes();
        String str = StringHelpers.byteArray2Hex(byteArray);
        assertEquals("537472696e67", str);
    }
}
