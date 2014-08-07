
package com.googlecode.common.client.util;


/**
 * Contains bit set helper methods.
 */
public final class Bits {
    
    private Bits() {
    }
    
    public static long add(long bits, long mask) {
        return (bits | mask);
    }

    public static long del(long bits, long mask) {
        return (bits & ~mask);
    }

    public static boolean all(long bits, long mask) {
        return (bits & mask) == mask;
    }
    
    public static boolean any(long bits, long mask) {
        return (bits & mask) != 0;
    }
    
}
