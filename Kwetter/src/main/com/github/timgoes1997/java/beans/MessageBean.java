package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.interceptor.UserAuthorization;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.services.beans.interfaces.MessageService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("message")
public class MessageBean {

    @Inject
    private MessageService messageService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Message getMessageByID(@PathParam("id") long id) {
        return messageService.getMessageByID(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/replies")
    public List<ReplyMessage> getMessageRepliesByMessageID(@PathParam("id") long id) {
        return messageService.getMessageRepliesByMessageID(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{username}/{id}")
    public Message getMessageByUsernameAndID(@PathParam("username") String username, @PathParam("id") long id){
        return messageService.getMessageByUsernameAndID(username, id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{username}/messages")
    public List<Message> getProfileMessagesByUsername(@PathParam("username") String username){
        return messageService.getProfileMessagesByUsername(username);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("create")
    public Message createMessage(@Context ContainerRequestContext request, @FormParam("message") String message, @FormParam("messageType")MessageType messageType) {
        return messageService.createMessage(request, message, messageType);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("{id}/reply")
    public ReplyMessage createReply(@Context ContainerRequestContext request, @PathParam("id") long messageID, @FormParam("text") String text) {
        return messageService.createReplyMessage(request, messageID, text);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @UserAuthorization({UserRole.User})
    @Path("{id}/remove")
    public Message removeMessage(@Context ContainerRequestContext request, @PathParam("id") long messageID) {
        return messageService.removeMessage(request, messageID);
    }

}
