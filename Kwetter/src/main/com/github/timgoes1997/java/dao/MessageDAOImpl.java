package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Message message) {
        em.persist(message);
    }

    @Override
    public void remove(Message message) {
        em.remove(message);
    }

    @Override
    public Message find(long id) {
        TypedQuery<Message> query =
                em.createNamedQuery("Message.findByID", Message.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Message> findMessagesByUser(User user) {
        TypedQuery<Message> query =
                em.createNamedQuery("Message.findByUser", Message.class);
        return query.setParameter("id", user.getId()).getResultList();
    }

    @Override
    public List<Message> findMessagesByUser(User user, int firstResult, int maxResults) {
        TypedQuery<Message> query =
                em.createNamedQuery("Message.findByUser", Message.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", user.getId()).getResultList();
    }

    @Override
    public List<ReplyMessage> getMessageReplies(Message message) {
        TypedQuery<ReplyMessage> query =
                em.createNamedQuery("Message.findReplies", ReplyMessage.class);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<ReplyMessage> getMessageReplies(Message message, int firstResult, int maxResults) {
        TypedQuery<ReplyMessage> query =
                em.createNamedQuery("Message.findReplies", ReplyMessage.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<Remessage> getMessageRemessages(Message message) {
        TypedQuery<Remessage> query =
                em.createNamedQuery("Message.findRemessages", Remessage.class);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<Remessage> getMessageRemessages(Message message, int firstResult, int maxResults) {
        TypedQuery<Remessage> query =
                em.createNamedQuery("Message.findRemessages", Remessage.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public Long getMessageLikes(Message message) {
        return null;
    }

    @Override
    public Date getCurrentLocalDateTime() {
        return null;
    }

    @Override
    public List<Tag> generateTags(String text) {
        return null;
    }

    @Override
    public List<User> generateMentions(String text) {
        return null;
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
