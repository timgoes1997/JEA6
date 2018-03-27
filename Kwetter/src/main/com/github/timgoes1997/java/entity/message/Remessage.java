package com.github.timgoes1997.java.entity.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class Remessage extends ReplyMessage implements Serializable {

    private static final long serialVersionUID = -9015261934439105868L;

    @Column(name = "HASTEXT")
    private boolean hasText;

    public Remessage(){
        super();
    }

    public Remessage(String text){
        super(text);
    }

    public boolean isHasText() {
        return hasText;
    }
}
