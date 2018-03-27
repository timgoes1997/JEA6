package com.github.timgoes1997.java.entity.message;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class InitialMessage extends Message {

    private static final long serialVersionUID = 7201507778760836571L;

    public InitialMessage(){
        super();
    }

    public InitialMessage(String text){
        super(text);
        this.date = Date.from(LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());

        this.tags = new ArrayList<>();
        this.mentions = new ArrayList<>();
    }
}
