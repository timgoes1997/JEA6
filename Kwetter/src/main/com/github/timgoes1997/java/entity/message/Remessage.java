package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("3")
public class Remessage extends ReplyMessage implements Serializable {

    private static final long serialVersionUID = -9015261934439105868L;

    @Column(name = "HASTEXT")
    private boolean hasText;

    public Remessage(){
        super();
    }

    public Remessage(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions, Message message){
        super(text, type, messager, date, tags, mentions, message);
        this.hasText = text.length() > 0;
    }

    public boolean isHasText() {
        return hasText;
    }
}
