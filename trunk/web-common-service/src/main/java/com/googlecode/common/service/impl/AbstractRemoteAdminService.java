
package com.googlecode.common.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.common.http.RequestParams;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.Permission;
import com.googlecode.common.protocol.PermissionNode;
import com.googlecode.common.protocol.admin.AdminRequests;
import com.googlecode.common.protocol.admin.AppConfReqDTO;
import com.googlecode.common.protocol.admin.AppConfRespDTO;
import com.googlecode.common.protocol.admin.AppConfResponse;
import com.googlecode.common.protocol.admin.AppSystemDTO;
import com.googlecode.common.protocol.perm.PermissionDTO;
import com.googlecode.common.protocol.perm.PermissionNodeDTO;
import com.googlecode.common.service.AdminSettingsService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.JsonRequestService;
import com.googlecode.common.service.PermissionService;
import com.googlecode.common.service.SettingsService;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.util.Bits;
import com.googlecode.common.util.CollectionsUtil;
import com.googlecode.common.util.NumUtils;
import com.googlecode.common.util.SafeDigest;


/**
 * Contains common remote admin implementation methods.
 */
public abstract class AbstractRemoteAdminService implements PermissionService, 
        SettingsService, AdminSettingsService {

    protected final Logger          log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonRequestService      requestClient;
    
    private final ExecutorService   readerExecutor = 
        Executors.newCachedThreadPool();
    
    private PermissionNode          permissRoot;
    private boolean                 loadSystemInfo;
    
    private volatile URI            adminUrl;
    private volatile RequestParams  requestParams = new RequestParams();
    private volatile AdminData      adminData;
    
    
    protected AbstractRemoteAdminService(PermissionNode permissRoot) {
        this.permissRoot = permissRoot;
    }

    protected void setLoadSystemInfo(boolean loadSystemInfo) {
        this.loadSystemInfo = loadSystemInfo;
    }
    
    protected void setPermissRoot(PermissionNode permissRoot) {
        this.permissRoot = permissRoot;
    }
    
    protected void setAdminUrl(URI adminUrl) {
        this.adminUrl = adminUrl;
        this.requestParams = new RequestParams(adminUrl);
    }
    
    protected void initInternal() {
        // start reading remote settings asynchronously
        readerExecutor.execute(new AbstractSettingsReader(this) {
            @Override
            protected void settingsReady() {
                AbstractRemoteAdminService.this.settingsReady();
            }
        });
    }
    
    protected void destroyInternal() {
        try {
            readerExecutor.shutdownNow();
            readerExecutor.awaitTermination(20, TimeUnit.SECONDS);
        
        } catch (InterruptedException x) {
            throw new RuntimeException(x);
        }
    }
    
    /**
     * This method is called when remote settings is ready.
     */
    protected abstract void settingsReady();
    
    private boolean isSuperUserRole(List<Integer> roles) {
        if (roles.isEmpty()) {
            return false;
        }
        
        // first bit is always SUPERUSER role
        return Bits.any(NumUtils.ensureNotNull(roles.get(0)), 1);
    }
    
    @Override
    public URI getAdminServerUrl() {
        return adminUrl;
    }
    
    @Override
    public boolean authSystem(String name, String pass) {
        AdminData adminData = this.adminData;
        if (adminData == null) {
            return false;
        }
        
        return SafeDigest.digest(pass).equals(
                getAppSystem(adminData, name).getPassHash());
    }
    
    @Override
    public String getSystemTitle(String name) {
        AdminData adminData = this.adminData;
        if (adminData == null) {
            return name;
        }
        
        return getAppSystem(adminData, name).getTitle();
    }
    
    private static AppSystemDTO getAppSystem(AdminData adminData, String name) {
        AppSystemDTO sysDto = adminData.systems.get(name);
        if (sysDto == null) {
            throw new OperationFailedException(
                    CommonResponses.ENTITY_NOT_FOUND,
                    "System (" + name + ") not found");
        }
        
        return sysDto;
    }
    
    @Override
    public boolean hasRolePermission(List<Integer> roles, Permission p) {
        if (isSuperUserRole(roles)) {
            // super user has all permissions
            return true;
        }
        
        AdminData adminData = this.adminData;
        if (adminData == null) {
            return false;
        }
        
        int[] permRoles = adminData.permissions.get(p);
        if (permRoles == null) {
            throw new OperationFailedException(
                    CommonResponses.ENTITY_NOT_FOUND,
                    "Permission (" + p.getName() + ") not found");
        }
        
        final int count = Math.min(permRoles.length, roles.size());
        for (int i = 0; i < count; i++) {
            if (Bits.any(permRoles[i], NumUtils.ensureNotNull(roles.get(i)))) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isInitialized() {
        return (adminData != null);
    }
    
    @Override
    public void reloadSettings() throws IOException {
        AppConfRespDTO appDto;
        if (isInitialized()) {
            appDto = readSettings(null);
        } else {
            appDto = readSettings(permissRoot);
        }
        
        Map<Permission, int[]> permissions;
        if (permissRoot != null) {
            permissions = loadPermissions(permissRoot, 
                    appDto.getPermissions());
        } else {
            permissions = Collections.emptyMap();
        }
        
        Map<String, AppSystemDTO> systems;
        if (loadSystemInfo) {
            systems = loadSystems(appDto.safeGetSystems());
        } else {
            systems = Collections.emptyMap();
        }
        
        adminData = new AdminData(permissions, systems);
    }
    
    protected AppConfRespDTO readSettings(PermissionNode permissRoot) 
            throws IOException {
        
        PermissionNodeDTO permissDto = null;
        if (permissRoot != null) {
            permissDto = convertToPermissionNodeDTO(
                    new PermissionNodeDTO(), permissRoot);
        }
        
        AppConfReqDTO reqDto = new AppConfReqDTO(permissDto);
        reqDto.setLoadSystemInfo(loadSystemInfo);
        
        AppConfResponse resp = requestClient.create(requestParams, 
                AdminRequests.APP_READ_SETTINGS, reqDto, 
                AppConfResponse.class);
        
        if (resp != null) {
            if (resp.getStatus() == BaseResponse.OK_STATUS) {
                log.info("Settings read successfully");
                
                AppConfRespDTO settDto = resp.getData();
                if (settDto != null) {
                    return settDto;
                }
                
                log.error("Error while reading settings: No response data");
            }
            
            log.error("Error while reading settings: " 
                    + "\nmsg: " + resp.getMessage() 
                    + "\nerror: " + resp.getError());
        }
        
        throw new RuntimeException("Cannot read settings");
    }

    protected Map<String, AppSystemDTO> loadSystems(
            List<AppSystemDTO> systemsDto) {

        Map<String, AppSystemDTO> systems = new HashMap<String, AppSystemDTO>();
        for (AppSystemDTO dto : systemsDto) {
            systems.put(dto.getName(), dto);
        }
        
        log.info("Loaded " + systems.size() + " systems");
        return systems;
    }
    
    protected Map<Permission, int[]> loadPermissions(
            PermissionNode permissNode, PermissionNodeDTO permissDto) {

        Map<Permission, int[]> permissions = new HashMap<Permission, int[]>();
        
        List<PermissionNodeDTO> nodes = permissDto.safeGetNodes();
        for (PermissionNode n : permissNode.getNodes()) {
            loadPermissionNode("", n, nodes, permissions);
        }
        
        log.info("Loaded permissions");
        return permissions;
    }
    
    private static void loadPermissionNode(String absolutePath, 
            PermissionNode node, List<PermissionNodeDTO> permList, 
            Map<Permission, int[]> permMap) {

        String name = node.getName();
        absolutePath += "/" + name;
        
        PermissionNodeDTO dto = null;
        for (PermissionNodeDTO n : permList) {
            if (n != null && name.equals(n.getName())) {
                dto = n;
                break;
            }
        }
        
        if (dto == null) {
            throw new IllegalStateException("Permission node not found: " 
                    + absolutePath);
        }
        
        List<PermissionNodeDTO> nodes = dto.safeGetNodes();
        for (PermissionNode n : node.getNodes()) {
            loadPermissionNode(absolutePath, n, nodes, permMap);
        }
        
        List<PermissionDTO> permissions = dto.safeGetPermissions();
        for (Permission p : node.getPermissions()) {
            loadPermission(absolutePath, p, permissions, permMap);
        }
    }
    
    private static void loadPermission(String absolutePath, Permission perm, 
            List<PermissionDTO> permissions, Map<Permission, int[]> permMap) {
        
        String name = perm.getName();
        absolutePath += "/" + name;
        
        PermissionDTO dto = null;
        for (PermissionDTO p : permissions) {
            if (p != null && name.equals(p.getName())) {
                dto = p;
                break;
            }
        }
        
        if (dto == null) {
            throw new IllegalStateException("Permission not found: " 
                    + absolutePath);
        }
        
        permMap.put(perm, CollectionsUtil.toIntArray(dto.safeGetRoles()));
    }
    
    private PermissionNodeDTO convertToPermissionNodeDTO(PermissionNodeDTO dto, 
            PermissionNode node) {
        
        dto.setName(node.getName());
        dto.setTitle(node.getTitle());
        
        List<PermissionNodeDTO> nodes = null;
        for (PermissionNode n : node.getNodes()) {
            nodes = CollectionsUtil.addToList(nodes, 
                    convertToPermissionNodeDTO(new PermissionNodeDTO(), n));
        }
        
        List<PermissionDTO> permiss = null;
        for (Permission p : node.getPermissions()) {
            permiss = CollectionsUtil.addToList(permiss, 
                    convertToPermissionDTO(new PermissionDTO(), p));
        }
        
        dto.setNodes(nodes);
        dto.setPermissions(permiss);
        return dto;
    }
    
    private PermissionDTO convertToPermissionDTO(PermissionDTO dto, 
            Permission perm) {
        
        dto.setName(perm.getName());
        dto.setTitle(perm.getTitle());
        return dto;
    }
    
    
    private static final class AdminData {
        
        // permission to roles
        private final Map<Permission, int[]>     permissions;
        private final Map<String, AppSystemDTO>  systems;
        
        
        public AdminData(Map<Permission, int[]> permissions,
                Map<String, AppSystemDTO> systems) {
            
            this.permissions = permissions;
            this.systems = systems;
        }
    }
    
}
