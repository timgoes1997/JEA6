package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("3")
@NamedQueries({
        @NamedQuery(name=Remessage.FIND_ALL,
                query="SELECT r FROM Remessage r"),
        @NamedQuery(name=Remessage.FIND_ALL_BY_TYPE,
                query="SELECT r FROM Remessage r WHERE r.discriminator=3"),
        @NamedQuery(name=Remessage.FIND_REMESSAGE_FOR_MESSAGE_ID,
                query="SELECT r FROM Remessage r WHERE r.discriminator=3 AND r.message.id = :id"),
})
public class Remessage extends ReplyMessage implements Serializable {
    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "Remessage.findAll";
    public static final String FIND_ALL_BY_TYPE = "Remessage.findRepliesByType";
    public static final String FIND_REMESSAGE_FOR_MESSAGE_ID = "Message.findRemessages";

    private static final long serialVersionUID = -9015261934439105868L;

    //======================
    //==      Fields      ==
    //======================


    @Column(name = "HASTEXT")
    private boolean hasText;

    public Remessage(){
        super();
        this.discriminator = 3;
    }

//    @Override
//    public int getDiscriminator() {
//        return this.getDiscriminator();
//    }

    public Remessage(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions, Message message){
        super(text, type, messager, date, tags, mentions, message);
        this.discriminator = 3;
        this.hasText = text.length() > 0;
    }

    public boolean isHasText() {
        return hasText;
    }
}
