
package com.googlecode.common.client.util;

import java.math.BigDecimal;
import com.google.gwt.i18n.client.NumberFormat;


/**
 * Useful for converting currency to string
 */
public final class CurrencyHelpers {

    // This class should never be instantiated
    private CurrencyHelpers() {
    }
    
    /**
     * Formats cent amount to string with double format to percent currencies 
     * and virtual coins 
     * 
     * @param centAmount value of cent amount that need to be formatted    
     * @return formatted string
     */
    public static String format(long centAmount) {
        return NumberFormat.getFormat("#,##0.00")
                .format(((double)centAmount) / 100);
    }
    
    /**
     * Formats USD cents amount to string with double format and 
     * dollar sign at begin 
     * 
     * @param priceUsd  price in USD cents    
     * @return          formatted string
     */
    public static String formatUsd(int priceUsd) {
        return NumberFormat.getFormat("$#,##0.00")
                .format(((double)priceUsd) / 100);
    }

    /**
     * Formats kopecks to simple price format, for example: 
     * <code>12345.23</code>
     * 
     * @param kopecks   price in kopecks
     * @return          price in simple format: <code>XXXXX.YY</code>
     * 
     * @see #parseSimple(String)
     */
    public static String formatSimple(int kopecks) {
        BigDecimal dec = BigDecimal.valueOf(kopecks);
        return dec.movePointLeft(2).toString();
        
        //return NumberFormat.getFormat("0.00").format(
        //        ((double)kopecks) / 100);
    }
    
    /**
     * Parses price in simple format, for example: <code>12345.23</code> to 
     * kopecks.
     * 
     * @param price     price in simple format: <code>XXXXX.YY</code>
     * @return          price in kopecks
     * 
     * @see #formatSimple(int)
     */
    public static int parseSimple(String price) {
        BigDecimal dec = new BigDecimal(price);
        return dec.movePointRight(2).intValue();
    }

}
