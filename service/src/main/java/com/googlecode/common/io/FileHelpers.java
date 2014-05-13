
package com.googlecode.common.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


/**
 * Contains useful methods for working with files.
 */
public final class FileHelpers {

    
    // This class contains static members only. No public constructor.
    private FileHelpers() {
    }
    
    public static void copyChars(Reader in, Writer out) 
            throws IOException {
        
        char[] buf = new char[2048];
        int len;
        
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }

    public static void copyCharsAndClose(Reader in, Writer out)
            throws IOException {

        try {
            copyChars(in, out);

        } finally {
            in.close();
            out.close();
        }
    }

    public static void copyBytes(InputStream in, OutputStream out) 
            throws IOException {
        
        byte[] buf = new byte[2048];
        int len;
        
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }

    public static void copyBytesAndClose(InputStream in, OutputStream out) 
            throws IOException {
        
        try {
            copyBytes(in, out);
            
        } finally {
            in.close();
            out.close();
        }
    }

    public static String readResourceAsString(ClassLoader cl, String path) 
            throws IOException {
        
        InputStream is = cl.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("Resource not found: " + path);
        }
        
        return readCharsToStr(new InputStreamReader(is, "UTF-8"));
    }
    
    public static String readCharsToStr(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        char[] buf = new char[2048];
        int len;
        
        while ((len = reader.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        
        return sb.toString();
    }
    
    public static byte[] readFileToBuf(String filePath) throws IOException {
        return readBytesToBuf(new FileInputStream(filePath));
    }
    
    public static byte[] readBytesToBuf(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyBytesAndClose(in, out);
        return out.toByteArray();
    }
    
    public static BufOutputStream readBytesToBuf(BufOutputStream out, 
            InputStream in) throws IOException {
        
        copyBytes(in, out);
        return out;
    }
    
    public static String getFileExtension(String fileName) {
        final int index = fileName.lastIndexOf('.');
        if (index >= 0) {
            return fileName.substring(index + 1);
        }
        
        return null;
    }
    
    public static String getFileName(String fileName) {
        final int index = fileName.lastIndexOf('.');
        if (index >= 0) {
            return fileName.substring(0, index);
        }
        
        return fileName;
    }
    
    /**
     * Deletes all files recursively from the given directory.
     * 
     * @param dir       directory
     * @param delDir    indicates whether to delete the directory itself
     */
    public static void deleteAll(File dir, boolean delDir) {
        if (dir == null) {
            throw new NullPointerException("dir");
        }
        
        deleteFiles(dir, null);
        if (delDir) {
            dir.delete();
        }
    }
    
    /**
     * Deletes files recursively from the given directory.
     * 
     * @param dir       directory
     * @param filter    filter for files to delete, can be <code>null</code> 
     *                  to delete all files
     */
    public static void deleteFiles(File dir, FileFilter filter) {
        if (dir == null) {
            throw new NullPointerException("dir");
        }
        
        File[] files = dir.listFiles(filter);
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFiles(f, filter);
                f.delete();
            } else {
                f.delete();
            }
        }
    }
    
}
