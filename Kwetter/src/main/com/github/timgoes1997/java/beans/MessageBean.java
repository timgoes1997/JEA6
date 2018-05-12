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
import java.util.Enumeration;
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
    @UserTokenAuthorization()
    @Path("{id}")
    public Message getMessageByID(@Context ContainerRequestContext request, @PathParam("id") long id) {
        return messageUriBuilder.buildMessageUriLinks(request, uriInfo, messageService.getMessageByID(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization()
    @Path("{id}/replies")
    public List<ReplyMessage> getMessageRepliesByMessageID(@Context ContainerRequestContext request, @PathParam("id") long id) {
        return messageService.getMessageRepliesByMessageID(id)
                .stream()
                .map(m -> (messageUriBuilder.buildReplyMessageUriLinks(request, uriInfo, m)))
                .collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization()
    @Path("/user/{username}/{id}")
    public Message getMessageByUsernameAndID(@Context ContainerRequestContext request, @PathParam("username") String username, @PathParam("id") long id) {
        return messageUriBuilder.buildMessageUriLinks(request, uriInfo, messageService.getMessageByUsernameAndID(username, id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization()
    @Path("/user/{username}/messages")
    public List<Message> getProfileMessagesByUsername(@Context ContainerRequestContext request, @PathParam("username") String username) {
        return messageService.getProfileMessagesByUsername(username)
                .stream()
                .map(m -> (messageUriBuilder.buildMessageUriLinks(request, uriInfo, m)))
                .collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization(requiresUser = true,
            allowed = {UserRole.User, UserRole.Moderator, UserRole.Admin},
            onlySelf = true)
    @Path("create")
    public Message createMessage(@Context ContainerRequestContext request, @FormParam("message") String message, @FormParam("messageType") MessageType messageType) {
        return messageUriBuilder.buildMessageUriLinks(request, uriInfo, messageService.createMessage(request, message, messageType));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization(requiresUser = true,
            allowed = {UserRole.User, UserRole.Moderator, UserRole.Admin},
            onlySelf = true)
    @Path("{id}/reply")
    public ReplyMessage createReply(@Context ContainerRequestContext request, @PathParam("id") long messageID, @FormParam("text") String text) {
        return messageUriBuilder.buildReplyMessageUriLinks(request, uriInfo, messageService.createReplyMessage(request, messageID, text));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @UserTokenAuthorization(requiresUser = true,
            allowed = {UserRole.User, UserRole.Moderator, UserRole.Admin},
            onlySelf = true,
            onlySelfExceptions = {UserRole.Moderator, UserRole.Admin})
    @Path("{id}/remove")
    public Message removeMessage(@Context ContainerRequestContext request, @PathParam("id") long messageID) {
        return messageService.removeMessage(request, messageID);
    }

}
