package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

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
        TypedQuery<User> query =
                em.createNamedQuery(User.FIND_BY_ID, User.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public User findByUsernameAndEmail(String userName, String email) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME_AND_EMAIL, User.class);
        return query.setParameter("name", userName)
                    .setParameter("email", email)
                    .getSingleResult();
    }

    @Override
    public User findByUsername(String userName) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME, User.class);
        return query.setParameter("name", userName).getSingleResult();
    }

    @Override
    public User findByVerificationLink(String link){
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK, User.class);
        return query.setParameter("link", link).getSingleResult();
    }

    @Override
    public boolean usernameExists(String username) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME, User.class);
        return query.setParameter("name", username).getResultList().size() > 0;
    }

    @Override
    public boolean emailExists(String email) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_EMAIL, User.class);
        return query.setParameter("email", email).getResultList().size() > 0;
    }

    @Override
    public boolean usernameAndEmailExists(String username, String email) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME_AND_EMAIL, User.class);
        return query.setParameter("name", username)
                    .setParameter("email", email)
                    .getResultList().size() > 0;
    }

    @Override
    public boolean verificationLinkExists(String link) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK, User.class);
        return query.setParameter("link", link).getResultList().size() > 0;
    }

    @Override
    public boolean hasBeenVerified(String link){
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK_AND_VERIFICATION, User.class);
        return query.setParameter("link", link)
                    .setParameter("verified", true)
                    .getResultList().size() > 0;
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
    public void removeFollower(User userFollow, User accToRemoveFollow) {
        //Question: Do I need to do a database optimized remove instead of this (might cause to much memory usage)
        userFollow.removeFollower(accToRemoveFollow);
        accToRemoveFollow.removeFollowing(userFollow);
        edit(userFollow);
        edit(accToRemoveFollow);
    }

    @Override
    public User authenticate(String username, String encryptedPassword) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME_PASSWORD,User.class);
        query.setParameter("name", username);
        query.setParameter("password",  encryptedPassword);

        User user = query.getSingleResult();
        if(user == null){
            throw new SecurityException("Entered a invalid username or password");
        }else{
            return user;
        }
    }

    /**
     * For testing purposes
     *
     * @param em entitymanager for unittesting this bean/dao
     */
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}
