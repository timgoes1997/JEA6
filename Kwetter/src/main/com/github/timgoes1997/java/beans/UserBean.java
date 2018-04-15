package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.token.TokenProvider;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import org.eclipse.persistence.internal.libraries.antlr.runtime.Token;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@Path("user")
public class UserBean {

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @Inject
    private TokenProvider tokenProvider;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void addUser(@PathParam("id") Long accId) {
        userDAO.create(new User("Timmeke", "Wachtwoord", UserRole.User, "Tim", "Goes", "Tim.goes@student.fontys.nl", "0600000000"));
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("username") String username, @FormParam("password") String password){
        try{
            User user = userDAO.authenticate(username, password); //TODO: In future encrypt password using salt and secret key.

            Set<String> authorities = new HashSet<>(); //TODO: Better authorities implementation.
            authorities.add(user.getRole().toString());

            String token = tokenProvider.issueToken(user.getUsername(), authorities, false); //TODO: remember me implementation

            return Response.ok().header(HttpHeaders.AUTHORIZATION, Constants.BEARER + token).build();

        }catch(Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
