package dao;

import dao.interfaces.UserDAO;
import entity.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void edit(User user) {
        em.merge(user);
    }

    @Override
    public User find(long id) {
        return null;
    }

    @Override
    public User findByUsername(String userName) {
        return null;
    }

    @Override
    public void addFollower(User accToFollow, User accToFollowing) {

    }
}
