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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
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

    @Inject
    private Logger logger;

    @Override
    public void create(Message message) {
        em.persist(message);
        em.flush();
        User user = message.getMessager();
        user.getMessages().add(message);
        userDAO.edit(user);
    }

    @Override
    public void remove(Message message) {
        logger.info("Removing replies from tweet " + String.valueOf(message.getId()));

        List<ReplyMessage> replyMessages = getMessageReplies(message);
        for (ReplyMessage rm : replyMessages) {
            rm.setMessage(null);
            em.merge(rm);
            logger.info("Reply from " + rm.getMessager().getUsername() + " removed from message " + String.valueOf(message.getId()));
        }
        em.flush();
        logger.info("Removing remessages from tweet " + String.valueOf(message.getId()));

        List<Remessage> remessages = getMessageRemessages(message);
        for (Remessage r : remessages) {
            r.setMessage(null);
            em.merge(r);
            logger.info("Remessage from " + r.getMessager().toString() + " removed from message " + String.valueOf(message.getId()));
        }
        em.flush();

        if (userDAO.removeUserMessage(message)) {
            logger.info("Succesfully removed from user messages");
        }

        logger.info("Removing tweet" + String.valueOf(message.getId()));
        em.remove(message);
        logger.info("Succesfully removed tweet");
    }

    @Override
    public void nullMessageData(Message message) {
        message.setMessager(null);
        message.setMentions(null);
        message.setLikes(null);
        em.merge(message);
    }

    @Override
    public void clearUserFromLikesAndMentions(User user) {
        List<Message> mentions = findMessagesByMention(user);
        for (Message m : mentions) {
            m.getMentions().remove(user);
        }

        List<Message> likes = findMessagesByLikes(user);
        for (Message m : likes) {
            m.getLikes().remove(user);
        }
    }

    @Override
    public Message find(long id) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_ID, Message.class);
        return query.setParameter("id", id).getSingleResult();
    }

    @Override
    public Message find(String username, long id) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_USER_ID, Message.class);
        return query.setParameter("id", id).setParameter("name", username ).getSingleResult();
    }

    @Override
    public List<Message> findProfileMessages(String username) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_MESSAGES, Message.class);
        return query.getResultList();
    }

    @Override
    public List<Message> findMessagesByMention(User user) {
        return em.createNativeQuery("SELECT * " +
                "FROM MESSAGE WHERE ID " +
                "IN (SELECT MESSAGE_ID m_ID FROM MESSAGE_MENTIONS " +
                "WHERE MENTION_ACCOUNT_ID = ?1)", Message.class)
                .setParameter(1, user.getId()).getResultList();
    }

    @Override
    public List<Message> findMessagesByLikes(User user) {
        return em.createNativeQuery("SELECT * FROM MESSAGE " +
                "WHERE ID IN (SELECT MESSAGE_ID m_ID " +
                "FROM MESSAGE_LIKES WHERE LIKED_ACCOUNT_ID = ?1)", Message.class)
                .setParameter(1, user.getId()).getResultList();
    }

    @Override
    public List<Message> findMessagesByUser(User user) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_USER, Message.class);
        return query.setParameter("id", user.getId()).getResultList();
    }

    @Override
    public List<Message> findMessagesByUser(User user, int firstResult, int maxResults) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_USER, Message.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", user.getId()).getResultList();
    }

    @Override
    public boolean exists(long id) {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_ID, Message.class);
        return query.setParameter("id", id).getResultList().size() > 0;
    }

    @Override
    public List<Message> getAllMessages() {
        TypedQuery<Message> query =
                em.createNamedQuery(Message.FIND_ALL, Message.class);
        return query.getResultList();
    }

    @Override
    public void likeMessage(Message message, User user) throws Exception {
        Message m = message;
        if (m.getLikes().contains(user)) {
            throw new Exception("This user has already liked this message");
        }
        m.getLikes().add(user);
        em.merge(m);
    }

    @Override
    public List<ReplyMessage> getMessageReplies(Message message) {
        TypedQuery<ReplyMessage> query =
                em.createNamedQuery(ReplyMessage.FIND_REPLY_FOR_MESSAGE_ID, ReplyMessage.class);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<ReplyMessage> getMessageReplies(Message message, int firstResult, int maxResults) {
        TypedQuery<ReplyMessage> query =
                em.createNamedQuery(ReplyMessage.FIND_REPLY_FOR_MESSAGE_ID, ReplyMessage.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<Remessage> getMessageRemessages(Message message) {
        TypedQuery<Remessage> query =
                em.createNamedQuery(Remessage.FIND_REMESSAGE_FOR_MESSAGE_ID, Remessage.class);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public List<Remessage> getMessageRemessages(Message message, int firstResult, int maxResults) {
        TypedQuery<Remessage> query =
                em.createNamedQuery(Remessage.FIND_REMESSAGE_FOR_MESSAGE_ID, Remessage.class).setFirstResult(firstResult).setMaxResults(maxResults);
        return query.setParameter("id", message.getId()).getResultList();
    }

    @Override
    public Long getMessageLikes(Message message) {
        return ((Number) em.createNamedQuery(Message.GET_LIKES_BY_MESSAGE).setParameter("id", message.getId()).getSingleResult()).longValue();
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
        List<Tag> tags = new ArrayList<>();
        while (mat.find()) {
            String tag = mat.group(1);
            if (tagDAO.hasTag(tag)) {
                tags.add(tagDAO.findTagByName(tag));
            } else {
                Tag newTag = new Tag(tag);
                tagDAO.create(newTag);
                tags.add(tagDAO.findTagByName(tag));
            }
        }
        return tags;
    }

    @Override
    public List<User> generateMentions(String text) {
        Pattern mentionPattern = Pattern.compile("@(\\w+)");
        Matcher mat = mentionPattern.matcher(text);
        List<User> mentions = new ArrayList<>();
        while (mat.find()) {
            try {
                User user = userDAO.findByUsername(mat.group(1).toLowerCase());
                if (!mentions.contains(user)) mentions.add(user);
            } catch (NoResultException exception) {
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

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public void setLogger() {
        this.logger = Logger.getLogger(MessageDAOImpl.class.getName());
    }
}
