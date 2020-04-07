package ateam.tickettoride.mongodb.Dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ateam.tickettoride.mongodb.MongoFactory;

import static org.junit.Assert.*;

public class MongoCommandDaoTest {

    @Before
    public void setUp() throws Exception {
        MongoFactory.getInstance().getCommandDao().clear();
        MongoFactory.getInstance().getCommandDao().replace("1", "commands1");
        MongoFactory.getInstance().getCommandDao().replace("2", "commands2");
    }

    @After
    public void tearDown() throws Exception {
        MongoFactory.getInstance().getCommandDao().clear();
    }

    @Test
    public void getCommands() {
        String command1 = MongoFactory.getInstance().getCommandDao().getCommands("1");
        String command2 = MongoFactory.getInstance().getCommandDao().getCommands("2");
        assertEquals("commands1", command1);
        assertEquals("commands2", command2);
        assertNull(MongoFactory.getInstance().getCommandDao().getCommands(null));
        assertNull(MongoFactory.getInstance().getCommandDao().getCommands("3"));
    }

    @Test
    public void replace() {
        MongoFactory.getInstance().getCommandDao().replace("1", "commands11");
        MongoFactory.getInstance().getCommandDao().replace("2", "commands22");
        MongoFactory.getInstance().getCommandDao().replace(null, null);

        String command1 = MongoFactory.getInstance().getCommandDao().getCommands("1");
        String command2 = MongoFactory.getInstance().getCommandDao().getCommands("2");
        assertEquals("commands11", command1);
        assertEquals("commands22", command2);
    }

    @Test
    public void clear() {
        MongoFactory.getInstance().getCommandDao().clear();
        String command1 = MongoFactory.getInstance().getCommandDao().getCommands("1");
        String command2 = MongoFactory.getInstance().getCommandDao().getCommands("2");
        assertNull(command1);
        assertNull(command2);
    }
}