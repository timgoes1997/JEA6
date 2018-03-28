package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("user")
public class UserBean {

    @Inject
    private UserDAO userDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void addUser(@PathParam("id") Long accId) {
        userDAO.create(new User("Timmeke", "Wachtwoord", UserRole.User, "Tim", "Goes", "Tim.goes@student.fontys.nl", "0600000000"));
    }

}
