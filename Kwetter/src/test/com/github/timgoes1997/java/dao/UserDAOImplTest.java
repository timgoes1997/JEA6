package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class UserDAOImplTest {

    private EntityManager em;

    private UserDAO mDao;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        UserDAOImpl impl = new UserDAOImpl();
        impl.setEntityManager(em);
        mDao = impl;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() {
    }

    @Test
    public void edit() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findByUsername() {
    }

    @Test
    public void addFollower() {
    }

    @Test
    public void removeFollower() {
    }
}