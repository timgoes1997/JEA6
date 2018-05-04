package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.interceptor.UserAuthorization;
import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@Path("message")
public class MessageBean {

    @Inject
    private MessageDAO messageDAO;

    @Inject
    private UserDAO userDAO;

    @Inject
    private Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getMessageByID(@PathParam("id") long id) {
        try {
            Message message = messageDAO.find(id);
            return Response.ok().entity(message).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/replies")
    public Response getMessageRepliesByMessageID(@PathParam("id") long id) {
        try {
            if(!messageDAO.exists(id)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Message message = messageDAO.find(id);
            List<ReplyMessage> replyMessages = messageDAO.getMessageReplies(message);
            return Response.ok().entity(replyMessages).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("create")
    public Response createMessage(@Context ContainerRequestContext request, @FormParam("message") String message, @FormParam("messageType")MessageType messageType) {
        try {
            User currentUser = (User)request.getProperty(Constants.USER_REQUEST_STRING);
            List<Tag> tags = messageDAO.generateTags(message);
            List<User> mentions = messageDAO.generateMentions(message);

            InitialMessage initialMessage = new InitialMessage(message, messageType, currentUser, new Date(), tags, mentions);
            messageDAO.create(initialMessage);

            Message created = messageDAO.find(initialMessage.getId());
            return Response.ok().entity(created).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("{id}/reply")
    public Response createReply(@Context ContainerRequestContext request, @PathParam("id") long messageID, @FormParam("text") String text, @FormParam("messageType")MessageType messageType) {
        try {
            if(!messageDAO.exists(messageID)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Message message = messageDAO.find(messageID);
            User currentUser = (User)request.getProperty(Constants.USER_REQUEST_STRING);
            List<Tag> tags = messageDAO.generateTags(text);
            List<User> mentions = messageDAO.generateMentions(text);
            ReplyMessage reply = new ReplyMessage(text, messageType, currentUser, new Date(), tags, mentions, message);
            messageDAO.create(reply);

            return Response.ok().entity(reply).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("{id}/remove")
    public Response removeMessage(@Context ContainerRequestContext request, @PathParam("id") long messageID) {
        try {
            if(!messageDAO.exists(messageID)){
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Message message = messageDAO.find(messageID);
            User currentUser = (User)request.getProperty(Constants.USER_REQUEST_STRING);
            if(!(message.getMessager().getId() == currentUser.getId()
                    || currentUser.getRole() == UserRole.Moderator
                    || currentUser.getRole() == UserRole.Admin)){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            messageDAO.remove(message);

            return Response.ok().build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

}
