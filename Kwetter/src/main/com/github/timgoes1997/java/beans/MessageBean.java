package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.authentication.token.interceptor.UserTokenAuthorization;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.hateoas.Link;
import com.github.timgoes1997.java.hateoas.builders.MessageUriBuilder;
import com.github.timgoes1997.java.services.beans.interfaces.MessageService;
import com.sun.research.ws.wadl.HTTPMethods;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("message")
public class MessageBean {

    @Inject
    private MessageService messageService;

    @Inject
    private MessageUriBuilder messageUriBuilder;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Message getMessageByID(@PathParam("id") long id) {
        return messageUriBuilder.buildMessageUriLinks(uriInfo, messageService.getMessageByID(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/replies")
    public List<ReplyMessage> getMessageRepliesByMessageID(@PathParam("id") long id) {
        return messageService.getMessageRepliesByMessageID(id)
                .stream()
                .map(m -> (messageUriBuilder.buildReplyMessageUriLinks(uriInfo, m)))
                .collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{username}/{id}")
    public Message getMessageByUsernameAndID(@PathParam("username") String username, @PathParam("id") long id) {
        return messageUriBuilder.buildMessageUriLinks(uriInfo, messageService.getMessageByUsernameAndID(username, id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user/{username}/messages")
    public List<Message> getProfileMessagesByUsername(@PathParam("username") String username) {
        return messageService.getProfileMessagesByUsername(username)
                .stream()
                .map(m -> (messageUriBuilder.buildMessageUriLinks(uriInfo, m)))
                .collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization({UserRole.User})
    @Path("create")
    public Message createMessage(@Context ContainerRequestContext request, @FormParam("message") String message, @FormParam("messageType") MessageType messageType) {
        return messageUriBuilder.buildMessageUriLinks(uriInfo, messageService.createMessage(request, message, messageType));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization({UserRole.User})
    @Path("{id}/reply")
    public ReplyMessage createReply(@Context ContainerRequestContext request, @PathParam("id") long messageID, @FormParam("text") String text) {
        return messageUriBuilder.buildReplyMessageUriLinks(uriInfo, messageService.createReplyMessage(request, messageID, text));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization({UserRole.User})
    @Path("{id}/remove")
    public Message removeMessage(@Context ContainerRequestContext request, @PathParam("id") long messageID) {
        return messageService.removeMessage(request, messageID);
    }

}
