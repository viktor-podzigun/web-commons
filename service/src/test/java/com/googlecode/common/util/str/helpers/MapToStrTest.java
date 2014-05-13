
package com.googlecode.common.util.str.helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;
import com.googlecode.common.util.StringHelpers;


/**
 * 
 */
public class MapToStrTest extends Assert {

    
    @Test(expected = NullPointerException.class)
    public void nullPointerException() {
        StringHelpers.mapToStr(null);
    }
    
    @Test
    public void emptyMap() {
        String actual = StringHelpers.mapToStr(new TreeMap<String, String>());
        assertEquals("", actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void nullKey() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "Some value");
        StringHelpers.mapToStr(map);
    }
    
    @Test
    public void nullValue() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Some key", null);
        String actual = StringHelpers.mapToStr(map);
        assertEquals("Some key", actual);
    }

    @Test
    public void emptyKey() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("", "something");
        String actual = StringHelpers.mapToStr(map);
        assertEquals("=something", actual);
    }
    
    @Test
    public void correctMap() {
        int size = 50;
        Map<String, String> map = new LinkedHashMap <String, String>();
        StringBuilder expected = new StringBuilder(size*10);
        for (int i = 0; i < size; i++) {
            String str = Integer.toString(i);
            expected.append(str).append("=").append(str).append(";");
            map.put(str, str);
        }
        assertEquals(expected.substring(0, expected.length() - 1), 
                StringHelpers.mapToStr(map));
    }

}
