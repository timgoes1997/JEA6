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
        userDAO.create(new User("User1", "useroo", UserRole.User, "", "", "User1@User.com", "", true));
        userDAO.create(new User("User2", "useroo", UserRole.User, "", "", "User2@User.com", "", true));
        userDAO.create(new User("User3", "useroo", UserRole.User, "", "", "User3@User.com", "", true));
        userDAO.create(new User("User4", "useroo", UserRole.User, "", "", "User4@User.com", "", true));
        userDAO.create(new User("User5", "useroo", UserRole.User, "", "", "User5@User.com", "", true));
        userDAO.create(new User("User6", "useroo", UserRole.User, "", "", "User6@User.com", "", true));
        User user1 = userDAO.findByUsername("User1");
        User user2 = userDAO.findByUsername("User2");
        User user3 = userDAO.findByUsername("User3");
        User user4 = userDAO.findByUsername("User4");
        User user5 = userDAO.findByUsername("User5");
        User user6 = userDAO.findByUsername("User6");

        String testBericht = "Dit is een test bericht ";

        String generated = generateTags(generateMentions(testBericht, new String[]{
                "User1",
                "User2",
                "User3",
                "User4",
                "User5",
                "User6",
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
