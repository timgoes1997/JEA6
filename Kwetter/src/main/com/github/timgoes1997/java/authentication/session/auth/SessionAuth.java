package com.github.timgoes1997.java.authentication.session.auth;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.entity.user.interfaces.UserInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionAuth {

    public static boolean login(HttpSession session, UserInterface user) {
        if (user == null) return false;

        session.setAttribute(Constants.SESSION_USER, user);
        return true;
    }

    public static boolean login(HttpServletRequest request, UserInterface user) {
        return login(request.getSession(), user);
    }

    public static boolean logout(HttpSession session){
        if(session.getAttribute(Constants.SESSION_USER) == null) return false;
        session.setAttribute(Constants.SESSION_USER, null);
        return true;
    }

    public static boolean logout(HttpServletRequest request){
        return logout(request.getSession());
    }
}
