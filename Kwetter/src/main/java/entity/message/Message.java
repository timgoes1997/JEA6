package entity.message;

import entity.tag.Tag;
import entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name="MESSAGE")
public abstract class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Size(min=1, max=280)
    @Column(name = "TEXT", unique = true)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private MessageType type; //To check wether a message is directed towards your followers or visible for everyone.

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MESSAGE_TAGS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="TAG_ID", referencedColumnName="ID")})
    private List<Tag> tags;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    private User messager;

    @OneToMany
    @JoinTable(name = "MESSAGE_MENTIONS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="MENTION_ACCOUNT_ID", referencedColumnName="ID")})
    private List<User> mentions;

    public Message(){

    }

    public Message(String text){
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public MessageType getType() {
        return type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Date getDate() {
        return date;
    }

    public User getMessager() {
        return messager;
    }

    public List<User> getMentions() {
        return mentions;
    }
}
