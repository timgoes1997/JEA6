package com.github.timgoes1997.java.hateoas.builders;

import com.github.timgoes1997.java.beans.MessageBean;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.Remessage;
import com.github.timgoes1997.java.entity.message.ReplyMessage;
import com.github.timgoes1997.java.hateoas.Link;
import com.sun.research.ws.wadl.HTTPMethods;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;

/**
 * Eventuele extra laag in de toekomst is om de method annotation op te vragen uit de klasse.
 * De waarde uit te lezen en kijken of de huidige user ook die permissie heeft en is ingelogd etc.
 * En op basis daarvan ook links toevoegen. Als er dan bijvoorbeeld geen link voor remove is in angular hoef ik die knop überhaupt niet te genereren etc.
 */
public class MessageUriBuilder {

    public Remessage buildRemessageUriLinks(UriInfo uriInfo, Remessage remessage){
        return (Remessage) buildReplyMessageUriLinks(uriInfo, remessage);
    }

    public ReplyMessage buildReplyMessageUriLinks(UriInfo uriInfo, ReplyMessage replyMessage){
        replyMessage.setMessage(buildMessageUriLinks(uriInfo, replyMessage.getMessage()));
        return (ReplyMessage)buildMessageUriLinks(uriInfo, replyMessage);
    }

    public Message buildMessageUriLinks(UriInfo uriInfo, Message message){
        if(message.hasLinks()) return  message;
        message.addLink(getDirectUri(uriInfo, message));
        message.addLink(getRepliesUri(uriInfo, message));
        message.addLink(getCreateReplyUri(uriInfo, message));
        message.addLink(getRemoveUri(uriInfo, message));
        return message;
    }

    private Link getDirectUri(UriInfo uriInfo, Message message){
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(Long.toString(message.getId()))
                .build()
                .toString();
        return new Link(uri, "self", HttpMethod.GET);
    }

    private Link getRepliesUri(UriInfo uriInfo, Message message){
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(MessageBean.class, "getMessageRepliesByMessageID")
                .resolveTemplate("id", message.getId())
                .build()
                .toString();
        return new Link(uri, "replies", HttpMethod.GET);
    }

    private Link getCreateReplyUri(UriInfo uriInfo, Message message){
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(MessageBean.class, "createReply")
                .resolveTemplate("id", message.getId())
                .build()
                .toString();
        return new Link(uri, "reply", HttpMethod.POST);
    }

    private Link getRemoveUri(UriInfo uriInfo, Message message){
        String uri = uriInfo.getBaseUriBuilder()
                .path(MessageBean.class)
                .path(MessageBean.class, "removeMessage")
                .resolveTemplate("id", message.getId())
                .build()
                .toString();
        return new Link(uri, "remove", HttpMethod.DELETE);
    }
}
