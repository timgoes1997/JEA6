package com.github.timgoes1997.java.entity.user;

import static org.junit.Assert.*;


import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

public class UserTest {

    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        System.out.println("TEST");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addMessage() {
        em.getTransaction().begin();

        em.persist(new User("Timmeke", "Wachtwoord", UserRole.User, "Tim", "Goes", "Tim.goes@student.fontys.nl", "0600000000"));

        em.getTransaction().commit();
    }

    @Test
    public void removeMessage() {
    }

    @Test
    public void addFollower() {
    }

    @Test
    public void removeFollower() {
    }

    @Test
    public void addFollowing() {
    }

    @Test
    public void removeFollowing() {
    }
}