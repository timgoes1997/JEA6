package entity.user;

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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addMessage() {
        em.getTransaction().begin();

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