
package com.googlecode.common.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.googlecode.i18n.annotations.MessageProvider;


/**
 * Localized common response messages.
 */
@MessageProvider
public enum CommonResponses implements ResponseMessage {

    /** Request successfully accepted           */
    OK(0),
    
    /** Internal server error, try again later  */
    INTERNAL_SERVER_ERROR(401),

    /** User authentication failure             */
    AUTHENTICATION_FAILED(402),

    /** Request is broken or not full           */
    INVALID_REQUEST(403),

    /** Access denied for specified entity      */
    ACCESS_DENIED(404),
    
    /** Entity not found                        */
    ENTITY_NOT_FOUND(405),
    
    /** Entity already exists                   */
    ENTITY_ALREADY_EXISTS(406),
    
    ;
    
    private final int   status;
    
    
    private CommonResponses(int status) {
        this.status = status;
    }
    
    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage(Locale locale) {
        String key = toString();
        ResourceBundle bundle = ResourceBundle.getBundle(
                CommonResponses.class.getName(), locale);
        
        try {
            return bundle.getString(key);
        
        } catch (MissingResourceException x) {
            return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    public static CommonResponses valueOf(int status) {
        for (CommonResponses val : values()) {
            if (val.status == status) {
                return val;
            }
        }
        
        return null;
    }
}
