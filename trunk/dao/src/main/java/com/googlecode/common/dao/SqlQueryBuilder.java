
package com.googlecode.common.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * SQL query builder.
 */
public final class SqlQueryBuilder extends AbstractQueryBuilder {
    
    private List<Object>    params;
    

    public SqlQueryBuilder() {
        super();
    }
    
    public SqlQueryBuilder(String prefix) {
        super(prefix);
    }
    
    public String build() {
        return build(null, null);
    }
    
    public String build(String queryPrefix, String querySuffix) {
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
        
        return qb.toString();
    }
    
    public Object[] getArgs() {
        return getParams().toArray();
    }
    
    @Override
    public SqlQueryBuilder addOrderBy(String orderInfo) {
        super.addOrderBy(orderInfo);
        return this;
    }
    
    @Override
    public SqlQueryBuilder and(CharSequence cond) {
        super.and(cond);
        return this;
    }
    
    @Override
    public SqlQueryBuilder or(CharSequence cond) {
        super.or(cond);
        return this;
    }
    
    @Override
    public SqlQueryBuilder andBetween(String cond, Object val1, 
            Object val2) {
        
        super.andBetween(cond, val1, val2);
        return this;
    }
    
    @Override
    public SqlQueryBuilder orBetween(String cond, Object val1, 
            Object val2) {
        
        super.orBetween(cond, val1, val2);
        return this;
    }
        
    public SqlQueryBuilder and(SqlQueryBuilder qb) {
        addAll(qb.getParams());
        and("(" + qb.getQuery() + ")");
        return this;
    }

    public SqlQueryBuilder or(SqlQueryBuilder qb) {
        addAll(qb.getParams());
        or("(" + qb.getQuery() + ")");
        return this;
    }

    public SqlQueryBuilder and(String cond, Object value) {
        if (checkValue(value)) {
            add(value);
            and(cond);
        }
        
        return this;
    }

    public SqlQueryBuilder or(String cond, Object value) {
        if (checkValue(value)) {
            add(value);
            or(cond);
        }
        
        return this;
    }

    private List<Object> getParams() {
        if (params == null) {
            return Collections.emptyList();
        }
        
        return params;
    }
    
    private void ensureParams() {
        if (params == null) {
            params = new ArrayList<Object>();
        }
    }
    
    private void addAll(List<Object> params) {
        if (!params.isEmpty()) {
            ensureParams();
            this.params.addAll(params);
        }
    }
    
    private void add(Object value) {
        ensureParams();
        this.params.add(value);
    }
    
    @Override
    protected String prepareBetween(String cond, Object val1, Object val2) {
        add(val1);
        add(val2);
        return cond + " BETWEEN ? AND ?";
    }
    
    @Override
    protected String paramsString() {
        return super.paramsString() 
                + (params != null ? "\nparams: " + params : "");
    }

}
