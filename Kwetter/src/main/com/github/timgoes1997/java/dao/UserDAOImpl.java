package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Stateless
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private Logger logger;

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void edit(User user) {
        em.merge(user);
    }

    @Override
    public void remove(User user) {
        //First delete all the messages of the user self
        CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<Message>(user.getMessages());
        ListIterator<Message> iterator = messages.listIterator();
        while (iterator.hasNext()) {
            messageDAO.remove(iterator.next());
        }
        messageDAO.nullMessagers(user);

        /*
        //Delete all references messages
        List<Message> messages = messageDAO.findMessagesByUser(user);
        for(Message m : messages){
            messageDAO.remove(m);
        }*/

        User toRemove = find(user.getId());
        em.remove(toRemove);
    }

    @Override
    public boolean removeUserMessage(Message message) {
        User user = message.getMessager();
        if(user == null){
            logger.severe("username null for message removal" + message.getId());
        }

        logger.info("Removing message from " + user.getUsername());
        user.getMessages().remove(message); //TODO: nullpointer need to fix
        User merged = em.merge(user);
        em.flush();
        return !merged.getMessages().contains(message);
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

    /*
    @Override
    public User findByMessage(Message message) {
        TypedQuery<User> query =
                em.createNamedQuery(User.FIND_BY_MESSAGE, User.class);
        return query.setParameter("id", message.getId()).getSingleResult();
    }*/

    @Override
    public User findByVerificationLink(String link) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_VERIFICATION_LINK, User.class);
        return query.setParameter("link", link).getSingleResult();
    }

    @Override
    public List<User> getUnverifiedAccounts(Date lowerThanThisDate) {
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_VERIFIED_BELOW_DATE, User.class);
        return query.setParameter("verified", false)
                .setParameter("date", lowerThanThisDate)
                .getResultList();
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
    public boolean hasBeenVerified(String link) {
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
        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_NAME_PASSWORD, User.class);
        query.setParameter("name", username);
        query.setParameter("password", encryptedPassword);

        User user = query.getSingleResult();
        if (user == null) {
            throw new SecurityException("Entered a invalid username or password");
        } else {
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

    public void setMessageDAO(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
}
