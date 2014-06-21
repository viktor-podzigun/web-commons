
package com.googlecode.common.ftp;

import java.io.FileOutputStream;
import java.util.List;
import com.googlecode.common.ftp.SFTPClient;
import com.googlecode.common.ftp.SFTPSession;


/**
 * 
 */
public class TestSFTPClient {
    
    private static final String DST = "target/demo/";
    
    private final SFTPClient client;

    
    public TestSFTPClient() {
        client = SFTPClient.create(
                "sftp://gm:5r6t7yzx@192.168.2.100/home/gm/Bohdan/SFTP");
    }
    
    void testList() throws Exception {
        SFTPSession session = client.openSession();
        try {
            List<String> result = session.list("testList/*.zip");
            System.out.println(result);
            result = session.list("");
            System.out.println(result);
        
        } finally {
            session.close();
        }
    }
    
    void testDownload() throws Exception {
        SFTPSession session = client.openSession();
        try {
            FileOutputStream fos = new FileOutputStream(DST + "i18n.zip");
            session.download("testDownload/i18n.zip", fos);
            session.download("testDownload/localization.zip", fos);
        
        } finally {
            session.close();
        }
    }
    
    void testRename() throws Exception {
        String oldpath = "testRename/old/";
        String newpath = "testRename/new/";
        
        SFTPSession session = client.openSession();
        try {
            session.rename(oldpath + "localization.zip", oldpath 
                    + "localization_new.zip");
            session.rename(oldpath + "i18n.zip", newpath + "i1:8n.zip");
        
        } finally {
            session.close();
        }
    }
    
    void testDelete() throws Exception {
        String path = "testDelete/";
        
        SFTPSession session = client.openSession();
        try {
            session.delete(path + "i18n.zip");
            session.delete(path + "localization.zip");
        
        } finally {
            session.close();
        }
    }
    
    void mainTest() throws Exception {
        String src = "mainTest/";
        
        SFTPSession session = client.openSession();
        try {
            System.out.println("\nList files...");
            
            List<String> filesList = session.list(src + "*.zip");
            if (filesList.isEmpty()) {
                System.out.println("No matched files are found");
                return;
            }
            
            String fileName = filesList.get(0);
            System.out.println("Found file: " + fileName);
            
            String newFileName = fileName + "_processing";
            System.out.println("Rename: " + newFileName);
            session.rename(src + fileName, src + newFileName);
            
            System.out.println("Downloading...");
            FileOutputStream fos = new FileOutputStream(DST + newFileName);
            session.download(src + newFileName, fos);
            
            System.out.println("Deleting...");
            session.delete(src + newFileName);
            
        } finally {
            session.close();
        }
    }

    private void runTests() throws Exception {
//        testList();
//        testDownload();
//        testRename();
//        testDelete();
        
        char ch = '0';
        while (ch != 'q') {
            mainTest();
            
            System.out.println("input symbol to get file or q to exit");
            
            ch = (char)System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        new TestSFTPClient().runTests();
    }

}
