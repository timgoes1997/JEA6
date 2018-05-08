package com.github.timgoes1997.java.services;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;

import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;

public class ServiceHelper {

    public static void checkIfWebApplicationExceptionAndThrow(Exception e) throws WebApplicationException {
        if(e instanceof WebApplicationException){
            throw (WebApplicationException)e;
        }
    }

    public static boolean isCurrentUserOrModeratorOrAdmin(User currentUser, User toCheck){
        return toCheck.getId() == currentUser.getId()
                || currentUser.getRole() == UserRole.Moderator
                || currentUser.getRole() == UserRole.Admin;
    }

    public static boolean isCurrentUserOrAdmin(User currentUser, User toCheck){
        return toCheck.getId() == currentUser.getId()
                || currentUser.getRole() == UserRole.Admin;
    }
}
