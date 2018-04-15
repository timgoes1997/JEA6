package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.entity.message.InitialMessage;
import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class MessageDAOImplTest {

    private EntityManager em;

    private MessageDAO mDao;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        MessageDAOImpl impl = new MessageDAOImpl();
        impl.setEntityManager(em);
        mDao = impl;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() {
        //voordat mdao iets maakt moeten er mentions en tags worden gegenereerd op basis van de tekst.
        mDao.create(new InitialMessage());

    }

    @Test
    public void remove() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findMessageByUser() {
    }

    @Test
    public void getMessageReplies() {
    }

    @Test
    public void getMessageRemessages() {
    }

    @Test
    public void getMessageLikes() {
    }

    @Test
    public void getCurrentLocalDateTime() {
    }

    @Test
    public void generateTags() {

    }

    @Test
    public void generateMentions() {
    }
}