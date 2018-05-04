package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class MockDBBean {

    @Inject
    private UserDAO userDAO;

    @Inject
    private MessageDAO messageDAO;


    @PostConstruct
    void init(){
        userDAO.create(new User("admin", "admini", UserRole.Admin, "", "", "Admin@admin.com", "", true));
        userDAO.create(new User("moderator", "modo", UserRole.Moderator, "", "", "@Moderator@Moderator.com", "", true));
    }

}
