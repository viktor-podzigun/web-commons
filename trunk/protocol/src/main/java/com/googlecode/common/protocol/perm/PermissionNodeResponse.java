
package com.googlecode.common.protocol.perm;

import com.googlecode.common.protocol.DataResponse;


/**
 * This response contains PermissionNodeDTO object.
 */
public final class PermissionNodeResponse extends DataResponse<PermissionNodeDTO> {

    public PermissionNodeResponse() {
    }
    
    public PermissionNodeResponse(PermissionNodeDTO data) {
        super(data);
    }
    
}
