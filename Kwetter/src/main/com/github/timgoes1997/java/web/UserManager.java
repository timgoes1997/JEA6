package com.github.timgoes1997.java.web;

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
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            return userService.authenticateUsingSession(request, username, password);
        } catch (Exception e) {
            return "index";
        }
    }

    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try{
            return userService.logoutFromSession(request);
        }catch (Exception e){
            logger.severe(e.getMessage());
            return "index";
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
}
