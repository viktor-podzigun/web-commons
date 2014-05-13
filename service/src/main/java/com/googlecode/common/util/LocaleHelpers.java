
package com.googlecode.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * Contains useful Locale utility methods.
 */
public final class LocaleHelpers {

    private static final Set<Locale> LOCALES = new HashSet<Locale>(
            Arrays.asList(Locale.getAvailableLocales()));
    
    
    private LocaleHelpers() {
    }

    /**
     * Converts a String to a Locale.
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.
     *
     * <pre>
     *   LocaleUtils.toLocale("en")        == new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")     == new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_GB_xxx") == new Locale("en", "GB", "xxx") (*)
     * </pre>
     *
     * <p>(*) The behaviour of the JDK variant constructor changed between 
     * JDK1.3 and JDK1.4. In JDK1.3, the constructor upper cases the variant, 
     * in JDK1.4, it doesn't. Thus, the result from getVariant() may vary 
     * depending on your JDK.
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     *
     * @param str   locale code to convert
     * @return      a Locale, null if null input
     * 
     * @throws NullPointerException if input string is null
     * @throws IllegalArgumentException if the string in an invalid format
     */
    public static Locale toLocale(String str) {
        if (str == null) {
            throw new NullPointerException("str");
        }
        
        final int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        if (len == 2) {
            return new Locale(str, "");
        }
        
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        char ch3 = str.charAt(3);
        if (ch3 == '_') {
            return new Locale(str.substring(0, 2), "", str.substring(4));
        }
        
        char ch4 = str.charAt(4);
        if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        }
        
        if (str.charAt(5) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        
        return new Locale(str.substring(0, 2), str.substring(3, 5), 
                str.substring(6));
    }

    /**
     * Checks if the specified locale is in the list of available (installed) 
     * locales.
     *
     * @param locale    Locale object to check
     * @return          <code>true</code> if the locale is a known locale and
     *                  <code>false</code> otherwise
     */
    public static boolean isAvailableLocale(Locale locale) {
        return LOCALES.contains(locale);
    }

    public static void main(String[] args) {
        boolean isValid = true;
        try {
            isValid = isAvailableLocale(toLocale("en_US"));
        
        } catch (IllegalArgumentException x) {
            isValid = false;
        }
        
        System.out.println(isValid);
    }
    
}
