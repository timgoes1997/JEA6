package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
        @NamedQuery(name="ReplyMessage.findReplies",
                query="SELECT r FROM ReplyMessage r " +
                        "WHERE TYPE(r) IN :class AND r.message.id = :id"), */

@Entity
@DiscriminatorValue("2")
@NamedQueries({
        @NamedQuery(name=ReplyMessage.FIND_ALL,
                query="SELECT r FROM ReplyMessage r"),
        @NamedQuery(name=ReplyMessage.FIND_ALL_BY_TYPE,
                query="SELECT r FROM ReplyMessage r WHERE r.discriminator=2"),
        @NamedQuery(name=ReplyMessage.FIND_REPLY_FOR_MESSAGE_ID,
                query="SELECT r FROM ReplyMessage r WHERE r.discriminator=2 AND r.message.id = :id"),
})
public class ReplyMessage extends Message implements Serializable {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "ReplyMessage.findAll";
    public static final String FIND_ALL_BY_TYPE = "ReplyMessage.findRepliesByType";
    public static final String FIND_REPLY_FOR_MESSAGE_ID = "Message.findReplies";

    private static final long serialVersionUID = 6864653489060895368L;

    //======================
    //==      Fields      ==
    //======================

    @OneToOne
    @JoinColumn(name = "MESSAGE")
    protected Message message;

    public ReplyMessage(){
        super();
        this.discriminator = 2;
    }

//    @Override
//    public int getDiscriminator() {
//        return this.getDiscriminator();
//    }

    public ReplyMessage(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions, Message message){
        super(text, type, messager, date, tags, mentions);
        this.message = message;
        this.discriminator = 2;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder( super.toJson());
        builder.remove("discriminator");
        builder.add("discriminator", String.valueOf(this.discriminator));
        builder.add("message", this.message.toJson());
        return builder.build();
    }
}
