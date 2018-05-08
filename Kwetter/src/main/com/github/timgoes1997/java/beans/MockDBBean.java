package com.github.timgoes1997.java.beans;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.*;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Date;

@Startup
@Singleton
public class MockDBBean {

    @Inject
    private UserDAO userDAO;

    @Inject
    private MessageDAO messageDAO;

    @PostConstruct
    void init() {
        userDAO.create(new User("admin", "admini", UserRole.Admin, "", "", "Admin@admin.com", "", true));
        userDAO.create(new User("moderator", "modo", UserRole.Moderator, "", "", "@Moderator@Moderator.com", "", true));
        userDAO.create(new User("user1", "useroo", UserRole.User, "", "", "User1@User.com", "", true));
        userDAO.create(new User("user2", "useroo", UserRole.User, "", "", "User2@User.com", "", true));
        userDAO.create(new User("user3", "useroo", UserRole.User, "", "", "User3@User.com", "", true));
        userDAO.create(new User("user4", "useroo", UserRole.User, "", "", "User4@User.com", "", true));
        userDAO.create(new User("user5", "useroo", UserRole.User, "", "", "User5@User.com", "", true));
        userDAO.create(new User("user6", "useroo", UserRole.User, "", "", "User6@User.com", "", true));
        User user1 = userDAO.findByUsername("user1");
        User user2 = userDAO.findByUsername("user2");
        User user3 = userDAO.findByUsername("user3");
        User user4 = userDAO.findByUsername("user4");
        User user5 = userDAO.findByUsername("user5");
        User user6 = userDAO.findByUsername("user6");

        userDAO.addFollower(user1, user2);
        userDAO.addFollower(user1, user5);
        userDAO.addFollower(user1, user6);
        userDAO.addFollower(user2, user5);
        userDAO.addFollower(user2, user6);
        userDAO.addFollower(user3, user2);
        userDAO.addFollower(user4, user3);
        userDAO.addFollower(user4, user5);
        userDAO.addFollower(user4, user6);
        userDAO.addFollower(user4, user1);
        userDAO.addFollower(user5, user1);
        userDAO.addFollower(user6, user1);

        String testBericht = "<b>Dit is een test bericht </b> <i> test </i>";

        String generated = generateTags(generateMentions(testBericht, new String[]{
                "user1",
                "user2",
                "user3",
                "user4",
                "user5",
                "user6",
        }), new String[]{
                "Amazing",
                "Geweldig",
                "MyFirstDay",
                "Test",
                "Test2",
                "Test3",
        });

        messageDAO.create(new InitialMessage(generated, MessageType.Public, user1, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated)));
        Message messageUser1 = messageDAO.find(1);
        messageDAO.create(new InitialMessage(generated, MessageType.Public, user2, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated)));
        Message messageUser2 = messageDAO.find(2);

        messageDAO.create(new ReplyMessage(generated, MessageType.Public, user3, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser1));
        Message messageUser3 = messageDAO.find(3);
        messageDAO.create(new ReplyMessage(generated, MessageType.Public, user4, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser2));
        Message messageUser4 = messageDAO.find(4);
        messageDAO.create(new ReplyMessage(generated, MessageType.Public, user5, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser3));
        Message messageUser5 = messageDAO.find(5);
        messageDAO.create(new Remessage(generated, MessageType.Public, user6, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser5));
        Message messageUser6 = messageDAO.find(6);

        messageDAO.create(new InitialMessage(generated, MessageType.Public, user2, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated)));
        messageDAO.create(new InitialMessage(generated, MessageType.Public, user2, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated)));
        messageDAO.create(new Remessage(generated, MessageType.Public, user2, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser1));
        messageDAO.create(new Remessage(generated, MessageType.Public, user2, new Date(), messageDAO.generateTags(generated), messageDAO.generateMentions(generated), messageUser5));
    }


    private String generateMentions(String bericht, String... mentions){
        String generated = bericht;
        for(String s : mentions){
            generated += " @"+s+" ";
        }
        return generated;
    }

    private String generateTags(String bericht, String... tags){
        String generated = bericht;
        for(String s : tags){
            generated += " #"+s+" ";
        }
        return generated;
    }

}
