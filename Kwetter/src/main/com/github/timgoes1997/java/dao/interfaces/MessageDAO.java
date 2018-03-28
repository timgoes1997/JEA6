package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import java.util.Date;
import java.util.List;

public interface MessageDAO {
    void create(Message message);

    void remove(Message message);

    Message find(long id);

    List<Message> findMessagesByUser(User user);

    List<ReplyMessage> getMessageReplies(Message message);

    List<Remessage> getMessageRemessages(Message message);

    Long getMessageLikes(Message message);

    Date getCurrentLocalDateTime();

    List<Tag> generateTags(String text);

    List<User> generateMentions(String text);
}
