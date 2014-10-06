
package com.googlecode.common.client.ui.panel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.common.client.ui.AbstractButton;
import com.googlecode.common.client.ui.ActionProvider;
import com.googlecode.common.client.ui.ButtonType;
import com.googlecode.common.client.ui.ButtonsPanel;
import com.googlecode.common.client.ui.CommonImages;


/**
 * Contains utility methods for showing detailed error messages.
 */
public final class ErrorPanel {
    
    private static final String T_WIDTH     = "500px";
    private static final String T_HEIGHT    = "80px";
    private static final String TD_HEIGHT   = "400px";
    
    private static final String BTN_MORE    = "Details >>";
    private static final String BTN_LESS    = "Details <<";
    
    private final Modal             modal;
    
    private final String            text;
    private final String            fullText;
    private final HTML              textHtml;
    private final AbstractButton    btnDetails;
    
    private boolean                 detailed;

    
    private ErrorPanel(String text, String error) {
        if (text == null) {
            throw new NullPointerException("text");
        }

        if (error != null) {
            fullText = MessageBox.makeHtmlText(text + "\n\n" + error);
        } else {
            fullText = null;
        }
        text = MessageBox.makeHtmlText(text);
        
        this.text  = text;
        this.modal = new Modal(null, true);
        
        ButtonsPanel bp = modal.getButtonsPanel();
        final List<String> cmdList = Arrays.asList("details", "close");
        if (error != null) {
            btnDetails = bp.addButton(new ButtonType(BTN_MORE, cmdList.get(0)));
        } else {
            btnDetails = null;
        }
        
        final AbstractButton closeButton = bp.addButton(new ButtonType(
                "Close", cmdList.get(1)));
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                closeButton.setFocus(true);
            }
        });
        
        bp.setActionProvider(new ActionProvider() {
            @Override
            public Collection<String> getActionCommands() {
                return cmdList;
            }
            
            @Override
            public void actionPerformed(String command) {
                if (command.equals("close")) {
                    modal.hide();
                } else if (command.equals("details")) {
                    onDetailsClick();
                }
            }
        });
        
        FlowPanel panel = new FlowPanel();
        panel.add(new Image(CommonImages.INSTANCE.dialogError()));
        
        textHtml = new HTML(text, false);
        textHtml.setSize(T_WIDTH, T_HEIGHT);
        panel.add(textHtml);
        
        modal.setBody(panel);
    }

    private void onDetailsClick() {
        detailed = !detailed;
        if (detailed) {
            textHtml.setHTML(fullText);
            btnDetails.setText(BTN_LESS);
            textHtml.setHeight(TD_HEIGHT);
        } else {
            textHtml.setHTML(text);
            btnDetails.setText(BTN_MORE);
            textHtml.setHeight(T_HEIGHT);
        }
    }
    
    private static String printStackTrace(Throwable x) {
        StringBuilder sb = new StringBuilder(x.toString());
        Object[] trace = x.getStackTrace();
        for (Object t : trace) {
            sb.append("\n\tat&nbsp").append(t);
        }
        
        Throwable cause = x.getCause();
        if (cause != null) {
            printStackTraceAsCause(sb, cause, trace);
        }
        
        return sb.toString();
    }
    
    /**
     * Print stack trace as a cause for the specified stack trace.
     */
    private static void printStackTraceAsCause(StringBuilder s, 
            Throwable cause, Object[] causedTrace) {
        
        // Compute number of frames in common between this and caused
        Object[] trace = cause.getStackTrace();
        int m = trace.length-1, n = causedTrace.length-1;
        while (m >= 0 && n >=0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        
        int framesInCommon = trace.length - 1 - m;

        s.append("\nCaused by: " + cause);
        for (int i = 0; i <= m; i++) {
            s.append("\n\tat&nbsp").append(trace[i]);
        }
        if (framesInCommon != 0) {
            s.append("\n\t...&nbsp").append(framesInCommon).append("&nbspmore");
        }

        // Recurse if we have a cause
        Throwable ourCause = cause.getCause();
        if (ourCause != null) {
            printStackTraceAsCause(s, ourCause, trace);
        }
    }
    
    private static ErrorPanel createError(String text, Throwable x) {
        if (x == null) {
            throw new NullPointerException("x");
        }
        if (text == null) {
            text = x.toString();
        }
        
        String stackTrace;
        if (x instanceof UmbrellaException) {
            String trace = "";
            for (Throwable cause : ((UmbrellaException)x).getCauses()) {
                if (!trace.isEmpty()) {
                    trace += "\n\n";
                }
                trace += printStackTrace(cause);
            }
            stackTrace = trace;
        } else {
            stackTrace = printStackTrace(x);
        }

        return create(text, stackTrace);
    }
    
    private static ErrorPanel create(String text, String error) {
        return new ErrorPanel(text, error);
    }

    public static void showDetailed(String text, String errorText) {
        create(text, errorText).modal.show();
    }
    
    public static void showError(String text, Throwable x) {
        createError(text, x).modal.show();
    }
    
    public static void show(String text) {
        create(text, null).modal.show();
    }

}
