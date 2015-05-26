
package com.googlecode.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


/**
 * Helper class for reading version information.
 * 
 * <p>This class has no public constructor, use static factories.
 */
public final class ModuleInfo {

    private static final String TITLE   = Attributes.Name.IMPLEMENTATION_TITLE.toString();
    private static final String AUTHOR  = "Built-By";
    private static final String VENDOR  = Attributes.Name.IMPLEMENTATION_VENDOR.toString();
    private static final String VERSION = Attributes.Name.IMPLEMENTATION_VERSION.toString();
    private static final String BUILD   = "Implementation-Build";
    
    private final Map<String, String>   entries;
    

    private ModuleInfo(Map<?, ?> params) {
        Map<String, String> entries = new HashMap<String, String>();
        for (Map.Entry<?, ?> e : params.entrySet()) {
            entries.put(e.getKey().toString(), e.getValue().toString());
        }
        
        this.entries = entries;
    }
    
    public String get(String paramName) {
        return entries.get(paramName);
    }
    
    public String getTitle() {
        return get(TITLE);
    }

    public String getAuthor() {
        return get(AUTHOR);
    }
    
    public String getVendor() {
        return get(VENDOR);
    }
    
    public String getVersion() {
        return get(VERSION);
    }
    
    public String getBuild() {
        return get(BUILD);
    }
    
    @Override
    public String toString() {
        return entries.toString();
    }
    
    private static Manifest readManifest(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            return (is != null ? new Manifest(is) : null);
            
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
    public static ModuleInfo readFor(Class<?> clazz) {
        String clazzPath = clazz.getName().replace('.', '/') + ".class";
        ClassLoader cl = clazz.getClassLoader();
        URL clazzUrl = cl.getResource(clazzPath);
        String baseUrl = clazzUrl.toString();
        baseUrl = baseUrl.substring(0, baseUrl.length() - clazzPath.length());

        try {
            Enumeration<URL> resEnum = cl.getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                URL url = resEnum.nextElement();
                if (url.toString().startsWith(baseUrl)) {
                    return readFromManifest(readManifest(url));
                }
            }
            
            return null;
            
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    public static ModuleInfo[] readAll(ClassLoader cl) {
        return readAllByVendor(cl, null);
    }

    public static ModuleInfo[] readAllByVendor(ClassLoader cl, String vendor) {
        try {
            ArrayList<ModuleInfo> list = new ArrayList<ModuleInfo>();
            Enumeration<URL> resEnum = cl.getResources(JarFile.MANIFEST_NAME);
            
            while (resEnum.hasMoreElements()) {
                URL url = resEnum.nextElement();
                Manifest manifest = readManifest(url);
                if (manifest != null) {
                    final Attributes mainAttribs = manifest.getMainAttributes();
                    final String title = mainAttribs.getValue(TITLE);
                    if ((vendor == null
                                || vendor.isEmpty()
                                || vendor.equals(mainAttribs.getValue(VENDOR)))
                            && title != null && !title.startsWith("Apache Tomcat")) {

                        list.add(readFromManifest(manifest));
                    }
                }
            }
            
            Collections.sort(list, new Comparator<ModuleInfo>() {
                @Override
                public int compare(ModuleInfo o1, ModuleInfo o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
            
            return list.toArray(new ModuleInfo[list.size()]);
            
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
    
    public static ModuleInfo readFromProperties(Properties props) {
        return new ModuleInfo(props);
    }
    
    public static ModuleInfo readFromManifest(Manifest manifest) {
        return new ModuleInfo(manifest.getMainAttributes());
    }
    
}
