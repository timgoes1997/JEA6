package com.github.timgoes1997.java.websockets;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

@ServerEndpoint(value = "/listener/user/{username}")
public class UserMessageEndPoint {

    @Inject
    private Logger logger;

    private Session session;
    private static Set<UserMessageEndPoint> userMessageEndPoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        this.session = session;
        userMessageEndPoints.add(this);
        users.put(session.getId(), username);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Received: " + message);
    }

    @OnClose
    public void onClose(Session session){
        userMessageEndPoints.remove(this);
        users.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable){

    }
}
