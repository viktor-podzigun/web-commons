
package com.googlecode.common.ldap;

import java.io.IOException;
import java.util.List;
import com.googlecode.common.ldap.LdapClient;
import com.googlecode.common.ldap.LdapUser;


/**
 * 
 */
public class TestLdap {
    
    public static void main(String[] args) throws IOException {
        String host = "ldap://localhost:8389/dc=corp,dc=company,dc=ua";
        String login = "user";
        String password = "pass";        
        LdapClient lc = new LdapClient(host, login, password);
        
        List<String> list = lc.getAllLogins();
        for (String user : list) {
            LdapUser li = lc.getUserByLogin(user);
            System.out.println(li);
        }
        
        System.out.println("------------------------------------------------");
        String testLogin = "v.soultan";
        System.out.println("get user: " + testLogin);
        LdapUser li = lc.getUserByLogin(testLogin);
        System.out.println(li);

        testLogin = "NOT EXIST";
        System.out.println("get user: " + testLogin);
        li = lc.getUserByLogin(testLogin);
        if (li == null) {
            System.out.println("User not found");
        } else {
            // if found (not null), it is error
            System.out.println(li);
        }
        
        testLogin = login + "@CORP.DOMAIN";
        System.out.println("Can authenticate user [" + testLogin + "] ?");
        boolean bool = lc.authenticateUser(testLogin, password);
        System.out.println(bool);
        
        testLogin = login + "qwerty";
        System.out.println("Can authenticate user [" + testLogin + "] ?");
        bool = lc.authenticateUser(testLogin, password);
        System.out.println(bool);
    }
}
