package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.user.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.security.auth.login.LoginException;

public interface UserDAO {

    void create(User user);

    void edit(User user);

    User find(long id);

    User findByUsername(String userName);

    User findByUsernameAndEmail(String userName, String email);

    User findByVerificationLink(String link);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    boolean usernameAndEmailExists(String username, String email);

    boolean verificationLinkExists(String link);

    boolean hasBeenVerified(String link);

    void addFollower(User accToFollow, User accToFollowing);

    void removeFollower(User userFollow, User accToRemoveFollow);

    User authenticate(String username, String encryptedPassword);
}
