
package com.googlecode.common.protocol;


/**
 * Uses builder pattern to produce requests string.
 */
public final class RequestBuilder {

    private StringBuilder   request;
    private boolean         hasQueryParam;
    
    
    /**
     * Constructs new builder with the given URL pattern.
     * 
     * @param requestPattern    URL pattern
     */
    public RequestBuilder(String requestPattern) {
        if (requestPattern == null || requestPattern.isEmpty()) {
            throw new IllegalArgumentException(
                    "requestPattern is null or empty");
        }
        
        this.request = new StringBuilder(requestPattern);
    }
    
    /**
     * Sets first ID path variable to the given value.
     * 
     * @param id        ID variable value
     * @return          this object
     */
    public RequestBuilder setIdParam(long id) {
        return setParam("id", Long.toString(id));
    }

    /**
     * Sets first path variable with the given name to the given value.
     * 
     * @param name      path variable name
     * @param value     value
     * @return          this object
     * 
     * @throws IllegalArgumentException if path variable with given {@code name} 
     *                  was not found 
     */
    public RequestBuilder setParam(String name, String value) {
        return setParam(name, value, false);
    }

    /**
     * Sets first path variable with the given name to the given value.<br>
     * Does nothing if path variable with given {@code name} was not found.
     *  
     * @param name      path variable name
     * @param value     value
     * @return          this object
     */
    public RequestBuilder setOptionalParam(String name, String value) {
        return setParam(name, value, true);
    }

    /**
     * Sets first path variable with the given name to the given value.
     * 
     * @param name      path variable name
     * @param value     value
     * @param optional  indicates that parameter is optional
     * @return          this object
     * @throws IllegalArgumentException if param with given name was not found
     *                  and {@code optional} is {@code false}
     */
    private RequestBuilder setParam(String name, String value, 
            boolean optional) {
        
        String str = "{" + name + "}";
        int index = request.indexOf(str);
        if (index == -1) {
            if (optional) {
                return this;
            }

            throw new IllegalArgumentException(
                    "No such request param: " + name);
        }
        
        request.replace(index, index + str.length(), value);
        return this;
    }
    
    /**
     * Adds the given query parameter to this builder. Skip parameter if 
     * value is null.
     * 
     * @param name      parameter name
     * @param value     corresponding parameter value
     * @return          this object
     */
    public RequestBuilder addQueryParam(String name, Object value) {
        if (value != null) {
            request.append(hasQueryParam ? '&' : '?')
                .append(name).append('=')
                .append(String.valueOf(value));
            hasQueryParam = true;
        }
        
        return this;
    }

    /**
     * Returns current URL string.
     * @return current URL string
     */
    public String build() {
        return request.toString();
    }

}
