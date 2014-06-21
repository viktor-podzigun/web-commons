
package com.googlecode.common.util;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


/**
 * String useful utilities set.
 */
public final class StringHelpers {

    private static final Random PASS_RANDOM = new Random(System.nanoTime());
    private static final String PASS_CHARS  = 
        "1234567890abcdefghijklmnopqrstuvwxyz";
    
    
    private StringHelpers() {
    }
    
    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.isEmpty());
    }
    
    public static String ensureNotNull(String str) {
        return (str == null ? "" : str);
    }
    
    public static String ensureNullIfEmpty(String str) {
        if (str == null) {
            return null;
        }
        return (str.isEmpty() ? null : str);
    }
    
    public static boolean isNumberString(String s) {
        for (int j = 0, length = s.length(); j < length; j++) {
            if (!Character.isDigit(s.charAt(j))) {
                return false;
            }
        }

        return true;
    }
    
    public static byte[] hex2ByteArray(String hex) {
        if (hex == null) {
            throw new NullPointerException("hex");
        }
        
        // get and check length
        final int len = hex.length();
        final int count = len >> 1;
        if ((count << 1) != len) {
            throw new IllegalArgumentException("Wrong hex length");
        }
        
        byte[] arr = new byte[count];
        for (int i = 0; i < count; i++) {
            int j = i << 1;
            int high = Character.digit(hex.charAt(j), 16);
            int low  = Character.digit(hex.charAt(j + 1), 16);
            
            arr[i] = (byte)((high << 4) | low);
        }
        
        return arr;
    }

    public static String byteArray2Hex(byte[] hash) {
        return byteArray2Hex(hash, false);
    }

    public static String byteArray2Hex(byte[] hash, boolean upperCase) {
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter();
        String format = (upperCase ? "%02X" : "%02x");
        for (byte b : hash) {
            formatter.format(format, b);
        }
        
        return formatter.toString();
    }

    /**
     * Converts parameters map to string representation. 
     * Opposite to {@link #strToMap(String)}.
     * 
     * @param map   map to convert
     * @return string representation of map
     * 
     * @throws NullPointerException if map, key or value is <code>null</code>
     * @throws IllegalArgumentException if at least one key contains one of 
     *              the special characters <code>['=', '%', ';']</code>
     * 
     * @see #strToMap(String)
     */
    public static String mapToStr(Map<String, String> map) {
        if (map == null) {
            throw new NullPointerException("map");
        }
        
//        Map<String, String> sortedMap = map;
//        final int size = map.size();
//        if (size > 0) {
//            // we need to sort our map first, so we get the same order 
//            // for the same set of keys
//            sortedMap = new TreeMap<String, String>(map);
//        }
        
        StringBuilder sbAll = new StringBuilder(map.size() * 32);
        int i = 0;
        for (Entry<String, String> entry : map.entrySet()) {
            if (i++ > 0) {
                sbAll.append(';');
            }
            
            // check key
            String key = entry.getKey();
            if (key == null) {
                throw new NullPointerException("key");
            }
            for (int j = 0, count = key.length(); j < count; j++) {
                char c = key.charAt(j);
                if (c == '=' || c == '%' || c == ';') {
                    throw new IllegalArgumentException("key: \"" + key + "\"");
                }
            }
            
            String val = entry.getValue();
            StringBuilder sbVal = null;
            if (val != null) {
                // substituting special characters in value with their codes
                for (int j = 0, count = val.length(); j < count; j++) {
                    char c = val.charAt(j);
                    if (c == '=' || c == '%' || c == ';') {
                        if (sbVal == null) {
                            sbVal = new StringBuilder(count * 2);
                            sbVal.append(val, 0, j);
                        }
                        
                        sbVal.append('%').append(Integer.toHexString(c));
                    
                    } else if (sbVal != null) {
                        sbVal.append(c);
                    }
                }
            }
            
            sbAll.append(key);
            if (sbVal != null) {
                sbAll.append('=').append(sbVal);
            
            } else if (val != null) {
                sbAll.append('=').append(val);
            }
        }

        return sbAll.toString();
    }
    
    /**
     * Unpacks map from it's string representation. 
     * Opposite to {@link #mapToStr(Map)}.
     * 
     * @param str string with packed map
     * @return unpacked map
     * 
     * @throws NullPointerException if str is <code>null</code>
     * 
     * @see #mapToStr(Map)
     */
    public static Map<String, String> strToMap(String str) {
        if (str == null) {
            throw new NullPointerException("str");
        }
        
        boolean isValue = false;
        int start = 0;
        final int count = str.length();
        String key = null;
        StringBuilder sbVal = null; // for substituting special characters
        
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int j = 0; j < count; j++) {
            final char c = str.charAt(j);
            if (isValue) {
                if (c == ';') {
                    String val;
                    if (sbVal != null) {
                        val   = sbVal.toString();
                        sbVal = null;
                    } else {
                        val   = str.substring(start, j);
                    }
                    
                    map.put(key, val);
                    key     = null;
                    isValue = false;
                    start   = j + 1;
                    
                } else if (c == '%') { // special character started
                    if (sbVal == null) {
                        sbVal = new StringBuilder(32);
                        sbVal.append(str, start, j);
                    }
                    
                    // convert 2-digit HEX code of special character 
                    sbVal.append((char)Integer.parseInt(
                            str.substring(j + 1, j + 3), 16));
                    j += 2;
                
                } else if (sbVal != null) {
                    sbVal.append(c);
                }
            } else {
                if (c == '=') {
                    key     = str.substring(start, j);
                    isValue = true;
                    start   = j + 1;
                
                } else if (c == ';') {
                    map.put(str.substring(start, j), null);
                    start   = j + 1;
                }
            }
        }
        
        if (key != null) {
            String val;
            if (sbVal != null) {
                val   = sbVal.toString();
                sbVal = null;
            } else {
                val   = str.substring(start, count);
            }
            
            map.put(key, val);
        
        } else if (start != count) {
            map.put(str.substring(start, count), null);
        }

        return map;
    }
    
    public static String join(String separator, String... data) {
        return join(separator, (Object[]) data);
    }
    
    public static String join(String separator, Object... data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    sb.append(separator);
                }
                
                sb.append(data[i].toString());
            }
        }
        
        return sb.toString();
    }

    public static boolean isEqual(String s1, String s2) {
        if (s1 != null) {
            return s1.equals(s2);
        }
    
        return (s2 == null);
    }
    
    public static String str(Object obj) {
        return (obj != null ? obj.toString() : "");
    }
    
    public static String trim(String str) {
        return (str != null ? str.trim() : null);
    }
    
    public static String generatePassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length: " + length);
        }
        
        final int charsLen = PASS_CHARS.length();
        StringBuilder sb = new StringBuilder(length);
        while (--length >= 0) {
            sb.append(PASS_CHARS.charAt(PASS_RANDOM.nextInt(charsLen)));
        }
        
        return sb.toString();
    }
    
    /**
     * Divides the given string into parts with specified maximum length.
     * 
     * @param str       string to divide into parts
     * @param maxLen    maximum length of one part
     * @return          parts of the given string
     */
    public static String[] divide(String str, int maxLen) {
        int strLen = str.length();
        int partsLen = strLen / maxLen;
        if (strLen % maxLen != 0) {
            partsLen++;
        }
        
        String[] parts = new String[partsLen];
        int begin = 0;
        for (int i = 0; i < partsLen; i++) {
            int end = begin + maxLen;
            if (end > strLen) {
                end = strLen;
            }
            
            parts[i] = str.substring(begin, end);
            begin = end;
        }
        
        return parts;
    }
    
    
    static void testStrToMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("test3", "12%3");
        map.put("test2", "1=23;");
        map.put("test1", "1234");
        map.put("test0", "");
        map.put("test",  null);
        
        String str = mapToStr(map);
        System.out.println("map: " + map + "\n\tstr: " + str);
        
        Map<String, String> map2 = strToMap(str);
        System.out.println("map: " + map2);
        System.out.println(map.equals(map2));
    }
    
    static void testHex() {
        byte[] bytes = "Hello World!".getBytes();
        String hex = byteArray2Hex(bytes).toUpperCase();
        byte[] arr = hex2ByteArray(hex);
        
        System.out.println(hex);
        System.out.println(byteArray2Hex(arr));
        System.out.println(Arrays.equals(bytes, arr));
    }
    
    public static void main(String[] args) {
        testStrToMap();
        //testHex();
    }

}
