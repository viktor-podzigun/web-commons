
package com.googlecode.common.util.str.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.googlecode.common.util.StringHelpers;


/**
 * 
 */
public class StrToMapTest extends Assert {
    
    
    @Test(expected = NullPointerException.class)
    public void nullPointerException() {
        StringHelpers.strToMap(null);
    }

    @Test
    public void emptyString() {
        Map<String, String> expected = new LinkedHashMap<String, String>();
        Map<String, String> actual = StringHelpers.strToMap("");
        assertEquals(expected, actual);
    }
    
    @Test 
    public void wrongSeparator() {
        //TODO: Add to documentation separator symbol
        String str = "k1=v1,k2=v2,k3=v3";
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("k1", "v1,k2=v2,k3=v3");
        Map<String, String> actual = StringHelpers.strToMap(str);
        assertEquals(expected, actual);
    }
    
    @Test
    public void notKeyandValue() {
        String str = "=;k2=;=v3";
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("", "");
        expected.put("k2", "");
        expected.put("", "v3");
        Map<String, String> actual = StringHelpers.strToMap(str);
        assertEquals(expected, actual);
    }
    
    @Ignore("REAL BUG")
    @Test 
    public void separatorKey() {
        //TODO: if key contains ; parses wrong: added new element
        String str = "k1;k=v1";
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("k1;k", "v1");
        Map<String, String> actual = StringHelpers.strToMap(str);
        assertEquals(expected, actual);
    }
    
    @Ignore("REAL BUG")
    @Test 
    public void separatorValue() {
        //TODO: if value contains ; parses wrong: added new element
        String str = "k1=v1;v";
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("k1", "v1;v");
        Map<String, String> actual = StringHelpers.strToMap(str);
        assertEquals(expected, actual);
    }
    
    @Ignore("REAL BUG")
    @Test
    public void specialSymbols() {
        //TODO: \t % generates Exceptions
        String str = "k1=v1$#^&*{}\n\r\f\'\"\t%";
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("k1\n", "v1$#^&*{}\n\r\f\'\"\t%");
        Map<String, String> actual = StringHelpers.strToMap(str);
        assertEquals(expected, actual);
    }
    
    @Test 
    public void correctString() {
        int size = 50;
        Map<String, String> expected = new LinkedHashMap <String, String>();
        StringBuilder actual = new StringBuilder(size*10);
        for (int i = 0; i < size; i++) {
            String str = Integer.toString(i);
            actual.append(str).append("=").append(str).append(";");
            expected.put(str, str);
        }
        assertEquals(expected, StringHelpers.strToMap(actual.toString()));
    }
}
