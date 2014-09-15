
package com.googlecode.common.protocol.admin;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * This response contains list of {@link ServerModuleDTO} objects.
 */
public final class ServerModulesResponse extends 
        DataListResponse<ServerModuleDTO> {

    public ServerModulesResponse() {
    }
    
    public ServerModulesResponse(List<ServerModuleDTO> dataList) {
        super(dataList);
    }

}
