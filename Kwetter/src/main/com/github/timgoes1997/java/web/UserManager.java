package com.github.timgoes1997.java.web;

import com.github.timgoes1997.java.authentication.session.auth.SessionAuth;
import com.github.timgoes1997.java.services.ServiceHelper;
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
    private String email;

    private String firstName;
    private String middleName;
    private String lastName;
    private String telephone;

    public String login() {
        try {
            return userService.authenticateUsingSession(getCurrentRequest(), username, password);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return "error";
        }
    }

    public boolean isLoggedIn() {
        return SessionAuth.isLoggedIn(getCurrentRequest());
    }

    public String register() {
        try {
            if (username == null || username == "" ||
                    password == null || password == "" ||
                    email == null || email == "" ||
                    firstName == null || firstName == "" ||
                    lastName == null || lastName == "" ||
                    telephone == null || telephone == ""){
                return "register";
            }else{
                userService.registerUser(username, password, email, firstName, middleName, lastName, telephone);
            }
            return "created";
        } catch (Exception e) {
            logger.info(e.getMessage());
            return "error";
        }
    }

    public String logout() {
        try {
            return userService.logoutFromSession(getCurrentRequest());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return "error";
        }
    }

    private HttpServletRequest getCurrentRequest() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpServletRequest) context.getExternalContext().getRequest();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
