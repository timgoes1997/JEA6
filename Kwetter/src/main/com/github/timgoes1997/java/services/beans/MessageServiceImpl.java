package com.github.timgoes1997.java.services.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.services.ServiceHelper;
import com.github.timgoes1997.java.services.beans.interfaces.MessageService;
import com.github.timgoes1997.java.websockets.MessageInformer;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.Date;
import java.util.List;

@Stateless
public class MessageServiceImpl implements MessageService {

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    @MessageInformer
    Event<Message> messageEvent;

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
            if (!userDAO.exists(username) || !messageDAO.exists(id)) {
                throw new NotFoundException("Couldn't find given username or id!");
            }

            Message message = messageDAO.find(username, id);
            return message;
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new NotFoundException("Couldn't find a message for given username and id!");
        }
    }

    @Override
    public List<ReplyMessage> getMessageRepliesByMessageID(long id) {
        try {
            if (!messageDAO.exists(id)) {
                throw new NotFoundException("Couldn't find message for given id");
            }
            Message message = messageDAO.find(id);
            return messageDAO.getMessageReplies(message);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't find any replymessages for given id");
        }
    }

    @Override
    public List<Message> getProfileMessagesByUsername(String username) {
        try {
            if (!userDAO.exists(username)) {
                throw new NotFoundException("Coudln't find given username");
            }
            return messageDAO.findProfileMessages(username);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new NotFoundException("Could't find any profile messages for username");
        }
    }

    @Override
    public Message createMessage(ContainerRequestContext requestContext, String text, MessageType messageType) {
        try {
            return createMessage((User) requestContext.getProperty(Constants.USER_REQUEST_STRING), text, messageType);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't retrieve message after creating, it might not have been created by the server yet!");
        }
    }

    @Override
    public Message createMessage(User user, String text, MessageType messageType) {
        try {
            basicMessageValidation(text);

            InitialMessage initialMessage = new InitialMessage(text,
                    messageType, user, new Date(),
                    messageDAO.generateTags(text), messageDAO.generateMentions(text));
            messageDAO.create(initialMessage);
            messageEvent.fire(initialMessage);
            return initialMessage;
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Couldn't retrieve message after creating, it might not have been created by the server yet!");
        }
    }

    @Override
    public ReplyMessage createReplyMessage(ContainerRequestContext requestContext, long messageID, String text) {
        try {
            return createReplyMessage((User) requestContext.getProperty(Constants.USER_REQUEST_STRING),
                    messageID, text);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while creating your " +
                    "reply message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public ReplyMessage createReplyMessage(User user, long messageID, String text) {
        try {
            basicMessageValidation(text);

            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to reply to doesn't exist");
            }

            Message message = messageDAO.find(messageID);
            ReplyMessage reply = new ReplyMessage(text,
                    message.getType(), user, new Date(),
                    messageDAO.generateTags(text), messageDAO.generateMentions(text),
                    message);
            messageDAO.create(reply);
            messageEvent.fire(reply);
            return reply;
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while creating your " +
                    "reply message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public Message removeMessage(ContainerRequestContext requestContext, long messageID) {
        try {
            return removeMessage((User) requestContext.getProperty(Constants.USER_REQUEST_STRING), messageID);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public Message removeMessage(User user, long messageID) {
        try {
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to remove doesn't exist");
            }

            Message message = messageDAO.find(messageID);
            if (!ServiceHelper.isCurrentUserOrModeratorOrAdmin(user, message.getMessager())) {
                throw new NotAuthorizedException("User not authorized to remove message");
            }

            messageDAO.remove(message);
            return message;
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long getMessageLikes(long messageID) {
        try {
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to get the likes from doesn't exist");
            }

            Message message = messageDAO.find(messageID);
            return messageDAO.getMessageLikes(message);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong retrieving the message likes");
        }
    }

    @Override
    public long addMessageLike(ContainerRequestContext requestContext, long messageID) {
        try {
            return addMessageLike((User) requestContext.getProperty(Constants.USER_REQUEST_STRING), messageID);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long addMessageLike(User user, long messageID) {
        try {
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to get the likes from doesn't exist");
            }
            Message message = messageDAO.find(messageID);

            if (!messageDAO.addLike(message, user)) {
                throw new ForbiddenException("You already liked the message!");
            }

            return messageDAO.getAmountOfLikes(message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong with liking the message");
        }
    }

    @Override
    public boolean hasLiked(ContainerRequestContext requestContext, long messageID) {
        try {
            return hasLiked((User) requestContext.getProperty(Constants.USER_REQUEST_STRING), messageID);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public boolean hasLiked(User user, long messageID) {
        try {
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to check for wether you had liked it doesn't exist!");
            }
            Message message = messageDAO.find(messageID);

            return messageDAO.hasLiked(message, user);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long removeMessageLike(ContainerRequestContext requestContext, long messageID) {
        try {
            return removeMessageLike((User) requestContext.getProperty(Constants.USER_REQUEST_STRING), messageID);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long removeMessageLike(User user, long messageID) {
        try {
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to check for wether you had liked it doesn't exist!");
            }
            Message message = messageDAO.find(messageID);

            if (!messageDAO.removeLike(message, user)) {
                throw new ForbiddenException("You haven't liked the message!");
            }

            return messageDAO.getAmountOfLikes(message);
        } catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long getReplyCount(long messageID) {
        try{
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to check for wether you had liked it doesn't exist!");
            }
            Message message = messageDAO.find(messageID);
            return messageDAO.getReplyCount(message);
        }catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public long getRemessageCount(long messageID) {
        try{
            if (!messageDAO.exists(messageID)) {
                throw new NotFoundException("Message you tried to check for wether you had liked it doesn't exist!");
            }
            Message message = messageDAO.find(messageID);
            return messageDAO.getRemessageCount(message);
        }catch (Exception e) {
            ServiceHelper.checkIfWebApplicationExceptionAndThrow(e);
            throw new InternalServerErrorException("Something went wrong while removing your " +
                    "message due to a internal server exception, please contact a administrator :(");
        }
    }

    @Override
    public void basicMessageValidation(String text) {
        if (text.length() < 1 || text.length() > 240)
            throw new NotAcceptableException("Message needs to be between 1 and 240 characters");
    }
}
