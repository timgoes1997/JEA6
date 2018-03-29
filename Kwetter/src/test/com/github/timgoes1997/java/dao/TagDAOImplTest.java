package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.tag.Tag;
import helper.PersistenceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import java.util.List;

import static org.junit.Assert.*;

public class TagDAOImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private EntityManager em;

    private TagDAO tDao;

    @Before
    public void setUp() throws Exception {
        em = PersistenceHelper.getEntityManager();
        TagDAOImpl impl = new TagDAOImpl();
        impl.setEntityManager(em);
        tDao = impl;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() {
        em.getTransaction().begin();
        for (int i = 0; i < 10; i++) {
            tDao.create(new Tag(String.format("test %d", i)));
        }
        em.getTransaction().commit();
    }

    @Test
    public void remove() {
        deletAllTags();
        em.getTransaction().begin();
        String remove = "RemoveTag";
        em.persist(new Tag(remove));
        em.getTransaction().commit();
        Tag t = tDao.findTagByName(remove);
        assertNotNull(t);

        em.getTransaction().begin();
        tDao.remove(t);
        em.getTransaction().commit();

        exception.expect(NoResultException.class);
        Tag tRemoved = tDao.findTagByName(remove);
        assertNull(tRemoved);
    }

    @Test
    public void find() {
        deletAllTags();
        em.getTransaction().begin();
        String test = "TestFindByID";
        em.persist(new Tag(test));
        em.getTransaction().commit();
        Tag byName = tDao.findTagByName(test);
        assertNotNull(tDao);
        long id = byName.getId();
        Tag t = tDao.find(id);
        assertNotNull(t);
        assertEquals(test,t.getTagName());
    }

    @Test
    public void findTagByName() {
        em.getTransaction().begin();
        String tagText1 = "Zoek";
        String tagText2 = "Test";
        String tagText3 = "Nieuw";
        em.persist(new Tag(tagText1));
        em.persist(new Tag(tagText2));
        em.persist(new Tag(tagText3));
        Tag tag = tDao.findTagByName(tagText1);
        Tag tag2 = tDao.findTagByName(tagText2);
        Tag tag3 = tDao.findTagByName(tagText3);
        em.getTransaction().commit();

        assertEquals(tagText1, tag.getTagName());
        assertEquals(tagText2, tag2.getTagName());
        assertEquals(tagText3, tag3.getTagName());

        exception.expect(NoResultException.class);
        Tag none = tDao.findTagByName("RandomGekwetter");
        assertNull(none);
    }

    /**
     * Checks all the tags in the database and gets the through the DAO
     */
    @Test
    public void getAllTags() {
        deletAllTags();
        em.getTransaction().begin();
        int count = 5;
        for (int i = 0; i < count; i++) {
            em.persist(new Tag(String.format("test %d", i)));
        }

        List<Tag> tags = tDao.getAllTags();

        assertEquals(tags.size(), count);

        em.getTransaction().commit();
    }

    private int deletAllTags() {
        em.getTransaction().begin();
        int count = em.createQuery("DELETE FROM TAG").executeUpdate();
        em.getTransaction().commit();
        return count;
    }
}