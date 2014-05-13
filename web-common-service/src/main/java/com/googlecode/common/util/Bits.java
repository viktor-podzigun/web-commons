
package com.googlecode.common.util;

import java.util.Arrays;
import java.util.BitSet;


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
    
    public static boolean isAllSet(BitSet bs) {
        final int cardinality = bs.cardinality();
        return (cardinality != 0 && cardinality == bs.size());
    }
    
    public static BitSet fromIntArray(int[] bits) {
        BitSet set = new BitSet(bits.length << 5);
        for (int i = bits.length - 1; i >= 0; i--) {
            final int word = bits[i];
            if (word != 0) {
                for (int bit = 31; bit >= 0; bit--) {
                    if ((word & (1 << bit)) != 0) {
                        set.set((i << 5) + bit);
                    }
                }
            }
        }
        
        return set;
    }
    
    public static int[] toIntArray(BitSet set) {
        final int cardinality = set.cardinality();
        if (cardinality == 0) {
            return CollectionsUtil.EMPTY_INT_ARR;
        }
        
        int[] bits = new int[((set.length() - 1) >>> 5) + 1];
        if (cardinality == set.size()) {
            // special case: all bits are set
            Arrays.fill(bits, -1);
            return bits;
        }
        
        for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
            bits[i >>> 5] |= (1 << i);
        }
        
        return bits;
    }
    
    public static BitSet fromLongArray(long[] bits) {
        BitSet set = new BitSet(bits.length << 6);
        for (int i = bits.length - 1; i >= 0; i--) {
            final long word = bits[i];
            if (word != 0L) {
                for (int bit = 63; bit >= 0; bit--) {
                    if ((word & (1L << bit)) != 0L) {
                        set.set((i << 6) + bit);
                    }
                }
            }
        }
        
        return set;
    }
    
    public static long[] toLongArray(BitSet set) {
        return toLongArray(set, 0);
    }
    
    public static long[] toLongArray(BitSet set, int minArrLength) {
        final int count = Math.max(minArrLength, 
                ((set.length() - 1) >> 6) + 1);
        if (count == 0) {
            return CollectionsUtil.EMPTY_LONG_ARR;
        }
        
        long[] bits = new long[count];
        if (set.cardinality() == set.size()) {
            // special case: all bits are set
            Arrays.fill(bits, -1L);
            return bits;
        }
        
        for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
            bits[i >>> 6] |= (1L << i);
        }
        
        return bits;
    }
    
    static boolean testAllSet(BitSet set, boolean isAll) {
        boolean success = isAllSet(set);
        success = (success == isAll);
        System.out.println(set + " isAllSet(" + isAll + ") " + success);
        return success;
    }
    
    static boolean testFromToIntArray(BitSet set) {
        int[] bits = toIntArray(set);

        boolean success = set.equals(fromIntArray(bits));
        System.out.println(set + " FromToIntArray(" + bits.length + ") " 
                + success);
        
        return success;
    }
  
    static boolean testFromToLongArray(BitSet set) {
        long[] bits = toLongArray(set);

        boolean success = set.equals(fromLongArray(bits));
        System.out.println(set + " FromToLongArray(" + bits.length + ") " 
                + success);
        
        return success;
    }
  
    public static void main(String[] args) {
        BitSet allSet = new BitSet();
        for (int i = 0; i < 64; i++) {
            allSet.set(i);
        }
        
        BitSet set = new BitSet(65);
        set.set(0);
        set.set(1);
        set.set(31);
        set.set(32);
        set.set(64);
        set.set(65);
        
        boolean success = true;
        success = success && testAllSet(allSet, true);
        success = success && testAllSet(set, false);
        success = success && testFromToIntArray(allSet);
        success = success && testFromToIntArray(set);
        success = success && testFromToLongArray(allSet);
        success = success && testFromToLongArray(set);
        
        BitSet emptySet = new BitSet();
        long[] arr = toLongArray(emptySet, 1);
        success = success && (arr.length >= 1);
        
        if (!success) {
            System.err.println("Some of tests are FAILED");
        }
    }
    
}
