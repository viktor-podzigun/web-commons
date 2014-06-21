
package com.googlecode.common.client.app.task;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.core.client.GWT;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Generic {@link BaseResponse} codec.
 */
public interface BaseResponseCodec extends JsonEncoderDecoder<BaseResponse> {

    public static final BaseResponseCodec INSTANCE = 
        GWT.create(BaseResponseCodec.class);

}
