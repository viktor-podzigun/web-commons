
package com.googlecode.common.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import com.googlecode.common.util.StringHelpers;


/**
 * Client for working with LDAP database.
 */
public final class LdapClient {
    
    private static final String ATTR_EMAIL      = "mail";
    private static final String ATTR_LOGIN      = "userPrincipalName";
    private static final String ATTR_FULL_NAME  = "cn";
    
    private final String        url;
    private final Properties    props;
    
    
    /**
     * Creates LdapClient object for LDAP connection.<br/>
     * URL example <tt>"ldap://localhost:8389/dc=corp,dc=company,dc=ua"</tt>
     * <br/>login example <tt>test</tt>t or <tt>test@corp.test.ua</tt>
     * 
     * @param providerUrl   url for connection
     * @param login         login for connection
     * @param password      password for connection
     */
    public LdapClient(String providerUrl, String login, String password) {
        this.url    = providerUrl;
        this.props  = createContextProps(getFullLogin(login), password);
    }
    
    /**
     * Creates properties for connection to LDAP. If connection fails throws 
     * <code>NamingException</code>
     * 
     * @param login     login for connection
     * @param password  password for connection
     * @return          propeties for connection to LDAP
     * 
     * @throws NamingException if <code>NamingException</code> occurs.
     */
    private Properties createContextProps(String login, String password) {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, login);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.REFERRAL, "follow");
        return env;
    }
    
    /**
     * Closes connection to LDAP.
     * 
     * @param ctx open LDAP connection
     * 
     * @throws IOException if closing fails
     */
    private void closeContext(DirContext ctx) throws IOException {
        try {
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException x) {
            throw new IOException(x);
        }
    }
    
    private String getFullLogin(String login) {
        if (login.indexOf('@') != -1) {
            return login;
        }
        
        return login /*+ "@CORP.DOMAIN"*/;
    }
    
    /**
     * Tries to login the specified user.
     * 
     * @param login     user login
     * @param password  user password
     * @return          true if and only if user with <code>login</code> and 
     *                  <code>password</code> exist in LDAP database
     * 
     * @throws IOException if LDAP connection fails
     */
    public boolean authenticateUser(String login, String password) 
            throws IOException {
        
        if (StringHelpers.isNullOrEmpty(login)) {
            throw new IllegalArgumentException("login is null or empty");
        }
        if (StringHelpers.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("password is null or empty");
        }
        
        DirContext dctx = null; 
        try {
            dctx = new InitialDirContext(createContextProps(
                    getFullLogin(login), password));
            
            return true;
         
        } catch (AuthenticationException x) {
            return false;
            
         } catch (NamingException x) {
             throw new IOException(x);
         
         } finally {
             closeContext(dctx);
         }
    }
    
    /**
     * 
     * @param ctx       LDAP context
     * @param filter    filter. Example "cn=*" - get all users with <tt>cn</tt> 
     *                  attribute
     * @param attrs     a set of attributes that you want to get from LDAP 
     *                  service
     * @return          Enumeration with filtered attributes for users
     * 
     * @throws NamingException if <code>NamingException</code> occurs
     */
    private NamingEnumeration<SearchResult> doSearch(DirContext ctx, 
            String filter, String... attrs) throws NamingException {
    
        SearchControls params = new SearchControls();
        params.setReturningAttributes(attrs);
        params.setSearchScope(SearchControls.SUBTREE_SCOPE);

        return ctx.search("", filter, params);
        
    }

    /**
     * Returns Information about user with passed login. Returns 
     * <code>null</code> if user not found. Throws <code>IOException</code>
     *  if LDAP connection fails.
     *         
     * @param login user login
     * @return      Information about user with passed login. Returns 
     *              <code>null</code> if user not found.
     *         
     * @throws IOException if LDAP connection fails;
     */
    public LdapUser getUserByLogin(String login) throws IOException {
        if (StringHelpers.isNullOrEmpty(login)) {
            throw new IllegalArgumentException("login is null or empty");
        }
        
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(props);
            NamingEnumeration<SearchResult> results = doSearch(ctx, 
                    ATTR_LOGIN + "=" + getFullLogin(login), 
                    ATTR_EMAIL, ATTR_FULL_NAME, ATTR_LOGIN);
            
            // user not found
            if (!results.hasMore()) {
                return null;
            }
            
            // specified login can store only one user, so get user info
            Attributes attrs = results.next().getAttributes();

            String fullName = getAttribute(attrs, ATTR_FULL_NAME);
            String mail     = getAttribute(attrs, ATTR_EMAIL);
            
            return new LdapUser(login, fullName, mail);
            
        } catch (NamingException x) {
           throw new IOException(x);
        
        } finally {
            closeContext(ctx);
        }
    }
    
    /**
     * Returns attribute value for attribute ID. Throws 
     * <code>NamingException</code> if  If a naming exception was encountered
     * while retrieving the value.
     * 
     * @param attrs LDAP attributes for user
     * @param name  attribute ID
     * @return      attribute value
     * 
     * @throws NamingException if <code>NamingException</code> occurs
     */
    private static String getAttribute(Attributes attrs, String name) 
            throws NamingException {
        
        Object val = null;
        Attribute attr = attrs.get(name);
        if (attr != null) {
            val = attr.get();
        }
        
        return (val != null ? val.toString() : null);
    }
    
    /**
     * Returns all logins for specified LDAP data base
     * 
     * @return all logins for specified LDAP data base
     * 
     * @throws IOException if <code>NamingException</code> occurs
     */
    List<String> getAllLogins() throws IOException {
        List<String> list = new ArrayList<String>();
        
        DirContext dctx = null;
        try {
            dctx = new InitialDirContext(props);
            NamingEnumeration<SearchResult> results = doSearch(dctx, 
                    ATTR_LOGIN + "=*", ATTR_LOGIN);
            
            while (results.hasMore()) {
                SearchResult sr = results.next();
                Attributes attrs = sr.getAttributes();
                
                list.add(getAttribute(attrs, ATTR_LOGIN));
            }

            return list;
            
        } catch (NamingException x) {
           throw new IOException(x);
        
        } finally {
            closeContext(dctx);
        }
    }
    
    /**
     * Returns all LDAP users for specified LDAP data base
     * 
     * @return all users for specified LDAP data base
     * 
     * @throws IOException if <code>NamingException</code> occurs
     */
    public List<LdapUser> getAllUsers() throws IOException {
        List<LdapUser> list = new ArrayList<LdapUser>();
        
        DirContext dctx = null;
        try {
            dctx = new InitialDirContext(props);
            NamingEnumeration<SearchResult> results = doSearch(dctx, 
                    ATTR_LOGIN + "=*", ATTR_LOGIN, ATTR_FULL_NAME, ATTR_EMAIL);
            
            while (results.hasMore()) {
                SearchResult sr = results.next();
                Attributes attrs = sr.getAttributes();

                list.add(new LdapUser(getAttribute(attrs, ATTR_LOGIN),
                        getAttribute(attrs, ATTR_FULL_NAME), getAttribute(
                                attrs, ATTR_EMAIL)));
            }

            return list;
            
        } catch (NamingException x) {
           throw new IOException(x);
        
        } finally {
            closeContext(dctx);
        }
    }
}
