
package com.googlecode.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Collection of useful date and time operations and conversions.
 */
public final class DateHelpers {
    
    private DateHelpers() {
    }
    
    /** Minimum possible date value */
    public static final Date MIN_DATE           = new Date(0L); // 1970 year

    /** Maximum possible date value */
    public static final Date MAX_DATE           = new Date(3471285600000L); // 2080 year
    
    /** Number of milliseconds in a day.  */
    public static final long MILLIS_IN_DAY      = 1000 * 60 * 60 * 24;
    
    
    /**
     * Returns start of the day for the given date time (yyyy-MM-dd 00:00:00.000).
     * 
     * @param dateTime  original date time 
     * @return          date and time for the start of the day
     */
    public static Date getDayStart(Date dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return new Date(getDayStart(dateTime.getTime()));
    }
    
    /**
     * Returns start of the day for the given date time (yyyy-MM-dd 00:00:00.000).
     * 
     * @param dateTime  original date time, in millis 
     * @return          date and time for the start of the day
     */
    public static long getDayStart(long dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * Returns end of the day for the given date time (yyyy-MM-dd 23:59:59.999).
     * 
     * @param dateTime original date time, in millis
     * @return         date and time for the end of the day
     */
    public static Date getDayEnd(Date dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return new Date(getDayEnd(dateTime.getTime()));
    }

    /**
     * Returns end of the day for the given date time (yyyy-MM-dd 23:59:59.999).
     * 
     * @param dateTime original date time, in millis
     * @return         date and time for the end of the day
     */
    public static long getDayEnd(long dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - 1L;
    }

    /**
     * Formats date to date and time string in ISO format: yyyy-MM-ddTHH:mm:ss
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String formatDate(Date date) {
        return formatDate(date.getTime());
    }
    
    /**
     * Formats date to date and time string in ISO format: yyyy-MM-ddTHH:mm:ss
     *  
     * @param date  date to format, in millis
     * @return      formatted string
     */
    public static String formatDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        return dateFormat.format(new Date(date));
    }
    
    /**
     * Formats date to date and time string in format "yyyy-MM-dd" 
     *  
     * @param date  date to format
     * @return      formatted string
     */
    public static String formatDateOnly(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    
    /**
     * Parses date and time from the given string in format 
     * <code>yyyy-MM-ddTHH:mm:ss</code>
     * 
     * @param date  date in string representation
     * @return      date object
     */
    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss");
        try {
            return dateFormat.parse(date);
        
        } catch (ParseException x) {
            throw new RuntimeException(x);
        }
    }
    
    /**
     * Parses date from the given string in format <code>yyyy-MM-dd</code>
     * 
     * @param date  date in string representation
     * @return      date object
     */
    public static Date parseDateOnly(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        
        } catch (ParseException x) {
            throw new RuntimeException(x);
        }
    }
    
    /**
     * Days between two dates.
     * 
     * @param startDate first date
     * @param endDate   second date
     * @return          days between specified two dates
     */
    public static int daysBetween(Date startDate, Date endDate) {
        return (int)((endDate.getTime() - startDate.getTime()) / MILLIS_IN_DAY);
    }
   
    /**
     * Returns next day date for the given date.
     * 
     * @param date  source date, in millis
     * @return      next day date, in millis
     */
    public static long nextDay(long date) {
        return (date + MILLIS_IN_DAY);
    }

    public static boolean isEqual(Date d1, Date d2) {
        if (d1 != null) {
            return d1.equals(d2);
        }
    
        return (d2 == null);
    }
    
}
