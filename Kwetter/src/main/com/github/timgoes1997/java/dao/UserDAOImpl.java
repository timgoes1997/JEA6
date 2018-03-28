package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
        try {
            TypedQuery<User> query =
                    em.createNamedQuery("User.findByID", User.class);
            return query.setParameter("id", id).getSingleResult();
        }catch (NoResultException exception){
            return null;
        }
    }

    @Override
    public User findByUsername(String userName) {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByName", User.class);
            return query.setParameter("User.findByName", userName).getSingleResult();
        }catch (NoResultException exception){
            return null;
        }
    }

    @Override
    public void addFollower(User userFollow, User accToFollow) {
        //Question: Do I need to do a database optimized add instead of this (might cause to much memory usage)
        userFollow.addFollower(accToFollow);
        accToFollow.addFollowing(userFollow);
        edit(userFollow);
        edit(accToFollow);
    }

    @Override
    public void removeFollower(User userFollow, User accToRemoveFollow){
        //Question: Do I need to do a database optimized remove instead of this (might cause to much memory usage)
        userFollow.removeFollower(accToRemoveFollow);
        accToRemoveFollow.removeFollowing(userFollow);
        edit(userFollow);
        edit(accToRemoveFollow);
    }

    /**
     * For testing purposes
     * @param em entitymanager for unittesting this bean/dao
     */
    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
