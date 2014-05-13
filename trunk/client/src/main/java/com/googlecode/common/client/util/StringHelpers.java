
package com.googlecode.common.client.util;


/**
 * String useful utilities set.
 */
public final class StringHelpers {

    
    private StringHelpers() {
    }

    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.isEmpty());
    }
    
    public static String nullIfEmpty(String str) {
        if (str == null) {
            return null;
        }
        
        return (str.isEmpty() ? null : str);
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

    public static String makeHtmlString(String str) {
        if (str == null) {
            return "";
        }
        
        return "<html>" + str.replace("\n", "<br>") + "</html>";
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
    
}
