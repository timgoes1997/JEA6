package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.user.User;

import javax.security.auth.login.LoginException;

public interface UserDAO {

    void create(User user);

    void edit(User user);

    User find(long id);

    User findByUsername(String userName);

    void addFollower(User accToFollow, User accToFollowing);

    void removeFollower(User userFollow, User accToRemoveFollow);

    User authenticate(String username, String encryptedPassword);
}
