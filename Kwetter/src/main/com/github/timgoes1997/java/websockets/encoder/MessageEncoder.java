package com.github.timgoes1997.java.websockets.encoder;

import com.github.timgoes1997.java.entity.message.Message;
import groovy.json.JsonBuilder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message object) throws EncodeException {
        return object.toJson().toString();
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
