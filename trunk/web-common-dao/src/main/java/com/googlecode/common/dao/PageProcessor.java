
package com.googlecode.common.dao;

import java.util.List;


/**
 * Generic page data processor.
 */
public abstract class PageProcessor<T> {

    public void process(int limit) {
        PageData<T> data = getData(null, limit);
        List<T> entities = data.getEntities();
        int total = data.safeGetTotalCount();
        int count = entities.size();
        for (int i = 0; i < total && count != 0; i += count) {
            if (entities == null) {
                data = getData(i, limit);
                entities = data.getEntities();
                count = entities.size();
            }
            
            processData(entities);
            entities = null;
        }
    }
    
    protected abstract PageData<T> getData(Integer startIndex, int limit);
    
    protected abstract void processData(List<T> data);
    

//    public static void main(String[] args) {
//        final List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
//        new PageProcessor<Integer>() {
//            @Override
//            protected PageData<Integer> getData(Integer startIndex, int limit) {
//                Long total = null;
//                if (startIndex == null) {
//                    total = Long.valueOf(list.size());
//                }
//                
//                int from = (startIndex != null ? startIndex : 0);
//                return new PageData<Integer>(list.subList(from, 
//                        Math.min(from + limit, list.size())), total);
//            }
//
//            @Override
//            protected void processData(List<Integer> data) {
//                System.out.print(" " + data);
//            }
//        }.process(3);
//    }

}
