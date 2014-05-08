
package com.googlecode.common.showcase.server;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Dummy servlet for client requests.
 */
public class DummyServlet extends HttpServlet {

    private static final long serialVersionUID = 7643403810028300101L;

    private String  token;
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json;charset=utf-8");
        
        String path = req.getPathInfo();
        System.out.println("Path: " + path);
        if (path.startsWith("/showcase")) {
            path = path.substring("/showcase".length());
        }
        
        Writer writer = resp.getWriter();
        if (path.equals("/login")) {
            writeLoginResp(writer);
        
        } else if (path.equals("/login/token")) {
            if (token == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.write("{\"status\": 402}");
            } else {
                writeLoginResp(writer);
            }
        } else if (path.equals("/logout")) {
            token = null;
            writer.write("{\"status\": 0}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
    }
    
    private void writeLoginResp(Writer writer) throws IOException {
        try {
            JSONObject app1 = new JSONObject();
            app1.put("title", "App 1");
            app1.put("url", "#");
            
            JSONObject app2 = new JSONObject();
            app2.put("title", "App 2");
            app2.put("url", "#");
            
            JSONObject env1 = new JSONObject();
            env1.put("title", "dev");
            env1.put("subMenu", Arrays.asList(app1, app2));
            
            JSONObject env2 = new JSONObject();
            env2.put("title", "qa");
            env2.put("subMenu", Arrays.asList(app1, app2));
            
            JSONObject env = new JSONObject();
            env.put("title", "Environments");
            env.put("subMenu", Arrays.asList(env1, env2));
            
            JSONObject appMenu = new JSONObject();
            appMenu.put("title", "Showcase");
            appMenu.put("url", "#");
            appMenu.put("subMenu", Arrays.asList(app1, app2, env));
            
            token = "126b8b2033476a285a394215db4052384fe50f71";
            
            JSONObject loginData = new JSONObject();
            loginData.put("token", token);
            loginData.put("superUser", true);
            loginData.put("appMenu", appMenu);
            loginData.put("login", "login");
            
            JSONObject loginResp = new JSONObject();
            loginResp.put("status", 0);
            loginResp.put("data", loginData);
            
            loginResp.write(writer);
        
        } catch (JSONException x) {
            throw new IOException(x);
        }
    }

}
