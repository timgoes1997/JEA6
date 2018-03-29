package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity(name="MESSAGE")
@DiscriminatorColumn(name="TYPE", discriminatorType =DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@NamedQueries({
        @NamedQuery(name="Message.findAll",
                query="SELECT m FROM MESSAGE m"),
        @NamedQuery(name="Message.findByID",
                query="SELECT m FROM MESSAGE m WHERE m.id = :id"),
        /*@NamedQuery(name="Message.findByUser",
                query="SELECT m FROM MESSAGE m WHERE m.messager.id = :id"),*/
        @NamedQuery(name="Message.findByUser",
                query="SELECT m FROM MESSAGE m WHERE m.messager.id = :id ORDER BY m.date DESC"),
        @NamedQuery(name="Message.getLikesByID",
                query="SELECT COUNT(m.likes) FROM MESSAGE m WHERE m.id = :id"),
})
public abstract class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(min=1, max=280)
    @Column(name = "TEXT", unique = true)
    protected String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    protected MessageType type; //To check wether a message is directed towards your followers or visible for everyone.

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_TAGS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="TAG_ID", referencedColumnName="ID")})
    protected List<Tag> tags;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    protected Date date;

    @OneToOne(cascade = CascadeType.ALL)
    protected User messager;

    @OneToMany
    @JoinTable(name = "MESSAGE_MENTIONS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="MENTION_ACCOUNT_ID", referencedColumnName="ID")})
    protected List<User> mentions;

    @OneToMany
    @JoinTable(name = "MESSAGE_LIKES",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="MENTION_ACCOUNT_ID", referencedColumnName="ID")})
    protected List<User> likes;

    public Message(){

    }

    public Message(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions){
        this.text = text;
        this.type = type;
        this.mentions = mentions;
        this.messager = messager;
        this.tags = tags;
        this.date = getCurrentLocalDateTime();
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

    protected Date getCurrentLocalDateTime() {
        return Date.from(LocalDateTime.ofInstant(new Date().toInstant(),
            ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant());
    }
}
