
package com.googlecode.common.client.util;

import java.util.ArrayList;
import java.util.List;


public final class CollectionsUtil {
    
    
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
}
