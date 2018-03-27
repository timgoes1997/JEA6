package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.EntityManager;
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
        return query.getSingleResult();
    }

    @Override
    public Message findMessageByUser(User user) {
        return null;
    }

    @Override
    public List<ReplyMessage> getMessageReplies(Message message) {
        return null;
    }

    @Override
    public List<Remessage> getMessageRemessages(Message message) {
        return null;
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
     * @param em entitymanager for unittesting this bean/dao
     */
    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
