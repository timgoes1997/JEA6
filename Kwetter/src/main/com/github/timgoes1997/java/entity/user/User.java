package com.github.timgoes1997.java.entity.user;

import com.github.timgoes1997.java.entity.message.Message;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        @NamedQuery(name=User.FIND_BY_NAME_PASSWORD,
                query="SELECT u FROM USERDATA u WHERE u.username = :name AND u.password = :password"),
        @NamedQuery(name=User.FIND_BY_VERIFIED_BELOW_DATE,
                query="SELECT u FROM USERDATA u WHERE u.verified = :verified AND u.registrationDate < :date"),
        @NamedQuery(name=User.FIND_VERIFICATION_LINK,
                query="SELECT u FROM USERDATA u WHERE u.verifyLink = :link"),
        @NamedQuery(name=User.FIND_VERIFICATION_LINK_AND_VERIFICATION,
                query="SELECT u FROM USERDATA u WHERE u.verifyLink = :link AND u.verified = :verified"),
})
public class User implements Serializable{

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
    public static final String FIND_VERIFICATION_LINK = "User.findVerificationLink";
    public static final String FIND_VERIFICATION_LINK_AND_VERIFICATION = "User.findVerificationLinkAndVerified";
    public static final String IS_FOLLOWING_USER = "User.isFollowing";

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

    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlAttribute
    public UserRole getRole() {
        return role;
    }

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

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowing() {
        return following;
    }

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

    @XmlAttribute
    public long getId() {
        return id;
    }

    @XmlTransient
    public String getVerifyLink() {
        return verifyLink;
    }

    public void setVerifyLink(String verifyLink) {
        this.verifyLink = verifyLink;
    }

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
}
