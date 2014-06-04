
package com.googlecode.common.protocol.admin;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains {@link ServerStatusDTO} object.
 */
public final class ServerStatusResponse extends DataResponse<ServerStatusDTO> {
    
    public ServerStatusResponse() {
    }
    
    public ServerStatusResponse(ServerStatusDTO data) {
        super(data);
    }
    
}
