
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link AuthUserDTO} object.
 */
public final class AuthUserResponse extends DataResponse<AuthUserDTO> {
    
    public AuthUserResponse() {
    }
    
    public AuthUserResponse(AuthUserDTO data) {
        super(data);
    }
    
}
