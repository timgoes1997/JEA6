package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import com.github.timgoes1997.java.entity.message.Message;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
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
            return Response.status(Response.Status.OK).entity(message).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e).build();
        }
    }

}
