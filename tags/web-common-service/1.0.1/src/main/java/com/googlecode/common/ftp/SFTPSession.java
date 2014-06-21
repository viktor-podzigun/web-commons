
package com.googlecode.common.ftp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import com.googlecode.common.util.StringHelpers;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;


/**
 * Represents single SFTP session, used to perform bulk operations.
 */
public final class SFTPSession implements Closeable {

    private final SFTPClient    client;
    private Session             session;
    private ChannelSftp         channel;
    
    
    /**
     * Creates a new session and performs connect.
     * 
     * @param client    SFTP client
     * 
     * @throws IOException  if I/O error occurs
     */
    SFTPSession(SFTPClient client) throws IOException {
        this.client = client;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(client.user, client.host);
            session.setPassword(client.password);
            
            if (client.port > 0) {
                session.setPort(client.port);
            }
        
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        
            Channel c = session.openChannel(client.protocol);
            c.connect();
            channel = (ChannelSftp)c;
        
        } catch (JSchException x) {
            throw new IOException(x);
        }
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.exit();
            channel = null;
        }
        
        if (session != null && session.isConnected()) {
            session.disconnect();
            session = null;
        }
    }

    /**
     * Uploads source input stream into destination path through SFTP channel.
     * 
     * @param is        source input stream
     * @param dstPath   destination path with file name
     * 
     * @throws IOException if I/O error occurs
     */
    public void upload(InputStream is, String dstPath) throws IOException {
        if (StringHelpers.isNullOrEmpty(dstPath)) {
            throw new IllegalArgumentException("dstPath is null or empty");
        }
        
        try {
            channel.put(is, client.getFullPath(dstPath));
        
        } catch (SftpException x) {
            throw new IOException(x);
        }
    }

    /**
     * Returns list of files and directories by the given pattern.
     *  
     * <p>For example, if you want a list of all .zip files in "myDir" 
     * directory pass "/myDir/*.zip"
     * 
     * @param pattern   search pattern, can contain *, ? control characters
     * @return list of files and directories by the given pattern
     * 
     * @throws IOException if I/O error occurs
     */
    public List<String> list(String pattern) throws IOException {
        if (pattern == null) {
            pattern = "";
        }
        
        try {
            @SuppressWarnings("unchecked")
            Vector<LsEntry> list = channel.ls(client.getFullPath(pattern));

            if (list == null || list.isEmpty()) {
                return Collections.emptyList();
            }
            
            List<String> result = new ArrayList<String>(list.size());
            for (LsEntry lsEntry : list) {
                String fileName = lsEntry.getFilename();
                    result.add(fileName);            
            }
            
            return result;
        
        } catch (SftpException x) {
            throw new IOException(x);
        }
    }
    
    /**
     * Downloads file from the given source path to the given output stream.
     * 
     * @param src   source path
     * @param os    output stream
     * 
     * @throws IOException if I/O error occurs
     */
    public void download(String src, OutputStream os) throws IOException {
        if (StringHelpers.isNullOrEmpty(src)) {
            throw new IllegalArgumentException("src is null or empty");
        }
        
        try {
            channel.get(client.getFullPath(src), os);

        } catch (SftpException x) {
            throw new IOException(x);
        }
    }
    
    /**
     * Deletes the given file or directory.
     * 
     * <p>If file or directory does not exist, then <code>IOException</code> 
     * will be thrown. 
     * 
     * @param path  full file name
     * 
     * @throws IOException if I/O error occurs
     */
    public void delete(String path) throws IOException {
        if (StringHelpers.isNullOrEmpty(path)) {
            throw new IllegalArgumentException("path is null or empty");
        }
        
        try {
            channel.rm(client.getFullPath(path));

        } catch (SftpException x) {
            throw new IOException(x);
        }
    }
     
    /**
     * Renames file or directory or moves form old path to new path.
     * 
     * <p>Throws <code>IOException</code> if old path or new path does not 
     * exist.
     * 
     * @param oldpath   old path
     * @param newpath   new path
     * 
     * @throws IOException if I/O error occurs
     */
    public void rename(String oldpath, String newpath) throws IOException {
        if (StringHelpers.isNullOrEmpty(oldpath)) {
            throw new IllegalArgumentException("Old path is null or empty");
        }
        if (StringHelpers.isNullOrEmpty(newpath)) {
            throw new IllegalArgumentException("New path is null or empty");
        }
        
        try {
            channel.rename(client.getFullPath(oldpath), 
                    client.getFullPath(newpath));

        } catch (SftpException x) {
            throw new IOException(x);
        }
    }
    
}
