package com.github.timgoes1997.java.web;

import com.github.timgoes1997.java.authentication.session.auth.SessionAuth;
import com.github.timgoes1997.java.services.beans.interfaces.UserService;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class UserManager implements Serializable {

    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    @Inject
    private UserService userService;

    private String username;
    private String password;

    public String login() {
        try {
            return userService.authenticateUsingSession(getCurrentRequest(), username, password);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return "error";
        }
    }

    public boolean isLoggedIn(){
        return SessionAuth.isLoggedIn(getCurrentRequest());
    }

    public String logout(){
        try{
            return userService.logoutFromSession(getCurrentRequest());
        }catch (Exception e){
            logger.severe(e.getMessage());
            return "error";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private HttpServletRequest getCurrentRequest(){
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpServletRequest) context.getExternalContext().getRequest();
    }
}
