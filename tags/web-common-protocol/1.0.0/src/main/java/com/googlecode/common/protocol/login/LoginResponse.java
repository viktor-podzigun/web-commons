
package com.googlecode.common.protocol.login;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains LoginRespDTO object.
 */
public class LoginResponse extends DataResponse<LoginRespDTO> {

    public LoginResponse() {
    }
    
    public LoginResponse(int status, String message) {
        super(status, message);
    }
    
    public LoginResponse(LoginRespDTO data) {
        super(data);
    }
    
}
