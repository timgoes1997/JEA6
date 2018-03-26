package entity.tag;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name="TAG")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
