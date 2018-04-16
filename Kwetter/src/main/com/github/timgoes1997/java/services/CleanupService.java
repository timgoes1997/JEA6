package com.github.timgoes1997.java.services;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.user.User;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Singleton
public class CleanupService {

    @Inject
    private UserDAO userDAO;

    private static final long validationPeriod = 3 * 24 * 60 * 60; //3days;

    @Schedule(dayOfWeek="*/1")
    public void cleanupUnverifiedAccounts(){
        long now = new Date().getTime();
        long min = now - validationPeriod;
        Date minValidationDate = new Date(min);
        List<User> users = userDAO.getUnverifiedAccounts(minValidationDate);

        for (User u: users) {
            userDAO.remove(u);
        }
    }
}
