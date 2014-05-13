
package com.googlecode.common.util;

import java.util.Random;


/**
 * Contains number helper methods.
 */
public final class NumUtils {

    private static final Random RAND = new Random(System.nanoTime());
    
    
    private NumUtils() {
    }

    public static boolean ensureNotNull(Boolean val) {
        return (val == null ? false : val);
    }
    
    public static Boolean ensureNull(boolean val) {
        return (val == false ? null : val);
    }
    
    public static int ensureNotNull(Integer val) {
        return (val == null ? 0 : val);
    }
    
    public static Integer ensureNull(int val) {
        return (val == 0 ? null : val);
    }
    
    public static long ensureNotNull(Long val) {
        return (val == null ? 0L : val);
    }
    
    public static Long ensureNull(long val) {
        return (val == 0L ? null : val);
    }
    
    /**
     * Returns a psuedo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min   minimim value, inclusive
     * @param max   maximim value, inclusive
     * @return      integer between min and max, inclusive
     * 
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return min + RAND.nextInt((max - min) + 1);
    }
    
}
