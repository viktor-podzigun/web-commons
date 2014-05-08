
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link AppSettingsDTO} object.
 */
public final class AppSettingsResponse extends DataResponse<AppSettingsDTO> {

    public AppSettingsResponse() {
    }
    
    public AppSettingsResponse(AppSettingsDTO data) {
        super(data);
    }

}
