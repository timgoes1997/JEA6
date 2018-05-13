package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import java.util.Date;
import java.util.List;

public interface MessageDAO {
    boolean addLike(Message message, User user);
    boolean removeLike(Message message, User user);
    boolean hasLiked(Message message, User user);

    void clearUserFromLikesAndMentions(User user);
    void create(Message message);

    boolean exists(long id);
    Message find(long id);
    Message find(String username, long id);
    List<Message> findProfileMessages(String username);
    List<Message> findProfileMessagesWithReplies(String username);
    List<Message> findMessagesByMention(User user);
    List<Message> findMessagesByLikes(User user);
    List<Message> findMessagesByTag(Tag tag);
    List<Message> findMessagesByUser(User user);
    List<Message> findMessagesByUser(User user, int firstResult, int maxResults);
    List<User> generateMentions(String text);
    List<Tag> generateTags(String text);
    Date getCurrentLocalDateTime();
    List<Message> getUserTimeLine(User user);
    List<Remessage> getMessageRemessages(Message message);
    List<Remessage> getMessageRemessages(Message message, int firstResult, int maxResults);
    Long getMessageLikes(Message message);
    List<ReplyMessage> getMessageReplies(Message message);
    List<ReplyMessage> getMessageReplies(Message message, int firstResult, int maxResults);
    List<Message> getAllMessages();

    long getRemessageCount(Message message);
    long getReplyCount(Message message);

    long getAmountOfLikes(Message message);

    void nullMessageData(Message message);

    void remove(Message message);
}
