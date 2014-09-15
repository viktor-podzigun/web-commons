
package com.googlecode.common.protocol.dict;

import java.util.List;
import com.googlecode.common.protocol.DataListResponse;


/**
 * Represents response that hold list of {@link DictDTO} objects.
 */
public final class DictListResponse extends DataListResponse<DictDTO>{

    
    public DictListResponse() {
    }
    
    public DictListResponse(List<DictDTO> dataList) {
        super(dataList);
    }

}
