package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.interceptor.UserAuthorization;
import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.sun.deploy.net.HttpRequest;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    private Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getMessageByID(@PathParam("id") int id) {
        try {
            Message message = messageDAO.find(id);
            return Response.ok().entity(message).build();
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
            User currentUser = (User)request.getProperty("user");
            List<Tag> tags = messageDAO.generateTags(message);
            List<User> mentions = messageDAO.generateMentions(message);

            InitialMessage initialMessage = new InitialMessage(message, messageType, currentUser, new Date(), tags, mentions);
            messageDAO.create(initialMessage);

            return Response.ok().entity(initialMessage).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }


}
