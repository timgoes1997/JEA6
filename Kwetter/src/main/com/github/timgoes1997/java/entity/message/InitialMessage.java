package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("1")
public class InitialMessage extends Message {

    private static final long serialVersionUID = 7201507778760836571L;

    public InitialMessage(){
        super();
        this.discriminator = 1;
    }

    public InitialMessage(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions){
        super(text, type, messager, date, tags, mentions);
        this.discriminator = 1;
    }

//    @Override
//    public int getDiscriminator() {
//        return this.getDiscriminator();
//    }
}
