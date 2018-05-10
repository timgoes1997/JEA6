package com.github.timgoes1997.java.web;

import com.github.timgoes1997.java.authentication.session.auth.SessionAuth;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.message.MessageType;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.interfaces.UserInterface;
import com.github.timgoes1997.java.services.ServiceHelper;
import com.github.timgoes1997.java.services.beans.interfaces.MessageService;
import com.github.timgoes1997.java.services.beans.interfaces.UserService;

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
import java.util.logging.Logger;

@Named
@SessionScoped
public class MessageManager implements Serializable {

    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    @Inject
    private MessageService messageService;

    @Inject
    private UserService userService;

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

    public boolean isCurrentUserModeratorAdmin(){
        FacesContext fc = FacesContext.getCurrentInstance();
        String userRequest = getUsernameParam(fc);
        User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
        if (user != null && userRequest == null) {
            return true;
        }
        if (user != null && userRequest != null) {
            User requestUser = userService.getByUsername(userRequest);
            return ServiceHelper.isCurrentUserOrModeratorOrAdmin(user, requestUser);
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

    public String viewMessage(Message message){
        this.currentMessage = message;
        return "viewMessage";
    }

    public String createTextMessage(){
        FacesContext fc = FacesContext.getCurrentInstance();
        User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
        if(user != null){
            messageService.createMessage(user, typingMessage , MessageType.Public );
            logger.info("created the following message: " + typingMessage);
            return "created";
        }else{
            return "error";
        }
    }

    public String removeMessage(Message message){
        FacesContext fc = FacesContext.getCurrentInstance();
        User user = (User) SessionAuth.getCurrentUser(getCurrentRequest(fc));
        if(user != null){
            messageService.removeMessage(user, message.getId());
        }
        return "removed";
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
