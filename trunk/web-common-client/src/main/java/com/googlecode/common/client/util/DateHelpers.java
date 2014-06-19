
package com.googlecode.common.client.util;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;


/**
 * Collection of useful date and time operations and conversions.
 */
public final class DateHelpers {

    /** Date and time format pattern (<code>yyyy-MM-dd HH:mm:ss.SSS</code>) */
    public static final String      DATE_MILLIS     = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /** Date and time format pattern (<code>yyyy-MM-dd HH:mm:ss</code>) */
    public static final String      DATE_AND_TIME   = "yyyy-MM-dd HH:mm:ss";
    
    /** Date only format pattern (<code>yyyy-MM-dd</code>) */
    public static final String      DATE_ONLY       = "yyyy-MM-dd";
    
    /** Time only format pattern (<code>HH:mm:ss</code>) */
    public static final String      TIME_ONLY       = "HH:mm:ss";
    
    /** UTC time zone constant */
    public static final TimeZone    TZ_UTC = TimeZone.createTimeZone(0);
    
    private static final long       MILLIS_IN_MINUTE = 60000L;
    
    
    private DateHelpers() {
    }

    /**
     * Formats date using the specified pattern and time zone.
     *  
     * @param date      date to format
     * @param pattern   date/time format pattern
     * @param timeZone  time zone
     * @return          formatted string
     */
    public static String format(Date date, String pattern, TimeZone timeZone) {
        return DateTimeFormat.getFormat(pattern).format(date, timeZone);
    }

    /**
     * Formats date to date and time string in ISO format "yyyy-MM-dd HH:mm:ss" 
     * using current time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String formatDate(Date date) {
        return DateTimeFormat.getFormat(DATE_AND_TIME).format(date);
    }

    /**
     * Formats date to date only string in format "yyyy-MM-dd" 
     * using current time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String formatDateOnly(Date date) {
        return DateTimeFormat.getFormat(DATE_ONLY).format(date);
    }
    
    /**
     * Formats date to time only string in format "HH:mm:ss" 
     * using current time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String formatTimeOnly(Date date) {
        return DateTimeFormat.getFormat(TIME_ONLY).format(date);
    }
    
    /**
     * Formats date to date and time string in ISO format "yyyy-MM-dd HH:mm:ss" 
     * using UTC time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String utcFormatDate(Date date) {
        return DateTimeFormat.getFormat(DATE_AND_TIME).format(date, TZ_UTC);
    }
    
    /**
     * Converts the given date to UTC time zone.
     *  
     * @param date  date to convert
     * @return      UTC date in milliseconds
     */
    public static long utc(Date date) {
        @SuppressWarnings("deprecation")
        int timeZoneOffset = date.getTimezoneOffset();
        
        return date.getTime() + (timeZoneOffset * MILLIS_IN_MINUTE);
    }

    /**
     * Formats date to date only string in format "yyyy-MM-dd" 
     * using UTC time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String utcFormatDateOnly(Date date) {
        return DateTimeFormat.getFormat(DATE_ONLY).format(date, TZ_UTC);
    }
    
    /**
     * Formats date to time only string in format "HH:mm:ss" 
     * using UTC time zone.
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String utcFormatTimeOnly(Date date) {
        return DateTimeFormat.getFormat(TIME_ONLY).format(date, TZ_UTC);
    }
    
    /**
     * Parses the given date string in one of the following format: 
     * "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" to date using current time zone.
     * 
     * @param str   date in string format
     * @return      date object
     * @throws      IllegalArgumentException if the entire text could not be 
     *              converted into a number 
     */
    public static Date parseDate(String str) {
        if (str.length() == DATE_ONLY.length()) {
            return DateTimeFormat.getFormat(DATE_ONLY).parse(str);
        }
        
        return DateTimeFormat.getFormat(DATE_AND_TIME).parse(str);
    }
    
    /**
     * Parses the given date string in one of the following format: 
     * "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" to date using UTC time zone.
     * 
     * @param str   date in string format
     * @return      date object
     * @throws      IllegalArgumentException if the entire text could not be 
     *              converted into a number    
     */
    public static Date utcParseDate(String str) {
        if (str.length() == DATE_ONLY.length()) {
            return DateTimeFormat.getFormat(DATE_ONLY + "Z").parse(str 
                    + "+0000");
        }
        
        return DateTimeFormat.getFormat(DATE_AND_TIME + "Z").parse(str 
                + "+0000");
    }
    
}
