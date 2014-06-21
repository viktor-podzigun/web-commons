
package com.googlecode.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Contains collections helper methods.
 */
public final class CollectionsUtil {
    
    public static final int[]   EMPTY_INT_ARR   = new int[0];
    public static final long[]  EMPTY_LONG_ARR  = new long[0];
    
    
    private CollectionsUtil() {
    }

    public static <T> List<T> addToList(List<T> list, T data) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        
        list.add(data);
        return list;
    }
    
    public static <T> List<T> addAllToList(List<T> list, Collection<T> data) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        
        list.addAll(data);
        return list;
    }

    public static <T> Set<T> addToSet(Set<T> list, T data) {
        if (list == null) {
            list = new HashSet<T>();
        }
        
        list.add(data);
        return list;
    }
    
    public static <T> List<T> ensureNotNull(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        
        return list;
    }
    
    public static int[] toIntArray(List<Integer> list) {
        if (list.isEmpty()) {
            return EMPTY_INT_ARR;
        }
        
        int i = 0;
        int[] dst = new int[list.size()];
        for (Integer val : list) {
            dst[i] = (val == null ? 0 : val);
        }
        
        return dst;
    }
    
    public static List<Integer> toIntList(int... arr) {
        final int len = arr.length;
        if (len == 0) {
            return Collections.emptyList();
        }
        
        List<Integer> list = new ArrayList<Integer>(len);
        for (int val : arr) {
            list.add(val);
        }
        
        return list;
    }
    
    public static boolean isNullOrEmpty(Collection<?> data) {
        return (data == null || data.isEmpty());
    }
    
}
