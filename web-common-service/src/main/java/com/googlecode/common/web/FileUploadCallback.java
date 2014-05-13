
package com.googlecode.common.web;

import java.io.IOException;
import org.apache.commons.fileupload.FileItem;


/**
 * Interface to process file data.
 */
public interface FileUploadCallback {

    public void processFileItem(FileItem fileItem) throws IOException;
    
}
