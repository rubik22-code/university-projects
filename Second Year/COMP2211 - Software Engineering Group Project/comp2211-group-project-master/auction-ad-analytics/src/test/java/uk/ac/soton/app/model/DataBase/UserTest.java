package uk.ac.soton.app.model.DataBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.enums.Privilege;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the User record
 */
class UserTest {

    //List of User objects used to test some methods of the User record
    List<User> userObjects;

    /**
     * Sets up everything needed to test the User record
     */
    @BeforeEach
    void setUp() {
        //Sets up a list of User objects to be used to test some methods of the User record
        this.userObjects = new ArrayList<>();
        this.userObjects.add(new User("admin", Privilege.ADMIN));
        this.userObjects.add(new User("editor", Privilege.EDITOR));
        this.userObjects.add(new User("viewer", Privilege.VIEWER));
    }

    /**
     * Tests the username() method
     */
    @Test
    void testUsername() {
        //TEST #1
        //Tests if the expected username is returned
        assertEquals("editor", userObjects.get(1).username());

    }

    /**
     * Tests the accessLevel() method
     */
    @Test
    void testAccessLevel() {
        //TEST #1
        //Tests if the expected access level is returned
        //When the user is an admin
        assertEquals(Privilege.ADMIN, userObjects.get(0).accessLevel());

        //TEST #2
        //Tests if the expected access level is returned
        //When the user is an editor
        assertEquals(Privilege.EDITOR, userObjects.get(1).accessLevel());

        //TEST #3
        //Tests if the expected access level is returned
        //When the user is a viewer
        assertEquals(Privilege.VIEWER, userObjects.get(2).accessLevel());
    }
}