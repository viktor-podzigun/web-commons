
package com.googlecode.common.io;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;


/**
 * Contains helper methods for encoding/decoding.
 */
public final class EncodingHelpers {

    public static final Charset UTF8     = Charset.forName("UTF-8");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    
    
    /**
     * Encodes binary data using the base64 algorithm but does not 
     * chunk the output.
     * 
     * @param binaryData    binary data to encode
     * @return              byte array containing only Base64 character data
     */
    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    /**
     * Encodes binary data using the base64 algorithm.
     * 
     * @param binaryData    array containing binary data to encode
     * @param urlSafe       if <code>true</code> this encoder will emit - and _ 
     *                      instead of the usual + and / characters
     * @return              Base64-encoded data
     */
    public static byte[] encodeBase64(byte[] binaryData, boolean urlSafe) {
        return Base64.encodeBase64(binaryData, false, urlSafe);
    }

    /**
     * Decodes Base64 data into octets.
     * 
     * @param base64Data    byte array containing Base64 data
     * @return              array containing decoded data
     */
    public static byte[] decodeBase64(byte[] base64Data) {
        return Base64.decodeBase64(base64Data);
    }

    /**
     * Checks whether the given string contains US-ASCII characters only.
     * 
     * @param text  text to check
     * @return      <code>true</code> if, and only if, the given string 
     *              contains US ASCII characters only, or <code>false</code> 
     *              otherwise
     */
    public static boolean isAllAscii(String text) {
        if (text == null) {
            throw new NullPointerException(text);
        }
        
        return US_ASCII.newEncoder().canEncode(text);
    }

}
