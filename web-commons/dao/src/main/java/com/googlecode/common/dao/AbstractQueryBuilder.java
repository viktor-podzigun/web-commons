
package com.googlecode.common.dao;

import java.util.Collection;


/**
 * Contains common functionality to build SQL queries.
 */
public abstract class AbstractQueryBuilder {

    private final StringBuilder query;
    private boolean             hasCond;
    
    private StringBuilder       orderBy;
    

    protected AbstractQueryBuilder() {
        this(null);
    }

    protected AbstractQueryBuilder(String prefix) {
        query = new StringBuilder(prefix != null ? prefix : "");
    }
    
    protected AbstractQueryBuilder addOrderBy(String orderInfo) {
        if (orderBy == null) {
            orderBy = new StringBuilder(" ORDER BY ");
        } else {
            orderBy.append(", ");
        }
        
        orderBy.append(orderInfo);
        return this;
    }

    protected AbstractQueryBuilder and(CharSequence cond) {
        query.append(hasCond ? " AND " : " ").append(cond);
        hasCond = true;
        return this;
    }

    protected AbstractQueryBuilder or(CharSequence cond) {
        query.append(hasCond ? " OR " : " ").append(cond);
        hasCond = true;
        return this;
    }

    protected AbstractQueryBuilder andBetween(String cond, Object val1, 
            Object val2) {
        
        if (val1 != null && val2 != null) {
            and(prepareBetween(cond, val1, val2));
        }
        
        return this;
    }
    
    protected AbstractQueryBuilder orBetween(String cond, Object val1, 
            Object val2) {
        
        if (val1 != null && val2 != null) {
            or(prepareBetween(cond, val1, val2));
        }
        
        return this;
    }
    
    protected abstract String prepareBetween(String cond, Object val1, 
            Object val2);
    
    protected static boolean checkValue(Object val) {
        if (val instanceof Collection) {
            return !((Collection<?>)val).isEmpty();
        }
        
        if (val instanceof Object[]) {
            return ((Object[])val).length != 0;
        }
        
        return (val != null);
    }
    
    protected StringBuilder getQuery() {
        return query;
    }
    
    protected StringBuilder getOrderBy() {
        return orderBy;
    }
    
    protected String paramsString() {
        return "query: " + query.toString() 
                + (orderBy != null ? orderBy : "");
    }
    
    @Override
    public String toString() {
        return paramsString();
    }

}
