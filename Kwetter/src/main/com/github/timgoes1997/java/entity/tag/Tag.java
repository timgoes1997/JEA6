package com.github.timgoes1997.java.entity.tag;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name="TAG")
public class Tag implements Serializable {

    private static final long serialVersionUID = -7353582357753720500L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(min=1, max=279)
    @Column(name = "TEXT", unique = true)
    public String tagName;

    public Tag(){

    }

    public Tag(String tagName){
        this.tagName = tagName;
    }

    public long getId() {
        return id;
    }
}
