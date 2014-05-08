
package com.googlecode.common.util.str.helpers;

import org.junit.Assert;
import org.junit.Test;
import com.googlecode.common.util.StringHelpers;


/**
 * 
 */
public class StringHelpersTests extends Assert {
    
    
    @Test
    public void isNullOrEmptyTest() {
        assertTrue(StringHelpers.isNullOrEmpty(""));
        assertTrue(StringHelpers.isNullOrEmpty(null));
        assertFalse(StringHelpers.isNullOrEmpty("       "));
        assertFalse(StringHelpers.isNullOrEmpty("qwerty"));
    }
    
    @Test
    public void ensureNotNullTest() {
        String EMPTY_STRING = "";        
        assertEquals(EMPTY_STRING, StringHelpers.ensureNotNull(EMPTY_STRING));
        assertEquals(EMPTY_STRING, StringHelpers.ensureNotNull(null));
        
        String str = "       ";
        assertEquals(str, StringHelpers.ensureNotNull(str));
        str = "qwerty";
        assertEquals(str, StringHelpers.ensureNotNull(str));
    }
    
    @Test
    public void ensureNullIfEmptyTest() {        
        assertEquals(null, StringHelpers.ensureNullIfEmpty(""));
        assertEquals(null, StringHelpers.ensureNullIfEmpty(null));
        
        String str = "       ";
        assertEquals(str, StringHelpers.ensureNullIfEmpty(str));
        str = "qwerty";
        assertEquals(str, StringHelpers.ensureNullIfEmpty(str));
    }
}
