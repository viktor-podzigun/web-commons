
package com.googlecode.common.protocol.login;

import java.util.Collections;
import java.util.List;


/**
 * Define system's applications menu structure.
 */
public final class AppMenuDTO {

    private String  title;
    private String  url;
    
    private List<AppMenuDTO>  subMenu;
    
    
    public AppMenuDTO() {
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public List<AppMenuDTO> safeGetSubMenu() {
        if (subMenu == null) {
            return Collections.emptyList();
        }
        
        return subMenu;
    }
    
    /**
     * @deprecated You don't need to use it. This method is left for 
     * serialization layer. Instead, please, use the corresponding safe-method.
     * 
     * @see #safeGetRoles()
     */
    @Deprecated
    public List<AppMenuDTO> getSubMenu() {
        return subMenu;
    }
    
    public void setSubMenu(List<AppMenuDTO> subMenu) {
        this.subMenu = subMenu;
    }

}
