
package com.googlecode.common.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import com.googlecode.common.util.UriHelpers;


/**
 * This class represent`s client that can upload files via SFTP protocol.
 */
public class SFTPClient {
	
	private static final String SLASH = "/";

	final String   protocol;
	
	final String   user;
	final String   password;

	final String   host;
	final int      port;
	
	final String   path;

	
	public SFTPClient(String protocol, String user, String password, 
	        String host, int port, String path) {
	    
	    if (port > Short.MAX_VALUE) {
	        throw new IllegalArgumentException("port: " + port);
	    }
	    
	    this.protocol = protocol.toLowerCase();
		this.user     = user;
		this.password = password;
		this.host     = host;
		this.port     = port;
		this.path     = path;
	}

    public static SFTPClient create(String url) {
        try {
            return create(new URI(url));

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("url: " + url);
        }
    }
    
    public static SFTPClient create(URI uri) {
		String user = null;
		String password = null;
		
		String userInfo = uri.getUserInfo();
		if (userInfo != null) {
			String[] info = UriHelpers.splitUserInfo(userInfo);
			user = info[0];
			password = info[1];
		}
		
		return new SFTPClient(uri.getScheme(), user, password, 
		        uri.getHost(), uri.getPort(), uri.getPath());
	}

//	public static void main(String[] args) throws IOException {
//		SFTPClient client = SFTPClient.create("sftp://gm:5r6t7yzx@192.168.2.100/home/gm/Bohdan/");
//		client.upload(new ByteArrayInputStream("test".getBytes()), "ftp_upload_test.txt");
//	}
    
    public SFTPSession openSession() throws IOException {
        return new SFTPSession(this);
    }
	
    String getFullPath(String dst) {
        String fullPath;
        if (path != null && !path.isEmpty()) {
            fullPath = (path.endsWith(SLASH) ? path + dst 
                    : path + SLASH + dst);
        } else {
            fullPath = dst;
        }
        
        return fullPath;
    }
    
    /**
     * Uploads source input stream into destination path through SFTP channel.
     * 
     * @param is    source input stream
     * @param dst   destination path with file name
     * @throws IOException if <code>SftpException</code> or 
     * <code>JSchException</code> occur.
     * 
     * @deprecated please, use {@link SFTPSession#upload(InputStream, String)}
     */
    @Deprecated
	public void upload(InputStream is, String dst) throws IOException {
        if (dst == null || dst.isEmpty()) {
            throw new IllegalArgumentException("dst path is null or empty");
        }
	    		
		SFTPSession session = null;
		try {
			session = new SFTPSession(this);
			session.upload(is, dst);

        } finally {
            if (session != null) {
                session.close();
            }
        }
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getServerAddress() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
	    String portStr = (port > 0 ? ":" + port : "");
	    String pathStr = (path != null ? path : "");
	    
	    return protocol + "://" + user + "@" + host + portStr + pathStr;
	}

}
