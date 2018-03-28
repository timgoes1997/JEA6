package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.UserDAO;
import helper.PersistenceHelper;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Map;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void test1_create() {
        if(em.isOpen()) System.out.println("LUL");
    }

    @Test
    public void test2_edit() {
        if(em.isOpen()) System.out.println("LUL2");
    }

    @Test
    public void test3_find() {
        if(em.isOpen()) System.out.println("LUL3");
    }

    @Test
    public void test4_findByUsername() {
        if(em.isOpen()) System.out.println("LUL4");
    }

    @Test
    public void test5_addFollower() {
        if(em.isOpen()) System.out.println("LUL5");
    }

    @Test
    public void test6_removeFollower() {
        if(em.isOpen()) System.out.println("LUL6");
    }
}