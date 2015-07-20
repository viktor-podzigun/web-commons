package com.googlecode.common.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.NestedServletException;
import com.googlecode.common.io.JsonSerializer;
import com.googlecode.common.protocol.BaseResponse;
import com.googlecode.common.protocol.login.LoginRedirectResponse;
import com.googlecode.common.service.AdminService;
import com.googlecode.common.service.CommonResponses;
import com.googlecode.common.service.ResponseMessage;
import com.googlecode.common.service.ServerManager;
import com.googlecode.common.service.ex.OperationFailedException;
import com.googlecode.common.util.StringHelpers;

/**
 * Servlet Filter used to catch errors from servlets and return 
 * appropriate valid error responses.
 */
public final class CommonFilter implements Filter {

    private final Logger    log = LoggerFactory.getLogger(getClass());
	
    private JsonSerializer  jsonSerializer;
    private ServerManager   serverManager;
    private AdminService    adminService;
    
	
    public CommonFilter() {
    }

    @Override
	public void destroy() {
	}

    @Override
	public void init(FilterConfig config) throws ServletException {
        try {
            String appDomain = StringHelpers.trim(config.getInitParameter(
                    "appDomain"));
            if (!StringHelpers.isNullOrEmpty(appDomain)) {
                ServletHelpers.setAppDomain(appDomain);
                log.info("appDomain: " + appDomain);
            }
            
    	    ServletContext context = config.getServletContext();
            WebApplicationContext applicationContext = 
                WebApplicationContextUtils.getWebApplicationContext(context);
    	
            jsonSerializer = (JsonSerializer)applicationContext.getBean(
                    "jsonSerializer");
            
            adminService = applicationContext.getBean(AdminService.class);
        
            try {
                serverManager = (ServerManager)applicationContext.getBean(
                        "serverManager");
            
            } catch (NoSuchBeanDefinitionException x) {
                // we expect this
            }
        } catch (Exception x) {
            log.error("init", x);
            
            throw new RuntimeException(x);
        }
    }

    @Override
	public void doFilter(ServletRequest request, ServletResponse res,
	        FilterChain chain) throws IOException, ServletException {

        final StatusExposingServletResponse response = new StatusExposingServletResponse(
                (HttpServletResponse) res);
        final ServerManager serverManager = this.serverManager;
        if (serverManager != null) {
            serverManager.requestStarted();
        }
        
        boolean successReq = false;
        final boolean trace = log.isTraceEnabled();
        long start = 0L;
        if (trace) {
            start = System.currentTimeMillis();
        }
        
	    try {
	        ServletHelpers.setRequest((HttpServletRequest) request);
	        
			chain.doFilter(request, response);
			successReq = true;
			
	    } catch (NestedServletException x) {
	        Throwable t = x.getCause();
	        if (t instanceof OperationFailedException) {
	            writeOperationFailedException((OperationFailedException) t, request, response);
	        } else {
	            writeException(x, request, response);
	        }
		} catch (OperationFailedException x) {
		    writeOperationFailedException(x, request, response);
		
		} catch (Exception x) {
		    writeException(x, request, response);
		
		} finally {
		    if (trace) {
    	        long time = System.currentTimeMillis() - start;
    	        HttpServletRequest httpReq = (HttpServletRequest) request;
    	        String query = httpReq.getQueryString();
    	        log.trace("Rendering duration: " 
    	                + ServletHelpers.getRequestAddr() 
    	                + " [" + httpReq.getMethod() 
    	                + " " + httpReq.getRequestURI() 
    	                + (query != null ? "?" + query : "")
    	                + "] is " + time + " ms.");
		    }
		
            if (serverManager != null) {
                serverManager.requestFinished(successReq);
            }
		}
    }
	
    private void writeOperationFailedException(OperationFailedException x, 
            ServletRequest request, StatusExposingServletResponse response)
            throws IOException {
        
        HttpServletRequest  httpReq  = (HttpServletRequest)request;
        ResponseMessage respMsg = x.getResponseMessage();
        
        String error = "Error occurred while performing " + httpReq.getMethod() 
                + " request to " + httpReq.getRequestURI() 
                + "\n\terror: " + x.getMessage() + (x.getCause() != null ? 
                        ", cause: " + x.getCause().toString() : "");

        if (respMsg == CommonResponses.AUTHENTICATION_FAILED) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn(error);
        
        } else if (respMsg == CommonResponses.ACCESS_DENIED) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.warn(error);
        
        } else {
            log.error(error);
        }
        
        serializeFailedResponse(httpReq, response, respMsg, x);
    }

    private void writeException(Exception x, ServletRequest request,
            StatusExposingServletResponse response) throws IOException {
        
        HttpServletRequest httpReq = (HttpServletRequest) request; 
        log.error("Error occurred while performing " + httpReq.getMethod() 
                + " request to " + httpReq.getRequestURI(), x);

        serializeFailedResponse(httpReq, response,
                CommonResponses.INTERNAL_SERVER_ERROR, x);
    }
    
    private void serializeFailedResponse(HttpServletRequest httpReq,
            StatusExposingServletResponse response, ResponseMessage respMsg, Exception x)
            throws IOException {
        
        StringWriter stackTrace = new StringWriter();
        x.printStackTrace(new PrintWriter(stackTrace, true));
        
        // put login redirect URL only if in the same domain
        BaseResponse resp;
        if ((response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED 
                    || respMsg == CommonResponses.AUTHENTICATION_FAILED)
                && httpReq.getServerName().endsWith(ServletHelpers.getAppDomain())) {
            
            resp = new LoginRedirectResponse(
                    adminService.getLoginRedirectUrl(httpReq, null));
            resp.setStatus(respMsg.getStatus());
        } else {
            resp = new BaseResponse(respMsg.getStatus());
        }
        
        resp.setMessage(respMsg.getMessage(httpReq.getLocale()));
        resp.setError(stackTrace.toString());
        
        if (response.getContentType() == null) {
            response.setContentType("application/json");
        }
        response.setCharacterEncoding("UTF-8");
        
        Writer writer = response.getWriter();
        jsonSerializer.serialize(resp, writer);
        writer.flush();
    }
}
