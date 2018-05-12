package com.github.timgoes1997.java.authentication.token.interceptor;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.token.TokenProvider;
import com.github.timgoes1997.java.entity.user.User;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;


@Provider
@UserTokenAuthorization
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthorizationFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Inject
    private TokenProvider tokenProvider;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Method resourceMethod = resourceInfo.getResourceMethod();
        UserTokenAuthorization tokenAuthorization = resourceMethod.getAnnotation(UserTokenAuthorization.class);
        if (tokenAuthorization == null) return;

        try {
            String token = extractToken(requestContext);

            tokenProvider.validateToken(token);
            logger.info("Valid token: " + token);

            User user = tokenProvider.extractUser(token);

            requestContext.setProperty(Constants.USER_REQUEST_STRING, user);
            logger.info("User for token: " + token);
        } catch (Exception e) {
            if (!tokenAuthorization.requiresUser()) return;
            logger.severe("Invalid token or non given");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    public String extractToken(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        logger.info("Auth token used: " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith(Constants.BEARER)) {
            logger.severe("Invalid auth header: " + authorizationHeader);
            throw new NotAuthorizedException("A valid authorization header must be provided!");
        }

        return authorizationHeader.substring(Constants.BEARER.length(), authorizationHeader.length());
    }
}
