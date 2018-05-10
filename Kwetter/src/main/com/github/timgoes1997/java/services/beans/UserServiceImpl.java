package com.github.timgoes1997.java.services.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.session.auth.SessionAuth;
import com.github.timgoes1997.java.authentication.token.TokenProvider;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.EmailService;
import com.github.timgoes1997.java.services.ServiceHelper;
import com.github.timgoes1997.java.services.beans.interfaces.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private EmailService emailService;

    @Override
    public User getAuthenticatedUser(ContainerRequestContext requestContext) {
        return (User)requestContext.getProperty(Constants.USER_REQUEST_STRING);
    }

    @Override
    public User getByUsername(String username) {
        try {
            return userDAO.findByUsername(username.toLowerCase());
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find user for given username");
        }
    }

    @Override
    public User getUserByID(long id) {
        try {
            return userDAO.find(id);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find user for given id");
        }
    }

    @Override
    public Response authenticateUsingToken(String username, String password) {
        try {
            User user = userDAO.authenticate(username, password); //TODO: In future encrypt password using salt and secret key.

            if (!user.getVerified()) {
                throw new NotAuthorizedException("Please verify your e-mail before loging in!");
            }

            Set<String> authorities = new HashSet<>(); //TODO: Better authorities implementation.
            authorities.add(user.getRole().toString());

            String token = tokenProvider.issueToken(user.getUsername(), authorities, false); //TODO: remember me implementation

            return Response.ok().header(HttpHeaders.AUTHORIZATION, Constants.BEARER + token).entity(user).build();

        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new NotAuthorizedException("Entered username or password is invalid, please try again!");
        }
    }

    @Override
    public String authenticateUsingSession(HttpServletRequest request, String username, String password) {
        User user = userDAO.authenticate(username, password); //TODO: In future encrypt password using salt and secret key.

        if (!user.getVerified()) {
            throw new NotAuthorizedException("Please verify your e-mail before loging in!");
        }

        if(SessionAuth.login(request, user)){
            return "messages";
        }else{
            return "error";
        }
    }

    @Override
    public Response confirmUserRegistration(String token) {
        try {
            if (!userDAO.verificationLinkExists(token)) {
                throw new NotAcceptableException("Given verification link doesn't exist");
            }

            if (userDAO.hasBeenVerified(token)) {
                throw new NotAcceptableException("Your account has already been verified please login or create a password recovery request");
            }

            User user = userDAO.findByVerificationLink(token);
            if (user == null) {
                throw new InternalServerErrorException("Server couldn't find user for verification link please contact a administrator");
            }
            user.setVerified(true);
            userDAO.edit(user);

            Set<String> authorities = new HashSet<>(); //TODO: Better authorities implementation.
            authorities.add(user.getRole().toString());
            String bearerToken = tokenProvider.issueToken(user.getUsername(), authorities, false); //TODO: remember me implementation

            return Response.ok().header(HttpHeaders.AUTHORIZATION, Constants.BEARER + bearerToken)
                    .entity(user).tag("Registration confirmed").build();

        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Unkown exception occured while registrating user, please contact administrator");
        }
    }

    @Override
    public User registerUser(String username, String password, String email, String firstName, String middleName, String lastName, String telephone) {
        try {
            if (userDAO.usernameExists(username) || userDAO.emailExists(email)) {
                throw new NotAuthorizedException("Entered a already existing email or username please try a new one");
            }

            userDAO.create(new User(username.toLowerCase(), password, UserRole.User, firstName, middleName, lastName, email, telephone, false));

            logger.info("Created new account for user: " + username);

            User user = userDAO.findByUsername(username);
            user.setVerifyLink(emailService.generateVerificationLink(user));
            user.setRegistrationDate(new Date());
            userDAO.edit(user);

            emailService.sendVerificationMail(user);

            return user;
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't register user or send e-mail, try it once again or contact a administrator");
        }
    }

    @Override
    public User followUser(ContainerRequestContext requestContext, String username) {
        try {
            if (!userDAO.usernameExists(username)) {
                throw new NotFoundException("Couldn't find user you tried to follow, does the user exist?");
            }

            User currentUser = (User) requestContext.getProperty(Constants.USER_REQUEST_STRING);
            User userToFollow = userDAO.findByUsername(username);
            userDAO.addFollower(userToFollow, currentUser);

            return userToFollow;
        } catch (Exception e) {
            throw new InternalServerErrorException("A error occured while trying to follow " + username);
        }
    }

    @Override
    public boolean isFollowingUser(ContainerRequestContext requestContext, String username) {
        try {
            User user = userDAO.findByUsername(username.toLowerCase());
            User currentUser = (User) requestContext.getProperty(Constants.USER_REQUEST_STRING);
            return userDAO.isFollowing(currentUser, user);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find given username");
        }
    }

    @Override
    public String logoutFromSession(HttpServletRequest request) {
        if(SessionAuth.logout(request)){
            return "index";
        }
        logger.info("Logout failed");
        return "index";
    }

    @Override
    public User deleteUser(ContainerRequestContext requestContext, String username) {
        try {
            if (!userDAO.usernameExists(username)) {
                throw new NotFoundException("Couldn't find given username, does it exist?");
            }

            User currentUser = (User) requestContext.getProperty(Constants.USER_REQUEST_STRING);
            User userToDelete = userDAO.findByUsername(username);

            if (!ServiceHelper.isCurrentUserOrModeratorOrAdmin(currentUser, userToDelete)) {
                throw new NotAuthorizedException("User not authorized to remove user");
            }

            userDAO.remove(currentUser);

            return userToDelete;
        } catch (Exception e) {
            throw new InternalServerErrorException("Couldn't remove user");
        }
    }
}
