package com.github.timgoes1997.java.services.beans.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.List;

public interface MessageService {
    Message getMessageByID(long id);
    Message getMessageByUsernameAndID(String username, long id);
    List<ReplyMessage> getMessageRepliesByMessageID(long id);
    List<Message> getProfileMessagesByUsername(String username);

    Message createMessage(ContainerRequestContext requestContext, String text, MessageType messageType);
    Message createMessage(User user, String text, MessageType messageType);
    ReplyMessage createReplyMessage(ContainerRequestContext requestContext, long messageID, String text);
    ReplyMessage createReplyMessage(User user, long messageID, String text);

    Message removeMessage(ContainerRequestContext requestContext, long messageID);
    Message removeMessage(User user, long messageID);

    long getMessageLikes(long messageID);
    long addMessageLike(ContainerRequestContext requestContext, long messageID);
    long addMessageLike(User user, long messageID);
    boolean hasLiked(ContainerRequestContext requestContext, long messageID);
    boolean hasLiked(User user, long messageID);
    long removeMessageLike(ContainerRequestContext requestContext, long messageID);
    long removeMessageLike(User user, long messageID);

    long getReplyCount(long messageID);
    long getRemessageCount(long messageID);
    void basicMessageValidation(String text);
}
