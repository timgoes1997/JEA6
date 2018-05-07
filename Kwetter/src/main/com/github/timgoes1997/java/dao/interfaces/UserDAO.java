package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.user.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.List;

public interface UserDAO {

    User authenticate(String username, String encryptedPassword);
    void addFollower(User accToFollow, User accToFollowing);
    void create(User user);

    void edit(User user);
    boolean exists(String username);
    boolean emailExists(String email);

    User find(long id);
    User findByUsername(String userName);
    User findByUsernameAndEmail(String userName, String email);
    User findByVerificationLink(String link);

    boolean hasBeenVerified(String link);
    boolean isFollowing(User loggedIn, User loggedInFollowing);

    List<User> getAllUsers();
    long getAmountOfFollowers(User user);
    long getAmountOfFollowings(User user);
    List<User> getUnverifiedAccounts(Date lowerThanThisDate);

    void remove(User user);
    boolean removeUserMessage(Message message);
    void removeFollower(User userFollow, User accToRemoveFollow);
    long removeUserFollowing(User user);
    long removeUserFollowers(User user);

    boolean usernameExists(String username);
    boolean usernameAndEmailExists(String username, String email);

    boolean verificationLinkExists(String link);
}
