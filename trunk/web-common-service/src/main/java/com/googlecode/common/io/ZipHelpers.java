
package com.googlecode.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Helper class for working with ZIP archives.
 */
public final class ZipHelpers {

    private ZipHelpers() {
    }
    
    /**
     * Creates a new ZIP archive.
     * 
     * @param dstPath   path and file name with extension.
     * @param srcPath   source file or dir to be added recursively.
     * 
     * @throws IOException if any I/O error occurred.
     */
    public static void createZip(String dstPath, String srcPath) 
            throws IOException {
        
        ZipOutputStream out = null;

        try {
            File srcFile  = new File(srcPath);
            if (!srcFile.exists()) {
                throw new IOException("Source file or dir not found: \"" 
                        + srcFile.getPath() + "\"");
            }
            
            out = new ZipOutputStream(new FileOutputStream(dstPath));
            
            if (srcFile.isDirectory()) {
                addFilesR(out, srcFile, "");
            } else {
                addFile(out, srcFile, "");
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    
    private static void addFilesR(ZipOutputStream out, File dir, String parent) 
            throws IOException {
        
        //System.out.println("Writing dir: " + dir);
        parent = addDir(out, parent, dir.getName());
        
        File ff[] = dir.listFiles();
        for (File file : ff) {
            if (file.getName().startsWith(".")) {
                continue;
            }

            if (file.isDirectory()) {
                addFilesR(out, file, parent);
            } else {
                addFile(out, file, parent);
            }
        }
    }

    private static void addFile(ZipOutputStream out, File file, String parent) 
            throws IOException {
        
        //System.out.println("Writing file: " + file);
        InputStream in = null;
        
        try {
            in = new FileInputStream(file);
            addFile(out, in, parent, file.getName());
            
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static String addDir(ZipOutputStream out, String parent, String name)
            throws IOException {

        if (parent == null) {
            parent = "";
        }
        
        parent = parent + name + "/";
        
        out.putNextEntry(new ZipEntry(parent));
        out.closeEntry();
        return parent;
    }

    public static void addFile(ZipOutputStream out, InputStream in, 
            String parent, String fileName) throws IOException {

        out.putNextEntry(new ZipEntry(parent + fileName));
        
        try {
            FileHelpers.copyBytes(in, out);
        
        } finally {
            out.closeEntry();
        }
    }

    /**
     * Extracts files recursively from the given ZIP input stream.
     * 
     * @param zin       ZIP input stream
     * @param dstPath   destination directory path
     * @param filter    filter for ZIP entries or <code>null</code> to 
     *                  extract all entries
     * 
     * @throws IOException if any I/O error occurred
     */
    public static void extractZip(ZipInputStream zin, String dstPath, 
            ZipEntryFilter filter) throws IOException {
        
        try {
            File dir = new File(dstPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            extractFilesR(zin, dir, "", filter);
        
        } finally {
            if (zin != null) {
                zin.close();
            }
        }
    }
    
    private static String getEntryName(ZipEntry entry, String parentPath) {
        String path = entry.getName();
        int nameEndIndex = path.length();
        if (path.endsWith("/")) {
            nameEndIndex--;
        }
        
        return path.substring(parentPath.length(), nameEndIndex);
    }
    
    private static ZipEntry extractFilesR(ZipInputStream zin, File dir, 
            String parentPath, ZipEntryFilter filter) throws IOException {
        
        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String path = entry.getName();
            if (!path.startsWith(parentPath)) {
                break;
            }
            
            if (filter != null && !filter.accept(entry)) {
                entry = zin.getNextEntry();
                continue;
            }
            
            File dstFile = new File(dir, getEntryName(entry, parentPath));
            if (entry.isDirectory()) {
                dstFile.mkdir();
                entry = extractFilesR(zin, dstFile, path, filter);
            } else {
                extractFile(zin, entry, dstFile);
                entry = zin.getNextEntry();
            }
        }
        
        return entry;
    }
    
    private static void extractFile(ZipInputStream zin, ZipEntry entry, 
            File dstFile) throws IOException {

        OutputStream out = null;
        try {
            out = new FileOutputStream(dstFile);
            FileHelpers.copyBytes(zin, out);
            
        } finally {
            if (out != null) {
                out.close();
            }
        }
    
        final long time = entry.getTime();
        if (time != -1L) {
            dstFile.setLastModified(time);
        }
    }

    static void testCreate() throws IOException {
        ZipOutputStream out = null;
        InputStream in = null;
        try {
            out = new ZipOutputStream(
                    new FileOutputStream("/home/viktor/work/scratch/key/0.zip"));
            
            in = new FileInputStream("/home/viktor/work/scratch/key/test.csv");
            
            String parent = addDir(out, null, "1");
            addFile(out, in, parent, "test.csv");
        
        } finally {
            if (out != null) {
                out.close();
            }
        
            if (in != null) {
                in.close();
            }
        }
    }
    
    static void testList() throws IOException {
        ZipInputStream in = null;
        try {
            in = new ZipInputStream(new FileInputStream("D:/1/saved.zip"));
            
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
            
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    
    static void testExtract() throws IOException {
        ZipInputStream in = null;
        try {
            in = new ZipInputStream(new FileInputStream("D:/1/saved.zip"));
            extractZip(in, "D:/1/saved", new ZipEntryFilter() {
                @Override
                public boolean accept(ZipEntry pathname) {
                    String name = pathname.getName();
                    return (name.startsWith("incoming") 
                            || name.startsWith("JList.java"));
                }
            });
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        //testCreate();
        //testList();
        testExtract();
    }

}
