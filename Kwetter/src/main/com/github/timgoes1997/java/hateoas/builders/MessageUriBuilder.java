package com.github.timgoes1997.java.hateoas.builders;

import com.github.timgoes1997.java.authentication.Constants;
import com.github.timgoes1997.java.authentication.token.interceptor.UserTokenAuthorization;
import com.github.timgoes1997.java.beans.MessageBean;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import com.github.timgoes1997.java.hateoas.Link;
import com.sun.research.ws.wadl.HTTPMethods;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Eventuele extra laag in de toekomst is om de method annotation op te vragen uit de klasse.
 * De waarde uit te lezen en kijken of de huidige user ook die permissie heeft en is ingelogd etc.
 * En op basis daarvan ook links toevoegen. Als er dan bijvoorbeeld geen link voor remove is in angular hoef ik die knop überhaupt niet te genereren etc.
 */
public class MessageUriBuilder {

    public Remessage buildRemessageUriLinks(ContainerRequestContext requestContext, UriInfo uriInfo, Remessage remessage) {
        return (Remessage) buildReplyMessageUriLinks(requestContext, uriInfo, remessage);
    }

    public ReplyMessage buildReplyMessageUriLinks(ContainerRequestContext requestContext, UriInfo uriInfo, ReplyMessage replyMessage) {
        replyMessage.setMessage(buildMessageUriLinks(requestContext, uriInfo, replyMessage.getMessage()));
        return (ReplyMessage) buildMessageUriLinks(requestContext, uriInfo, replyMessage);
    }

    public Message buildMessageUriLinks(ContainerRequestContext requestContext, UriInfo uriInfo, Message message) {
        if (message.hasLinks()) message.setLinks(new ArrayList<>());
        message.addLink(getDirectUri(uriInfo, message));
        message.addLink(getRepliesUri(uriInfo, message));
        message.addLink(getCreateReplyUri(requestContext, uriInfo, message));
        message.addLink(getRemoveUri(requestContext, uriInfo, message));
        message.addLink(getAddLikeURI(requestContext,uriInfo, message));
        message.addLink(getRemoveLikeURI(requestContext,uriInfo, message));
        message.addLink(getHasLikedURI(requestContext,uriInfo, message));
        message.addLink(getMessageLikes(requestContext,uriInfo, message));
        message.addLink(getMessageRemessagesCount(requestContext,uriInfo, message));
        message.addLink(getMessageRepliesCount(requestContext,uriInfo, message));
        return message;
    }

    private Link getDirectUri(UriInfo uriInfo, Message message) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(Long.toString(message.getId()))
                .build()
                .toString();
        return new Link(uri, "self", HttpMethod.GET);
    }

    private Link getRepliesUri(UriInfo uriInfo, Message message) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(MessageBean.class, "getMessageRepliesByMessageID")
                .resolveTemplate("id", message.getId())
                .build()
                .toString();
        return new Link(uri, "replies", HttpMethod.GET);
    }

    private Link getCreateReplyUri(ContainerRequestContext requestContext, UriInfo uriInfo, Message message) {
        try {
            Method method = MessageBean.class.getMethod("createReply", ContainerRequestContext.class, long.class, String.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "reply", HttpMethod.POST);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Link getRemoveUri(ContainerRequestContext requestContext, UriInfo uriInfo, Message message) {
        try {
            Method method = MessageBean.class.getMethod("removeMessage", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "remove", HttpMethod.DELETE);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getAddLikeURI(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("likeMessage", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "addLike", HttpMethod.POST);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getRemoveLikeURI(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("removeMessageLike", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "removelike", HttpMethod.DELETE);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getHasLikedURI(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("hasLikedMessage", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "hasliked", HttpMethod.GET);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getMessageLikes(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("getMessageLikes", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "likes", HttpMethod.GET);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getMessageRepliesCount(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("getMessageReplyCountByMessageID", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "repliescount", HttpMethod.GET);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Link getMessageRemessagesCount(ContainerRequestContext requestContext, UriInfo uriInfo, Message message){
        try{
            Method method = MessageBean.class.getMethod("getMessageRemessagesCountByMessageID", ContainerRequestContext.class, long.class);
            UserTokenAuthorization tokenAuthorization = method.getAnnotation(UserTokenAuthorization.class);
            if (!hasPermission(requestContext, tokenAuthorization, message)) return null;

            String uri = uriInfo.getBaseUriBuilder()
                    .path(MessageBean.class)
                    .path(method)
                    .resolveTemplate("id", message.getId())
                    .build()
                    .toString();
            return new Link(uri, "remessagescount", HttpMethod.GET);
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasPermission(ContainerRequestContext requestContext, UserTokenAuthorization annotation, Message message) {
        User currentUser = (User) requestContext.getProperty(Constants.USER_REQUEST_STRING);
        if (annotation == null) return true; //check wether there is a annotation;
        if (!annotation.requiresUser()) return true; //check if it requires a user;
        if (!(hasRoles(currentUser, annotation.allowed()))) return false; //check if it has roles;
        if (!annotation.onlySelf()) return true;
        if (hasMessager(currentUser, message)) return true;
        if (hasRoles(currentUser, annotation.onlySelfExceptions())) return true;
        return false;
    }

    public boolean hasRoles(User user, UserRole[] roles) {
        if(user == null) return false;
        if (roles == null || roles.length == 0) return true;
        for (UserRole role : roles) {
            if (role.equals(user.getRole())) return true;
        }
        return false;
    }

    public boolean hasMessager(User user, Message message) {
        return (user != null) && user.getId() == message.getMessager().getId();
    }

}
