package com.github.timgoes1997.java.services.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.beans.interfaces.MessageServiceInterface;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

public class MessageService implements MessageServiceInterface {

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private UserDAO userDAO;

    @Override
    public Message getMessageByID(long id) {
        try {
            return messageDAO.find(id);
        } catch (Exception e) {
            throw new NotFoundException("Couldn't find message for given id!");
        }
    }

    @Override
    public Message getMessageByUsernameAndID(String username, long id) {
        try {
            if(!userDAO.exists(username) || !messageDAO.exists(id)){
                throw new NotFoundException("Couldn't find given username or id!");
            }

            Message message = messageDAO.find(username, id);
            return message;
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new NotFoundException("Couldn't find a message for given username and id!");
        }
    }

    @Override
    public List<ReplyMessage> getMessageRepliesByMessageID(long id) {
        try {
            if(!messageDAO.exists(id)){
                throw new NotFoundException("Couldn't find message for given id");
            }
            Message message = messageDAO.find(id);
            return messageDAO.getMessageReplies(message);
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't find any replymessages for given id");
        }
    }

    @Override
    public List<Message> getProfileMessagesByUsername(String username) {
        try {
            if(!userDAO.exists(username)){
                throw new NotFoundException("Coudln't find given username");
            }
            return messageDAO.findProfileMessages(username);
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new NotFoundException("Could't find any profile messages for username");
        }
    }

    @Override
    public Message createMessage(ContainerRequestContext requestContext, String text, MessageType messageType) {
        try {
            basicMessageValidation(text);

            User currentUser = (User)requestContext.getProperty(Constants.USER_REQUEST_STRING);
            InitialMessage initialMessage = new InitialMessage(text,
                    messageType, currentUser, new Date(),
                    messageDAO.generateTags(text), messageDAO.generateMentions(text));
            messageDAO.create(initialMessage);

            return messageDAO.find(initialMessage.getId());
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't retrieve message after creating, it might not have been created by the server yet!");
        }
    }

    @Override
    public ReplyMessage createReplyMessage(ContainerRequestContext requestContext, long messageID, String text) {
        try {
            basicMessageValidation(text);

            if(!messageDAO.exists(messageID)){
                throw new NotFoundException("Message you tried to reply to doesn't exist");
            }

            Message message = messageDAO.find(messageID);
            User currentUser = (User)requestContext.getProperty(Constants.USER_REQUEST_STRING);
            ReplyMessage reply = new ReplyMessage(text,
                    message.getType(), currentUser, new Date(),
                    messageDAO.generateTags(text), messageDAO.generateMentions(text),
                    message);
            messageDAO.create(reply);

            return reply;
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while creating your " +
                    "reply message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public Message removeMessage(ContainerRequestContext requestContext, long messageID) {
        try {
            if(!messageDAO.exists(messageID)){
                throw new NotFoundException("Message you tried to remove doesn't exist");
            }

            Message message = messageDAO.find(messageID);
            User currentUser = (User)requestContext.getProperty(Constants.USER_REQUEST_STRING);
            if(!(message.getMessager().getId() == currentUser.getId()
                    || currentUser.getRole() == UserRole.Moderator
                    || currentUser.getRole() == UserRole.Admin)){
                throw new NotAuthorizedException("User not authorized to remove message");
            }
            messageDAO.remove(message);

            return message;
        } catch (Exception e) {
            checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public void basicMessageValidation(String text) {
        if(text.length() < 1 || text.length() > 240)
            throw new NotAcceptableException("Message needs to be between 1 and 240 characters");
    }

    @Override
    public void checkIfWebApplicationExceptionAndThrow(Exception e) throws WebApplicationException {
        if(e instanceof WebApplicationException){
            throw (WebApplicationException)e;
        }
    }
}
