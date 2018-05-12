package com.github.timgoes1997.java.websockets;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.websockets.encoder.MessageEncoder;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

@ServerEndpoint(value = "/listener/user/{username}",
                encoders = MessageEncoder.class)
public class UserMessageEndPoint {

    @Inject
    private Logger logger;

    private Session session;
    private String username;
    private static Set<UserMessageEndPoint> userMessageEndPoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        logger.info("hello");
        this.session = session;
        userMessageEndPoints.add(this);
        this.username = username;
        users.put(session.getId(), username);
        //check if username exists otherwise disconect
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Received: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        userMessageEndPoints.remove(this);
        users.remove(session.getId());
        this.username = "";
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    public void informConnectedSessions(@Observes @MessageInformer Message msg) {
        logger.info("Informing users for msg" + msg.getId());
        userMessageEndPoints.forEach(endPoint -> {
            synchronized (endPoint) {
                try {
                    if(endPoint.username.equals(msg.getMessager().getUsername())){
                        endPoint.session.getBasicRemote().sendObject(msg);
                    }
                } catch (IOException e) {
                    logger.severe("Couldn't send message to the following endpoint session: " + endPoint.session.getId());
                } catch (EncodeException e) {
                    logger.severe("Failed to encode the message to the following endpoint session: " + endPoint.session.getId());
                }
            }
        });
    }
}
