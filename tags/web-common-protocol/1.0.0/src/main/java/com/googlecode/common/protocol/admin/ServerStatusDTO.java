
package com.googlecode.common.protocol.admin;

import java.util.Date;


/**
 * Part of server requests and responses, containing server status info.
 */
public final class ServerStatusDTO {

    private Date        startDate;
    
    private String      appVersion;
    private String      appBuild;
    
    private String      restartAuthorLogin;
    private Date        restartDate;
    private int         restartCount;
    
    private long        succeededReqCount;
    private long        failedReqCount;
    
    private int         activeReqCount;
    private int         maxActiveReqCount;

    
    public ServerStatusDTO() {
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public String getAppVersion() {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    
    public String getAppBuild() {
        return appBuild;
    }
    
    public void setAppBuild(String appBuild) {
        this.appBuild = appBuild;
    }

    public String getRestartAuthorLogin() {
        return restartAuthorLogin;
    }
    
    public void setRestartAuthorLogin(String restartAuthorLogin) {
        this.restartAuthorLogin = restartAuthorLogin;
    }
    
    public Date getRestartDate() {
        return restartDate;
    }
    
    public void setRestartDate(Date restartDate) {
        this.restartDate = restartDate;
    }
    
    public int getRestartCount() {
        return restartCount;
    }
    
    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }
    
    public long getSucceededReqCount() {
        return succeededReqCount;
    }
    
    public void setSucceededReqCount(long succeededReqCount) {
        this.succeededReqCount = succeededReqCount;
    }
    
    public long getFailedReqCount() {
        return failedReqCount;
    }
    
    public void setFailedReqCount(long failedReqCount) {
        this.failedReqCount = failedReqCount;
    }
    
    public int getActiveReqCount() {
        return activeReqCount;
    }
    
    public void setActiveReqCount(int activeReqCount) {
        this.activeReqCount = activeReqCount;
    }
    
    public int getMaxActiveReqCount() {
        return maxActiveReqCount;
    }
    
    public void setMaxActiveReqCount(int maxActiveReqCount) {
        this.maxActiveReqCount = maxActiveReqCount;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{startDate: " + startDate
                + (appVersion != null ? ", appVersion: " + appVersion : "")
                + (appBuild != null ? ", appBuild: " + appBuild : "")
                + (restartAuthorLogin != null ? 
                        ", restartAuthorLogin: " + restartAuthorLogin : "")
                + (restartDate != null ? ", restartDate: " + restartDate : "")
                + ", restartCount: " + restartCount
                + ", succeededReqCount: " + succeededReqCount
                + ", failedReqCount: " + failedReqCount
                + ", activeReqCount: " + activeReqCount
                + ", maxActiveReqCount: " + maxActiveReqCount
                + "}";
    }

}
