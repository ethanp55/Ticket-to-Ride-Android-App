package ateam.tickettoride.mongodb.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ateam.tickettoride.mongodb.MongoFactory;

import static org.junit.Assert.*;

public class MongoUserDaoTest {

    @Before
    public void setUp() throws Exception {
        MongoFactory.getInstance().getUserDao().clear();
        MongoFactory.getInstance().getUserDao().addUser("wredd", "password1");
        MongoFactory.getInstance().getUserDao().addUser("wil", "password2");
    }

    @After
    public void takeDown() throws Exception {
        MongoFactory.getInstance().getUserDao().clear();
    }

    @Test
    public void getPassword() {
        String password1 = MongoFactory.getInstance().getUserDao().getPassword("wredd");
        String password2 = MongoFactory.getInstance().getUserDao().getPassword("wil");
        assertEquals(password1, "password1");
        assertEquals(password2, "password2");
        assertNull(MongoFactory.getInstance().getUserDao().getPassword(null));
        assertNull(MongoFactory.getInstance().getUserDao().getPassword("jack"));
    }

    @Test
    public void clear() {
        MongoFactory.getInstance().getUserDao().clear();
        String password1 = MongoFactory.getInstance().getUserDao().getPassword("wredd");
        String password2 = MongoFactory.getInstance().getUserDao().getPassword("wil");
        assertNull(password1);
        assertNull(password2);
    }

    @Test
    public void checkPassword() {
        assertTrue(MongoFactory.getInstance().getUserDao().checkPassword("wredd", "password1"));
        assertFalse(MongoFactory.getInstance().getUserDao().checkPassword("wredd", "password2"));

        assertTrue(MongoFactory.getInstance().getUserDao().checkPassword("wil", "password2"));
        assertFalse(MongoFactory.getInstance().getUserDao().checkPassword("jack", "password1"));
        assertFalse(MongoFactory.getInstance().getUserDao().checkPassword(null, "password1"));
        assertFalse(MongoFactory.getInstance().getUserDao().checkPassword("wil", null));
        assertFalse(MongoFactory.getInstance().getUserDao().checkPassword(null, null));
    }

    @Test
    public void checkUsername() {
        assertTrue(MongoFactory.getInstance().getUserDao().checkUsername("wil"));
        assertTrue(MongoFactory.getInstance().getUserDao().checkUsername("wredd"));
        assertFalse(MongoFactory.getInstance().getUserDao().checkUsername(null));
        assertFalse(MongoFactory.getInstance().getUserDao().checkUsername("jack"));
    }
}