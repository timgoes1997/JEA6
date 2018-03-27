package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("2")
@NamedQueries({
        @NamedQuery(name="ReplyMessage.findAll",
                query="SELECT r FROM ReplyMessage r"),
        @NamedQuery(name="Message.findReplies",
                query="SELECT r FROM ReplyMessage r " +
                        "WHERE TYPE(r) IN :class AND r.message.id = :id"),
        @NamedQuery(name="ReplyMessage.findRepliesByType",
                query="SELECT r FROM ReplyMessage r " +
                        "WHERE r.type=2 AND r.message.id = :id"),
})
public class ReplyMessage extends Message implements Serializable {

    private static final long serialVersionUID = 6864653489060895368L;

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
