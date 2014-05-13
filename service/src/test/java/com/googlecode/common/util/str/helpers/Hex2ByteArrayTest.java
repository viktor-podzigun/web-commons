
package com.googlecode.common.util.str.helpers;

import org.junit.Assert;
import org.junit.Test;
import com.googlecode.common.util.StringHelpers;


/**
 * 
 */
public class Hex2ByteArrayTest extends Assert {
    
    
    @Test(expected = NullPointerException.class)
    public void nullPointerException() {
        StringHelpers.hex2ByteArray(null);
    }
    
    @Test 
    public void emptyString() {       
        assertArrayEquals(new byte[0], StringHelpers.hex2ByteArray(""));
    }
    
    @Test 
    public void symbol() {
        byte[] expecteds = "m".getBytes();;
        byte[] actuals = StringHelpers.hex2ByteArray("6d");
        assertArrayEquals(expecteds, actuals);
    }
    
    @Test 
    public void string() {
        byte[] expecteds = "String".getBytes();;
        byte[] actuals = StringHelpers.hex2ByteArray("537472696e67");
        assertArrayEquals(expecteds, actuals);
    }
}
