
package com.googlecode.common.client.util;

import java.util.ArrayList;
import java.util.List;


public final class CollectionsUtil {

    public interface Predicate<P> {

        public boolean predicate(P object);
    }

    private CollectionsUtil() {
    }

    public static <T> List<T> addToList(List<T> list, T data) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        
        list.add(data);
        return list;
    }
    
    public static <T> List<T> addAllToList(List<T> list, List<T> data) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        
        list.addAll(data);
        return list;
    }

    /**
     * Selects first element which satisfies predicate
     */
    public static <P, T extends P> T first(List<T> list,
            Predicate<P> predicate) {
        for (T e : list) {
            if (predicate.predicate(e)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Tests whether a predicate holds for some of the elements of the list
     */
    public static <P, T extends P> boolean exists(List<T> list,
            Predicate<P> predicate) {
        return first(list, predicate) != null;
    }

    /**
     * Selects all elements which satisfy a predicate
     * 
     * @return new {@code List} containing filtered elements
     */
    public static <P, T extends P> List<T> filter(List<T> list,
            Predicate<P> predicate) {
        List<T> filteredList = new ArrayList<T>();

        for (T e : list) {
            if (predicate.predicate(e)) {
                filteredList.add(e);
            }
        }

        return filteredList;
    }
}
