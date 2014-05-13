
package com.googlecode.common.ldap;


/**
 * Contains LDAP user details.
 */
public final class LdapUser {
    
    private final String    login;
    private final String    fullName;
    private final String    email;

    
    public LdapUser(String login, String fullName, String mail) {
        if (login == null) {
            throw new NullPointerException("login");
        }

        if (fullName == null) {
            throw new NullPointerException("fullName");
        }

        this.login      = login;
        this.fullName   = fullName;
        this.email      = mail;
    }
    
    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
        
    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{login: " + login
                + (fullName != null ? ", fullName: " + fullName : "")
                + (email != null ? ", email: " + email : "") 
                + "}";
    }
    
}
