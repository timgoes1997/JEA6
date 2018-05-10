package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.interceptor.UserTokenAuthorization;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.beans.interfaces.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

@Stateless
@Path("user")
public class UserBean {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public User getUserByID(@PathParam("id") long id) {
        return userService.getUserByID(id);
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("username") String username, @FormParam("password") String password){
        return userService.authenticate(username, password);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public User register(@FormParam("username") String username,
                             @FormParam("password") String password,
                             @FormParam("email") String email,
                             @FormParam("firstName") String firstName,
                             @FormParam("middleName") String middleName,
                             @FormParam("lastName") String lastName,
                             @FormParam("telephone") String telephone){
        return userService.registerUser(username, password, email, firstName, middleName, lastName, telephone);
    }

    @GET
    @Path("/verify/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmRegistration(@PathParam("token") String token){
        return userService.confirmUserRegistration(token);
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByUsername(@PathParam("username") String username) {
        return userService.getByUsername(username);
    }

    @GET
    @Path("{username}/following")
    @UserTokenAuthorization
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isFollowingUser(@Context ContainerRequestContext request,@PathParam("username") String username) {
        return userService.isFollowingUser(request, username);
    }

    @POST
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public User follow(@Context ContainerRequestContext request, @PathParam("username") String username){
        return userService.followUser(request, username);
    }

    @DELETE
    @Path("/{username}/delete")
    @UserTokenAuthorization({UserRole.User})
    @Produces(MediaType.APPLICATION_JSON)
    public User delete(@Context ContainerRequestContext request, @PathParam("username") String username){
        return userService.deleteUser(request, username);
    }

    @GET
    @Path("/authenticated")
    @UserTokenAuthorization
    @Produces(MediaType.APPLICATION_JSON)
    public User getAuthenticatedUser(@Context ContainerRequestContext request){
        return userService.getAuthenticatedUser(request);
    }
}
