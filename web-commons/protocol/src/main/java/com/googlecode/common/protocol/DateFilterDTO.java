
package com.googlecode.common.protocol;

import java.util.Date;


/**
 * Contains common date info used for filtering.
 */
public class DateFilterDTO {

    private Date    startDate;
    private Date    endDate;
    
    
    public DateFilterDTO() {
    }
    
    public DateFilterDTO(Date startDate, Date endDate) {
        this.startDate  = startDate;
        this.endDate    = endDate;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    protected String paramString() {
        return (startDate != null ? "startDate: " + startDate : "")
                + (endDate != null ? ", endDate: " + endDate : "");
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{" + paramString() + "}";
    }

}
