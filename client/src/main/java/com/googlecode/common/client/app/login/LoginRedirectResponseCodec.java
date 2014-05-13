
package com.googlecode.common.client.app.login;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.core.client.GWT;
import com.googlecode.common.protocol.login.LoginRedirectResponse;


/**
 * Common {@link LoginRedirectResponse} JSON decoder.
 */
public interface LoginRedirectResponseCodec extends 
        JsonEncoderDecoder<LoginRedirectResponse> {

    public static final LoginRedirectResponseCodec INSTANCE = 
        GWT.create(LoginRedirectResponseCodec.class);

}
