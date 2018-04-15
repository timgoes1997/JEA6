package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class MessageDAOImpl implements MessageDAO {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserDAO userDAO;

    @EJB
    private TagDAO tagDAO;

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
        return ((Number) em.createNamedQuery("Message.getLikesByID").setParameter("id", message.getId()).getSingleResult()).longValue();
    }

    @Override
    public Date getCurrentLocalDateTime() {
        return Date.from(LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public List<Tag> generateTags(String text) {
        Pattern tagPattern = Pattern.compile("#(\\w+)");
        Matcher mat = tagPattern.matcher(text);
        List<Tag> tags= new ArrayList<>();
        while (mat.find()) {
            String tag = mat.group(1);
            try {
                tags.add(tagDAO.findTagByName(tag));
            }catch (NoResultException exception){
                tagDAO.create(new Tag(mat.group(1)));
                tags.add(tagDAO.findTagByName(tag));
            }
        }
        return tags;
    }

    @Override
    public List<User> generateMentions(String text) {
        Pattern mentionPattern = Pattern.compile("@(\\w+)");
        Matcher mat = mentionPattern.matcher(text);
        List<User> mentions= new ArrayList<>();
        while (mat.find()) {
            try {
                User user = userDAO.findByUsername(mat.group(1));
                mentions.add(user);
            } catch (NoResultException exception){
                 /*Add something to notify user and don't throw a exception which causes the tweet not to be posted
                   Because on twitter you also can use the @ without a valid user. Someone might want to send a ssh link and it would be retarded to make it crash.
                  */
            }
        }
        return mentions;
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
