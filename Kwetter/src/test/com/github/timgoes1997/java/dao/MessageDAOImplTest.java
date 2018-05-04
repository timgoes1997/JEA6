package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import com.github.timgoes1997.java.entity.message.*;
import com.github.timgoes1997.java.entity.user.User;
import com.github.timgoes1997.java.entity.user.UserRole;
import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class MessageDAOImplTest {

    private EntityManager em;

    private MessageDAO mDao;
    private UserDAO userDAO;
    private TagDAO tagDAO;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        TagDAOImpl tagDAOImpl = new TagDAOImpl();
        tagDAOImpl.setEntityManager(em);

        UserDAOImpl userDAOImpl = new UserDAOImpl();
        userDAOImpl.setEntityManager(em);

        MessageDAOImpl impl = new MessageDAOImpl();
        impl.setEntityManager(em);

        userDAOImpl.setMessageDAO(impl);
        impl.setTagDAO(tagDAOImpl);
        impl.setUserDAO(userDAOImpl);
        userDAOImpl.setLogger();
        impl.setLogger();

        mDao = impl;
        userDAO = userDAOImpl;
        tagDAO = tagDAOImpl;

        em.getTransaction().begin();
        userDAO.create(new User("admin", "admini", UserRole.Admin, "", "", "Admin@admin.com", "", true));
        userDAO.create(new User("moderator", "modo", UserRole.Moderator, "", "", "@Moderator@Moderator.com", "", true));
        userDAO.create(new User("User1", "useroo", UserRole.User, "", "", "User1@User.com", "", true));
        userDAO.create(new User("User2", "useroo", UserRole.User, "", "", "User2@User.com", "", true));
        userDAO.create(new User("User3", "useroo", UserRole.User, "", "", "User3@User.com", "", true));
        userDAO.create(new User("User4", "useroo", UserRole.User, "", "", "User4@User.com", "", true));
        userDAO.create(new User("User5", "useroo", UserRole.User, "", "", "User5@User.com", "", true));
        userDAO.create(new User("User6", "useroo", UserRole.User, "", "", "User6@User.com", "", true));
        em.getTransaction().commit();
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

        em.getTransaction().begin();
        mDao.create(new InitialMessage(generated, MessageType.Public, user1, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated)));
        Message messageUser1 = mDao.find(1);
        mDao.create(new InitialMessage(generated, MessageType.Public, user2, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated)));
        Message messageUser2 = mDao.find(2);
        mDao.create(new ReplyMessage(generated, MessageType.Public, user3, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated), messageUser1));
        Message messageUser3 = mDao.find(3);
        mDao.create(new ReplyMessage(generated, MessageType.Public, user4, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated), messageUser2));
        Message messageUser4 = mDao.find(4);
        mDao.create(new ReplyMessage(generated, MessageType.Public, user5, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated), messageUser3));
        Message messageUser5 = mDao.find(5);
        mDao.create(new Remessage(generated, MessageType.Public, user6, new Date(), mDao.generateTags(generated), mDao.generateMentions(generated), messageUser5));
        Message messageUser6 = mDao.find(6);
        em.flush();
        em.getTransaction().commit();

        assertEquals(messageUser1.getText(), generated);
        assertEquals(messageUser2.getText(), generated);
        assertEquals(messageUser3.getText(), generated);
        assertEquals(messageUser4.getText(), generated);
        assertEquals(messageUser5.getText(), generated);
        assertEquals(messageUser6.getText(), generated);
    }

    @After
    public void tearDown() throws Exception {
        CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<User>(userDAO.getAllUsers());
        ListIterator<User> iterator = users.listIterator();
        while (iterator.hasNext()) {
            em.getTransaction().begin();
            userDAO.remove(iterator.next());
            em.getTransaction().commit();
        }
    }

    @Test
    public void likeMessage() throws Exception {
        User user3 = userDAO.findByUsername("User3");
        em.getTransaction().begin();
        Message messageUser1 = mDao.find(1);
        mDao.likeMessage(messageUser1, user3);
        em.getTransaction().commit();

        List<Message> messages = mDao.findMessagesByLikes(user3);
        assertEquals(messages.size(), 1);
        assertEquals(messages.get(0).getLikes().size(), 1);
        assertEquals(messages.get(0).getLikes().get(0).getId(), user3.getId());

        exception.expect(Exception.class);
        mDao.likeMessage(messageUser1, user3);
    }

    /*
    @Test
    public void removeMessagesAndUsersTest() {


        em.getTransaction().begin();
        //userDAO.remove(user1);
        //mDao.remove(messageUser2);
        em.getTransaction().commit();

        /*
        assertFalse(mDao.exists(1));

        ReplyMessage firstReplyMessage = (ReplyMessage) mDao.find(3);
        assertNull(firstReplyMessage.getMessage());
        assertEquals(firstReplyMessage.getTags().size(), 6);
        assertEquals(firstReplyMessage.getMentions().size(), 5);

        List<Message> messages = mDao.getAllMessages();
        assertEquals(messages.size(), 5);*/
    //}

    private String generateRandomMessage(String bericht){
        return generateRandomMessage(bericht, 240,
                new String[]{
                        "User1",
                        "User2",
                        "User3",
                        "User4",
                        "User5",
                        "User6",
                }, new String[]{
                        "Amazing",
                        "Geweldig",
                        "MyFirstDay",
                        "Test",
                        "Test2",
                        "Test3",
                });
    }

    private String generateRandomMessage(String bericht, int maxlength, String[] mentions, String[] tags){
        if(bericht.length() >= maxlength){
            return bericht;
        }

        int remainingSize = maxlength - bericht.length();
        int toGenerate = remainingSize / 10;
        int mentionsToGenerate = toGenerate / 2;
        int tagsToGenerate = toGenerate / 2;
        int amountOfMentions = new Random().nextInt(mentionsToGenerate) + 1;
        int amountOfTags = new Random().nextInt(tagsToGenerate) + 1;

        for(int i = 0; i < amountOfMentions; i++){

        }
        return " ";

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