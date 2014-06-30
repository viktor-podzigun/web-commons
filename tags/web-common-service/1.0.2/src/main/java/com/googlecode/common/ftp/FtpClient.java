
package com.googlecode.common.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.net.ftp.FTPClient;
import com.googlecode.common.util.UriHelpers;


/**
 * This class represent's client that can upload files via FTP protocol.
 */
public class FtpClient {

    private static final String SLASH = "/";

    private final String user;
    private final String password;

    private final String host;
    private final int port;
    
    private final String path;

    public FtpClient(String user, String password, 
            String host, int port, String path) {
        
        if (port > Short.MAX_VALUE) {
            throw new IllegalArgumentException("port: " + port);
        }
        
        this.user     = user;
        this.password = password;
        this.host     = host;
        this.port     = port;
        this.path     = path;
    }

    public static FtpClient create(URI uri) {
        String user = null;
        String password = null;
        
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            String[] info = UriHelpers.splitUserInfo(userInfo);
            user = info[0];
            password = info[1];
        }
        
        return new FtpClient(user, password, 
                uri.getHost(), uri.getPort(), uri.getPath());
    }

    public static FtpClient create(String url) {
        try {
            return create(new URI(url));

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("url: " + url);
        }
    }

    public void upload(InputStream is, String dst) throws IOException {
        if (dst == null || dst.isEmpty()) {
            throw new IllegalArgumentException("Destination path is null or empty");
        }
        
        FTPClient client = new FTPClient();
        if (port > 0) {
            client.connect(host, port);
        } else {
            client.connect(host);
        }

        String fullPath;
        if (path != null && !path.isEmpty()) {
            fullPath = path + SLASH + dst;
        } else {
            fullPath = dst;
        }

        client.login(user, password);
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        boolean isSend = client.storeFile(fullPath, is);
        client.disconnect();
        
        if (!isSend) {
            throw new IOException("File wasn't upload");
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

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        String portStr = (port > 0 ? ":" + port : "");
        String pathStr = (path != null ? path : "");
        
        return "ftp://" + user + "@" + host + portStr + pathStr;
    }

}
