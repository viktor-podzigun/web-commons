
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link AppConfRespDTO} object.
 */
public final class AppConfResponse extends DataResponse<AppConfRespDTO> {

    public AppConfResponse() {
    }
    
    public AppConfResponse(AppConfRespDTO data) {
        super(data);
    }

}
