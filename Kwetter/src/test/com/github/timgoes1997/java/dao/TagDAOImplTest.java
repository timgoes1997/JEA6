package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.tag.Tag;
import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static org.junit.Assert.*;

public class TagDAOImplTest {

    private EntityManager em;

    private TagDAO mDao;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        TagDAOImpl impl = new TagDAOImpl();
        impl.setEntityManager(em);
        mDao = impl;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() {
        em.getTransaction().begin();
        for(int i = 0; i < 10; i++){
            mDao.create(new Tag(String.format("test %d",i)));
        }
        em.getTransaction().commit();
    }

    @Test
    public void remove() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findTagByName() {
    }

    @Test
    public void getAllTags(){
        em.getTransaction().begin();
        int deletedCount = em.createQuery("DELETE FROM Tag").executeUpdate();
        int count = 5;
        for(int i = 0; i < count; i++){
            em.persist(new Tag(String.format("test %d",i)));
        }

        List<Tag> tags = mDao.getAllTags();

        assertEquals(tags.size(), count );

        em.getTransaction().commit();
    }
}