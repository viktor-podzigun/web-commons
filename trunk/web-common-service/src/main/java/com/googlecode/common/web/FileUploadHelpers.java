
package com.googlecode.common.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.googlecode.common.io.EncodingHelpers;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ex.OperationFailedException;


/**
 * Contains common file upload helper methods.
 */
public final class FileUploadHelpers {

    
    private FileUploadHelpers() {
    }

    /**
     * Utility method that determines whether the request contains multipart
     * content.
     *
     * @param request   the servlet request to be evaluated, must be non-null
     * @return          <code>true</code> if the request is multipart;
     *                  <code>false</code> otherwise
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        String method = request.getMethod().toLowerCase();
        if (!("post".equals(method) || "put".equals(method))) {
            return false;
        }
        
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        
        if (contentType.toLowerCase().startsWith(ServletFileUpload.MULTIPART)) {
            return true;
        }
        
        return false;
    }

    public static void process(HttpServletRequest req, FileItemFactory factory, 
            FileUploadCallback callback) throws IOException {
        
        if (!isMultipartContent(req)) {
            throw new OperationFailedException(CommonResponses.INVALID_REQUEST, 
                    "Request is not multipart");
        }
        
        // create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        // parse the request to get file items
        List<FileItem> fileItems;
        try {
            @SuppressWarnings("unchecked")
            List<FileItem> items = upload.parseRequest(req);
            fileItems = items;
        
        } catch (FileUploadException x) {
            throw new IOException(x);
        }
        
        if (fileItems == null || fileItems.isEmpty()) {
            throw new OperationFailedException(
                    CommonResponses.INVALID_REQUEST, "No file items");
        }
        
        for (FileItem fi : fileItems) {
            try {
                callback.processFileItem(fi);
            } finally {
                fi.delete();
            }
        }
    }
    
    /**
     * Returns string value from the provided form field item.
     * 
     * @param item  form field
     * @return      string value
     */
    public static String getStringField(FileItem item) {
        if (!item.isFormField()) {
            throw new IllegalArgumentException("item is not form field");
        }
        
        String contentType = item.getContentType();
        if (contentType != null 
                && contentType.toLowerCase().contains("utf-8")) {
            
            return new String(EncodingHelpers.decodeBase64(item.get()), 
                    EncodingHelpers.UTF8);
        }
        
        return new String(item.get(), EncodingHelpers.US_ASCII);
    }

}
