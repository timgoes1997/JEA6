package dao.interfaces;

import entity.user.User;

public interface UserDAO {

    void create(User user);

    void edit(User user);

    User find(long id);

    User findByUsername(String userName);

    void addFollower(User accToFollow, User accToFollowing);

}
