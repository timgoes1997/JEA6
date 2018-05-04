package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import java.util.Date;
import java.util.List;

public interface MessageDAO {
    void clearUserFromLikesAndMentions(User user);
    void create(Message message);

    Message find(long id);
    List<Message> findMessagesByMention(User user);
    List<Message> findMessagesByLikes(User user);
    List<Message> findMessagesByUser(User user);
    List<Message> findMessagesByUser(User user, int firstResult, int maxResults);

    boolean exists(long id);

    List<User> generateMentions(String text);
    List<Tag> generateTags(String text);
    Date getCurrentLocalDateTime();
    List<Remessage> getMessageRemessages(Message message);
    List<Remessage> getMessageRemessages(Message message, int firstResult, int maxResults);
    Long getMessageLikes(Message message);
    List<ReplyMessage> getMessageReplies(Message message);
    List<ReplyMessage> getMessageReplies(Message message, int firstResult, int maxResults);
    List<Message> getAllMessages();

    void likeMessage(Message message, User user) throws Exception;

    void nullMessageData(Message message);

    void remove(Message message);
}
