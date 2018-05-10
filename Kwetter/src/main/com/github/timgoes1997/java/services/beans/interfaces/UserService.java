package com.github.timgoes1997.java.services.beans.interfaces;

import com.github.timgoes1997.java.entity.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

public interface UserService {
    User getAuthenticatedUser(ContainerRequestContext requestContext);
    User getByUsername(String username);
    User getUserByID(long id);

    Response authenticateUsingToken(String username, String password);
    String authenticateUsingSession(HttpServletRequest request, String username, String password);
    Response confirmUserRegistration(String token); //Also return token in header and authenticate user in angular.
    User registerUser(String username, String password, String email, String firstName,
                      String middleName, String lastName, String telephone);

    User followUser(ContainerRequestContext requestContext, String username);
    boolean isFollowingUser(ContainerRequestContext requestContext, String username);

    String logoutFromSession(HttpServletRequest request);

    User deleteUser(ContainerRequestContext requestContext, String username);
}
