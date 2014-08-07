
package com.googlecode.common.client.ui.panel;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.common.client.ui.ButtonImages;
import com.googlecode.common.protocol.BaseResponse;


/**
 * Dialog for importing files.
 */
public final class ImportDialog<T extends BaseResponse> 
        extends BaseOkCancelDialog {
    
    private static final Binder binder = GWT.create(Binder.class);
    @SuppressWarnings("rawtypes")
    interface Binder extends UiBinder<Widget, ImportDialog> {
    }

    @UiField FormPanel      importForm;
    @UiField FileUpload     importWidget;
    
    private Command         successCommand;
    private T               response;
    
    public ImportDialog(String title, String url, JsonEncoderDecoder<T> coder,
            final String... fileExtensions) {
        
        super(title, "Import");
        
        setOkIcon(ButtonImages.INSTANCE.dbSave(), 
                ButtonImages.INSTANCE.dbSaveDisabled());
        
        setContent(binder.createAndBindUi(this));
        
        // because we added a FileUpload widget to form, we need to set 
        // the form to use POST method, and multipart MIME encoding
        importForm.setAction(url);
        importForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        importForm.setMethod(FormPanel.METHOD_POST);
        
        ImportTask task = new ImportTask(coder);
        importForm.addSubmitHandler(task);
        importForm.addSubmitCompleteHandler(task);
        
        setOkCommand(new Command() {
            
            @Override
            public void execute() {
                String fileName = importWidget.getFilename();
                boolean error = true;
                
                if (fileName != null) {
                    for (String fileExtension : fileExtensions) {
                        if (fileName.toLowerCase()
                                .endsWith(fileExtension.toLowerCase())) {
                            
                            error = false;
                            break;
                        }
                    }
                }
                
                if (error == true) {
                    StringBuilder sb = new StringBuilder();
                    for (String fileExtension : fileExtensions) {
                        if (sb.length() == 0) {
                            sb.append("'" + fileExtension + "'");
                        } else {
                            sb.append(", '" + fileExtension + "'");
                        }
                    }
                    
                    ErrorPanel.show("Only " + sb.toString() 
                            + " file name extensions are allowed");
                    return;
                }
                
                importForm.submit();
            }
        });
    }
    
    public T getResponse() {
        return response;
    }

    public void setSuccessCommand(Command successCommand) {
        this.successCommand = successCommand;
    }


    private class ImportTask extends FormSubmitTask<T> {
        
        public ImportTask(JsonEncoderDecoder<T> coder) {
            super("Importing file...", coder);
        }
        
        @Override
        protected void processResponse(T response) {
            if (successCommand != null) {
                ImportDialog.this.response = response;
                successCommand.execute();
            } else {
                MessageBox.showMessage("Import completed");
                hide();
            }
        }
    }
}
