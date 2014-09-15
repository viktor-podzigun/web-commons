
package com.googlecode.common.protocol;

import java.util.Collections;
import java.util.List;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Generic response for retrieving list of data of any type.
 */
public class DataListResponse<T> extends BaseResponse {
    
    private List<T>    dataList;
    private Integer    totalCount; // total count of rows, used for paging
    
    
    public DataListResponse() {
    }
    
    public DataListResponse(List<T> dataList) {
        this.dataList = dataList;
    }
    
    public List<T> safeGetDataList() {
        if (dataList == null) {
            return Collections.emptyList();
        }
        
        return dataList;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetDataList()
     */
    @Deprecated
    public List<T> getDataList() {
        return dataList;
    }
    
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
