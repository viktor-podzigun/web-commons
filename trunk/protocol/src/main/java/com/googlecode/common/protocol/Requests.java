
package com.googlecode.common.protocol;


/**
 * Contains common functionality for requests classes.
 */
public abstract class Requests {

    // Common parameters names
    
    public static final String  PARAM_ID    = "id";
    

    protected static String getById(String reqPattern, long id) {
        RequestBuilder builder = new RequestBuilder(reqPattern);
        builder.setIdParam(id);
        return builder.build();
    }

}
