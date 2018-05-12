package com.github.timgoes1997.java.entity.tag;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity(name="TAG")
@NamedQueries({
        @NamedQuery(name=Tag.FIND_ALL,
                query="SELECT t FROM TAG t"),
        @NamedQuery(name=Tag.FIND_BY_NAME,
                query="SELECT t FROM TAG t WHERE t.tagName = :name"),
        @NamedQuery(name=Tag.FIND_BY_ID,
                query="SELECT t FROM TAG t WHERE t.id = :id"),
})
public class Tag implements Serializable {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "Tag.findAll";
    public static final String FIND_BY_NAME = "Tag.findByName";
    public static final String FIND_BY_ID = "Tag.findByID";

    private static final long serialVersionUID = -7353582357753720500L;

    //======================
    //==      Fields      ==
    //======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(min=1, max=279)
    @Column(name = "NAME", unique = true)
    private String tagName;

    public Tag(){

    }

    public Tag(String tagName){
        this.tagName = tagName;
    }

    public long getId() {
        return id;
    }

    public String getTagName(){
        return tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("tagName", this.tagName).build();
    }
}
