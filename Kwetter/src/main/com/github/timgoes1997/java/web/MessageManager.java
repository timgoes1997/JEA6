package com.github.timgoes1997.java.web;

import com.github.timgoes1997.java.authentication.session.auth.SessionAuth;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.interfaces.UserInterface;
import com.github.timgoes1997.java.services.beans.interfaces.MessageService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class MessageManager implements Serializable {

    @Inject
    private MessageService messageService;

    private Message currentMessage;

    private String typingMessage;


    public boolean isCurrentUser() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String userRequest = getUsernameParam(fc);
        User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
        if (user != null && userRequest == null) {
            return true;
        }
        if (user != null && userRequest != null) {
            return user.getUsername() == userRequest;
        }
        return false;
    }

    public List<Message> retrieveMessages() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String userRequest = getUsernameParam(fc);

        if (userRequest != null) {
            return messageService.getProfileMessagesByUsername(userRequest);
        } else {
            User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
            if (user == null) {
                return null;
            }
            return messageService.getProfileMessagesByUsername(user.getUsername());
        }
    }

    public String createTextMessage(){
        FacesContext fc = FacesContext.getCurrentInstance();
        User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
        if(user != null){
            messageService.createMessage(user, typingMessage , MessageType.Public );
            return "created";
        }else{
            return "error";
        }
    }

    public String getUsernameParam(FacesContext context) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        return params.get("username");
    }

    private HttpServletRequest getCurrentRequest(FacesContext context) {
        return (HttpServletRequest) context.getExternalContext().getRequest();
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(Message currentMessage) {
        this.currentMessage = currentMessage;
    }

    public String getTypingMessage() {
        return typingMessage;
    }

    public void setTypingMessage(String typingMessage) {
        this.typingMessage = typingMessage;
    }
}
