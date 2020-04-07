package ateam.tickettoride.mongodb.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ateam.tickettoride.mongodb.MongoFactory;

import static org.junit.Assert.*;

public class MongoGameDaoTest {

    @Before
    public void setUp() throws Exception {
        MongoFactory.getInstance().getGameDao().clear();
        MongoFactory.getInstance().getGameDao().addGame("1", "game1", "proxy1");
        MongoFactory.getInstance().getGameDao().addGame("2", "game2", "proxy2");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getGames() {
        String[][] gameInfo = MongoFactory.getInstance().getGameDao().getGames();
        assertEquals(gameInfo[0][0], "game1");
        assertEquals(gameInfo[0][1], "proxy1");
        assertEquals(gameInfo[1][0], "game2");
        assertEquals(gameInfo[1][1], "proxy2");
        assertEquals(gameInfo.length, 2);
        assertEquals(gameInfo[0].length, 2);

    }

    @Test
    public void replace() {
        MongoFactory.getInstance().getGameDao().replace("1", "game11", "proxy11");
        String[][] gameInfo = MongoFactory.getInstance().getGameDao().getGames();
        assertEquals(gameInfo[0][0], "game11");
        assertEquals(gameInfo[0][1], "proxy11");
        assertEquals(gameInfo[1][0], "game2");
        assertEquals(gameInfo[1][1], "proxy2");
        assertEquals(gameInfo.length, 2);
        assertEquals(gameInfo[0].length, 2);

        MongoFactory.getInstance().getGameDao().replace("2", "game22", "proxy22");
        gameInfo = MongoFactory.getInstance().getGameDao().getGames();
        assertEquals(gameInfo[0][0], "game11");
        assertEquals(gameInfo[0][1], "proxy11");
        assertEquals(gameInfo[1][0], "game22");
        assertEquals(gameInfo[1][1], "proxy22");
        assertEquals(gameInfo.length, 2);
        assertEquals(gameInfo[0].length, 2);
    }

    @Test
    public void clear() {
        MongoFactory.getInstance().getGameDao().clear();
        String[][] gameInfo = MongoFactory.getInstance().getGameDao().getGames();
        assertEquals(gameInfo.length, 0);
    }
}