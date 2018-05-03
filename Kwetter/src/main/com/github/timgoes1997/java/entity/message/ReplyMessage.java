package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

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
    private Message message;

    public ReplyMessage(){
        super();
    }

    public ReplyMessage(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions, Message message){
        super(text, type, messager, date, tags, mentions);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
