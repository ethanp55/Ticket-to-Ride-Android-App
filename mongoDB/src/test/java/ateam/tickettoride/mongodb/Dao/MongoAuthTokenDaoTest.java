package ateam.tickettoride.mongodb.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ateam.tickettoride.mongodb.MongoFactory;

import static org.junit.Assert.*;

public class MongoAuthTokenDaoTest {

    @Before
    public void setUp() throws Exception {
        MongoFactory.getInstance().getAuthTokenDao().clear();
        MongoFactory.getInstance().getAuthTokenDao().addToken("wredd", "auth1");
        MongoFactory.getInstance().getAuthTokenDao().addToken("wil", "auth2");
    }

    @After
    public void tearDown() throws Exception {
        MongoFactory.getInstance().getAuthTokenDao().clear();
    }

    @Test
    public void getUsername() {
        String user1 = MongoFactory.getInstance().getAuthTokenDao().getUsername("auth1");
        String user2 = MongoFactory.getInstance().getAuthTokenDao().getUsername("auth2");
        String user3 = MongoFactory.getInstance().getAuthTokenDao().getUsername(null);
        String user4 = MongoFactory.getInstance().getAuthTokenDao().getUsername("jack");

        assertEquals(user1, "wredd");
        assertEquals(user2, "wil");
        assertNull(user3);
        assertNull(user4);
    }

    @Test
    public void getAuthToken() {
        String auth1 = MongoFactory.getInstance().getAuthTokenDao().getAuthToken("wredd");
        String auth2 = MongoFactory.getInstance().getAuthTokenDao().getAuthToken("wil");
        String auth3 = MongoFactory.getInstance().getAuthTokenDao().getAuthToken(null);
        String auth4 = MongoFactory.getInstance().getAuthTokenDao().getAuthToken("jack");

        assertEquals(auth1, "auth1");
        assertEquals(auth2, "auth2");
        assertNull(auth3);
        assertNull(auth4);
    }

    @Test
    public void clear() {
        MongoFactory.getInstance().getAuthTokenDao().clear();
        String auth1 = MongoFactory.getInstance().getAuthTokenDao().getAuthToken("wredd");
        String user1 = MongoFactory.getInstance().getAuthTokenDao().getUsername("auth2");
        assertNull(auth1);
        assertNull(user1);
    }
}