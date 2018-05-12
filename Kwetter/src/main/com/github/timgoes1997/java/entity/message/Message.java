package com.github.timgoes1997.java.entity.message;

import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.hateoas.Link;

import javax.json.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name="MESSAGE")
@DiscriminatorColumn(name="TYPE", discriminatorType =DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@NamedQueries({
        @NamedQuery(name=Message.FIND_ALL,
                query="SELECT m FROM MESSAGE m"),
        @NamedQuery(name=Message.FIND_ID,
                query="SELECT m FROM MESSAGE m WHERE m.id = :id"),
        @NamedQuery(name=Message.FIND_MESSAGES,
                query="SELECT m FROM MESSAGE m WHERE m.messager.username = :name AND (m.discriminator=1 OR m.discriminator=3) ORDER BY m.date DESC"), // ORDER BY m.date DESC
        @NamedQuery(name=Message.FIND_MESSAGES_REPLIES,
                query="SELECT m FROM MESSAGE m WHERE m.messager.username = :name ORDER BY m.date DESC"), // ORDER BY m.date DESC
        @NamedQuery(name=Message.FIND_USER,
                query="SELECT m FROM MESSAGE m WHERE m.messager.id = :id"),
        @NamedQuery(name=Message.FIND_USER_ID,
                query="SELECT m FROM MESSAGE m WHERE m.messager.username = :name AND m.id = :id"),
        @NamedQuery(name=Message.GET_LIKES_BY_MESSAGE,
                query="SELECT COUNT(m.likes) FROM MESSAGE m WHERE m.id = :id"),
})
public abstract class Message implements Serializable {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "Message.findAll";
    public static final String FIND_ID = "Message.findByID";
    public static final String FIND_MESSAGES = "Message.findMessages";
    public static final String FIND_MESSAGES_REPLIES = "Message.findMessagesReplies";
    public static final String FIND_USER = "Message.findByUser";
    public static final String FIND_USER_ID = "Message.findByUserAndID";
    public static final String GET_LIKES_BY_MESSAGE = "Message.getLikesByID";


    //======================
    //==      Fields      ==
    //======================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Size(min=1, max=280)
    @Column(name = "TEXT")
    protected String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGETYPE")
    protected MessageType type; //To check wether a message is directed towards your followers or visible for everyone.

    @Column(name = "TYPE")
    protected int discriminator;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_TAGS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="TAG_ID", referencedColumnName="ID")})
    protected List<Tag> tags;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    protected Date date;

    @OneToOne(fetch = FetchType.EAGER)
    protected User messager;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_MENTIONS",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="MENTION_ACCOUNT_ID", referencedColumnName="ID")})
    protected List<User> mentions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_LIKES",
            joinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="LIKED_ACCOUNT_ID", referencedColumnName="ID")})
    protected List<User> likes;

    @Transient
    protected List<Link> links = new ArrayList<>();

    public Message(){

    }

    public Message(String text, MessageType type, User messager, Date date, List<Tag> tags, List<User> mentions){
        this.text = text;
        this.type = type;
        this.mentions = mentions;
        this.messager = messager;
        this.tags = tags;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public int getDiscriminator() { return discriminator; }

    public void setDiscriminator(int discriminator) {
        this.discriminator = discriminator;
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

    public void setText(String text) {
        this.text = text;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMessager(User messager) {
        this.messager = messager;
    }

    public void setMentions(List<User> mentions) {
        this.mentions = mentions;
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public JsonObject toJson(){
        JsonArrayBuilder mentionsJsonArray = Json.createArrayBuilder();
        JsonArrayBuilder tagsJsonArray = Json.createArrayBuilder();
        JsonArrayBuilder linksJsonArray = Json.createArrayBuilder();
        mentions.stream().map(User::toJsonSimple).forEach(mentionsJsonArray::add);
        tags.stream().map(Tag::toJson).forEach(tagsJsonArray::add);
        links.stream().map(Link::toJson).forEach(linksJsonArray::add);

        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("messager", this.messager.toJsonSimple())
                .add("date", this.date.toString())
                .add("mentions", mentionsJsonArray)
                .add("tags", tagsJsonArray)
                .add("text", this.text)
                .add("type", this.type.toString())
                .add("discriminator", String.valueOf(this.discriminator))
                .add("links", linksJsonArray)
                .build();
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public boolean hasLinks(){
        return links.size() > 0;
    }

    public void addLink(Link link){
        if(link != null){
            links.add(link);
        }
    }

    public void addLink(String url, String rel, String requestType){
        links.add(new Link(url, rel, requestType));
    }
}
