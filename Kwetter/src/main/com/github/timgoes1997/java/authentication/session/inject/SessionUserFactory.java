package com.github.timgoes1997.java.authentication.session.inject;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.entity.user.interfaces.UserInterface;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;

public class SessionUserFactory {

    @Inject
    private HttpServletRequest request;

    @Dependent
    @Produces
    @CurrentSessionUser
    public UserInterface createSessionUser() {
        return (UserInterface)request.getSession().getAttribute(Constants.SESSION_USER);
    }
}
