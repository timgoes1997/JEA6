package com.github.timgoes1997.java.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.user.interfaces.UserInterface;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

@Entity(name = "USERDATA")
@NamedQueries({
        @NamedQuery(name=User.FIND_ALL,
                query="SELECT u FROM USERDATA u"),
        @NamedQuery(name=User.FIND_BY_NAME,
                query="SELECT u FROM USERDATA u WHERE u.username = :name"),
        @NamedQuery(name=User.FIND_BY_EMAIL,
                query="SELECT u FROM USERDATA u WHERE u.email = :email"),
        @NamedQuery(name=User.FIND_BY_NAME_AND_EMAIL,
                query="SELECT u FROM USERDATA u WHERE u.username = :name AND u.email = :email"),
        @NamedQuery(name=User.FIND_BY_ID,
                query="SELECT u FROM USERDATA u WHERE u.id = :id"),
        @NamedQuery(name=User.FIND_BY_MESSAGE,
                query="SELECT u FROM USERDATA u JOIN u.messages m WHERE m.id = :id"),
        @NamedQuery(name=User.FIND_BY_NAME_PASSWORD,
                query="SELECT u FROM USERDATA u WHERE u.username = :name AND u.password = :password"),
        @NamedQuery(name=User.FIND_BY_VERIFIED_BELOW_DATE,
                query="SELECT u FROM USERDATA u WHERE u.verified = :verified AND u.registrationDate < :date"),
        @NamedQuery(name=User.FIND_VERIFICATION_LINK,
                query="SELECT u FROM USERDATA u WHERE u.verifyLink = :link"),
})
public class User implements Serializable, UserInterface {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_NAME = "User.findByName";
    public static final String FIND_BY_EMAIL = "User.findByEmail";
    public static final String FIND_BY_NAME_AND_EMAIL = "User.findByNameAndEmail";
    public static final String FIND_BY_NAME_PASSWORD = "User.findByNameAndPassword";
    public static final String FIND_BY_ID = "User.findByID";
    public static final String FIND_BY_VERIFIED_BELOW_DATE = "User.findByVerified";
    public static final String FIND_BY_MESSAGE = "User.findByMessage";
    public static final String FIND_VERIFICATION_LINK = "User.findVerificationLink";
    public static final String FIND_VERIFICATION_LINK_AND_VERIFICATION = "User.findVerificationLinkAndVerified";

    private static final long serialVersionUID = 1941556366358043294L;

    //======================
    //==      Fields      ==
    //======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "VERIFIED", nullable = false)
    private Boolean verified;

    //@Pattern(regexp = "[^a-zA-Z0-9_]+$")
    @Size(min=3, max=40)
    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Size(min=3, max=40)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UserRole role;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;

    @Column(name = "MIDDLENAME")
    private String middleName;

    @Column(name = "LASTNAME", nullable = false)
    private String lastName;

    @Column(name = "TELEPHONENUMBER")
    private String telephoneNumber;

    @Size(min=0, max=160)
    @Column(name = "BIO")
    private String bio;

    @Size(min=0, max=140)
    @Column(name = "LOCATION")
    private String location;

    @Size(min=0, max=320)
    @Column(name = "EMAIL", unique = true)
    private String email;

    @Size(min=0, max=100)
    @Column(name = "WEBSITE")
    private String website;

    @Size(min=0, max=32)
    @Column(name = "VERIFICATION_LINK")
    private String verifyLink;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY")
    private Date birthDay;

    @Temporal(TemporalType.DATE)
    @Column(name = "REGISTRATION_DATE", nullable = false)
    private Date registrationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PINNED_MESSAGE")
    private Message pinnedMessage;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_FOLLOWERS",
            joinColumns = { @JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="FOLLOWER_ID", referencedColumnName="ID")})
    private List<User> followers;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_FOLLOWING",
            joinColumns = { @JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="FOLLOWER_ID", referencedColumnName="ID")})
    private List<User> following;

    @JsonIgnore
    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinTable(name = "USER_MESSAGES",
            joinColumns = { @JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns = { @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID")})
    private List<Message> messages;

    public User(){

    }

    public User(String username, String password, UserRole role, String firstName, String lastName, String email, String telephoneNumber, Boolean verified){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.verified = verified;
        this.registrationDate = new Date();
    }

    public User(String username, String password, UserRole role, String firstName, String middleName, String lastName, String email, String telephoneNumber, Boolean verified){
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.verified = verified;
        this.registrationDate = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonbTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonbTransient
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Message getPinnedMessage() {
        return pinnedMessage;
    }

    public void setPinnedMessage(Message pinnedMessage) {
        this.pinnedMessage = pinnedMessage;
    }

    @JsonbTransient
    public List<User> getFollowers() {
        return followers;
    }

    @JsonbTransient
    public List<User> getFollowing() {
        return following;
    }

    @JsonbTransient
    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }

    public void removeMessage(Message message){
        this.messages.remove(message);
    }

    public void addFollower(User user){
        this.followers.add(user);
    }

    public void removeFollower(User user){
        this.followers.remove(user);
    }

    public void addFollowing(User user){
        this.following.add(user);
    }

    public void removeFollowing(User user){
        this.following.remove(user);
    }

    public long getId() {
        return id;
    }

    @JsonbTransient
    public String getVerifyLink() {
        return verifyLink;
    }

    public void setVerifyLink(String verifyLink) {
        this.verifyLink = verifyLink;
    }

    @JsonbTransient
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
