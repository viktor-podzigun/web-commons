
package com.googlecode.common.dao.hibernate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.googlecode.common.dao.AbstractQueryBuilder;


/**
 * Hibernate query builder.
 */
public final class HibQueryBuilder extends AbstractQueryBuilder {

    private int                 nextTokenNum = 1;
    private Map<String, Object> params;
    
    
    public HibQueryBuilder() {
        super();
    }
    
    public HibQueryBuilder(String prefix) {
        super(prefix);
    }
    
    public <T> TypedQuery<T> build(EntityManager manager, Class<T> queryClass) {
        return build(manager, queryClass, null, null);
    }
    
    public <T> TypedQuery<T> build(EntityManager manager, Class<T> queryClass, 
            String queryPrefix, String querySuffix) {
        
        StringBuilder qb;
        if (queryPrefix != null || querySuffix != null) {
            qb = new StringBuilder(queryPrefix != null ? queryPrefix : "");
            qb.append(getQuery());
            if (querySuffix != null) {
                qb.append(' ').append(querySuffix);
            }
        } else {
            qb = new StringBuilder(getQuery());
        }
        
        StringBuilder orderBy = getOrderBy();
        if (orderBy != null) {
            qb.append(orderBy);
        }
        
        TypedQuery<T> query = manager.createQuery(qb.toString(), queryClass);
        for (Map.Entry<String, Object> entry : getParams().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    
        return query;
    }
    
    @Override
    public HibQueryBuilder addOrderBy(String orderInfo) {
        super.addOrderBy(orderInfo);
        return this;
    }
    
    @Override
    public HibQueryBuilder and(CharSequence cond) {
        super.and(cond);
        return this;
    }
    
    @Override
    public HibQueryBuilder or(CharSequence cond) {
        super.or(cond);
        return this;
    }
    
    @Override
    public HibQueryBuilder andBetween(String cond, Object val1, 
            Object val2) {
        
        super.andBetween(cond, val1, val2);
        return this;
    }
    
    @Override
    public HibQueryBuilder orBetween(String cond, Object val1, 
            Object val2) {
        
        super.orBetween(cond, val1, val2);
        return this;
    }
        
    public HibQueryBuilder and(HibQueryBuilder qb) {
        putAll(qb.getParams());
        return and("(" + qb.getQuery() + ")");
    }

    public HibQueryBuilder or(HibQueryBuilder qb) {
        putAll(qb.getParams());
        return or("(" + qb.getQuery() + ")");
    }

    public HibQueryBuilder and(String cond, String field, Object value) {
        if (checkValue(value)) {
            put(field, value);
            return and(cond);
        }
        
        return this;
    }

    public HibQueryBuilder or(String cond, String field, Object value) {
        if (checkValue(value)) {
            put(field, value);
            return or(cond);
        }
        
        return this;
    }

    @Override
    protected String prepareBetween(String cond, Object val1, Object val2) {
        String t1 = nextToken();
        String t2 = nextToken();
        put(t1, val1);
        put(t2, val2);
        return cond + " BETWEEN :" + t1 + " AND :" + t2;
    }
    
    private String nextToken() {
        return "_t" + System.identityHashCode(this) + "_" + nextTokenNum++;
    }
    
    private Map<String, Object> getParams() {
        if (params == null) {
            return Collections.emptyMap();
        }
        
        return params;
    }

    private void ensureParams() {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
    }
    
    private void putAll(Map<String, Object> params) {
        if (!params.isEmpty()) {
            ensureParams();
            this.params.putAll(params);
        }
    }
    
    private void put(String field, Object value) {
        ensureParams();
        this.params.put(field, value);
    }
    
    @Override
    protected String paramsString() {
        return super.paramsString() 
                + (params != null ? "\nparams: " + params : "");
    }

}
