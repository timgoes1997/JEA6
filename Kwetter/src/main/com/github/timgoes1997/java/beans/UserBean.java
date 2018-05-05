package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.interceptor.UserAuthorization;
import com.github.timgoes1997.java.authentication.token.TokenProvider;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.EmailService;
import org.eclipse.persistence.internal.libraries.antlr.runtime.Token;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
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

    @Inject
    private EmailService emailService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response getUserByID(@PathParam("id") long id) {
        try {
            User user = userDAO.find(id);
            return Response.ok().entity(user).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
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

            return Response.ok().header(HttpHeaders.AUTHORIZATION, Constants.BEARER + token).entity(user).build();

        }catch(Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(@FormParam("username") String username,
                             @FormParam("password") String password,
                             @FormParam("email") String email,
                             @FormParam("firstName") String firstName,
                             @FormParam("middleName") String middleName,
                             @FormParam("lastName") String lastName,
                             @FormParam("telephone") String telephone){
        try{
            if(userDAO.usernameExists(username) || userDAO.emailExists(email)){
                return Response.status(Response.Status.CONFLICT).build();
            }

            userDAO.create(new User(username, password, UserRole.User, firstName, middleName, lastName, email, telephone, false));

            logger.info("Created new account for user: " + username);

            User user = userDAO.findByUsername(username);
            user.setVerifyLink(emailService.generateVerificationLink(user));
            user.setRegistrationDate(new Date());
            userDAO.edit(user);

            emailService.sendVerificationMail(user);

            return authenticate(username, password);
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Path("/verify/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmRegistration(@PathParam("token") String token){
        try{
            if(!userDAO.verificationLinkExists(token)){
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }

            if(userDAO.hasBeenVerified(token)){
                return Response.status(Response.Status.CONFLICT).entity("You have already verified your account").build();
            }

            User user = userDAO.findByVerificationLink(token);
            if(user == null){
                return Response.serverError().build();
            }
            user.setVerified(true);
            userDAO.edit(user);

            return Response.ok("Account has been verified").build();

        }catch(Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUsername(@PathParam("username") String username) {
        try {
            User user = userDAO.findByUsername(username);
            return Response.ok().entity(user).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @POST
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response follow(@Context ContainerRequestContext request, @PathParam("username") String username){
        try{
            if(!userDAO.usernameExists(username)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            User currentUser = (User)request.getProperty(Constants.USER_REQUEST_STRING);

            User userToFollow = userDAO.findByUsername(username);

            return  Response.ok().build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @DELETE
    @Path("/{username}/delete")
    @UserAuthorization({UserRole.User})
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context ContainerRequestContext request, @PathParam("username") String username){
        try{
            if(!userDAO.usernameExists(username)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            User currentUser = (User)request.getProperty(Constants.USER_REQUEST_STRING);
            User userToDelete = userDAO.findByUsername(username);

            if(currentUser.getId() == userToDelete.getId()){
                userDAO.remove(currentUser);
            }

            return  Response.ok().build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();
        }
    }

    @GET
    @Path("/notoken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response noTokenGetRequest(){
        return Response.ok("Hello").build();
    }

    @GET
    @Path("/token")
    @UserAuthorization
    @Produces(MediaType.APPLICATION_JSON)
    public Response tokenGetRequest(){
        return Response.ok("Hello").build();
    }
}
