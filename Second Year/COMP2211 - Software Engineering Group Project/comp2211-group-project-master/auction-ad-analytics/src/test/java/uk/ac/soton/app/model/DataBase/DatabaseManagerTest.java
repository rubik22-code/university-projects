package uk.ac.soton.app.model.DataBase;

import org.junit.jupiter.api.*;
import uk.ac.soton.app.enums.Privilege;
import uk.ac.soton.app.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the DatabaseManager class
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseManagerTest {

    //DatabaseManager object used to test some methods of the DatabaseManager class
    private DatabaseManager databaseManager;

    /**
     * Sets up everything needed to test the DatabaseManager class
     * @throws ConnectionError exception thrown when a connection to the database can't be made
     */
    @BeforeEach
    void setUp() throws ConnectionError {
        //Sets up a DatabaseManager object to be used to test some methods of the DatabaseManager class
        databaseManager = new DatabaseManager();
    }

    /**
     * Tests the authenticateLogin() method
     * Also tests the hashPassword() method indirectly
     * @throws InvalidLogin exception thrown when a user's login credentials aren't valid
     */
    @Test
    @Order(1)
    void testAuthenticateLogin() throws InvalidLogin {
        //TEST #1
        //Tests if the expected access level is returned
        //When a valid username and password combination is given
        assertSame(databaseManager.authenticateLogin("admin", "admin"), Privilege.ADMIN);

        //TEST #2
        //Tests if an InvalidLogin exception is thrown
        //When an invalid username but valid password are given
        assertThrows(InvalidLogin.class, () -> databaseManager.authenticateLogin("admin1", "admin"));

        //TEST #3
        //Tests if an InvalidLogin exception is thrown
        //When a valid username but invalid password are given
        assertThrows(InvalidLogin.class, () -> databaseManager.authenticateLogin("admin", "admin1"));

        //TEST #4
        //Tests if an InvalidLogin exception is thrown
        //When an invalid username and password are given
        assertThrows(InvalidLogin.class, () -> databaseManager.authenticateLogin("admin1", "admin1"));
    }


    /**
     * Tests the createUser() method
     * Also tests the checkPrivilege() method indirectly
     * @throws UserAlreadyExists exception thrown when the user we're trying to create already exists
     * @throws UnauthorisedOperation exception thrown when the user is not allowed to create this new user
     */
    @Test
    @Order(2)
    void testCreateUser() throws UserAlreadyExists, UnauthorisedOperation {
        //TEST #1
        //Tests if an UnauthorisedOperation exception is thrown
        //When an admin tries to create another admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("admin", Privilege.ADMIN), "testAdmin", "testPassword", Privilege.ADMIN));

        //TEST #2
        //Tests if an UnauthorisedOperation exception is thrown
        //When an editor tries to create an admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("editor", Privilege.EDITOR), "testAdmin", "testPassword", Privilege.ADMIN));

        //TEST #3
        //Tests if an UnauthorisedOperation exception is thrown
        //When an editor tries to create another editor
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("editor", Privilege.EDITOR), "testEditor", "testPassword", Privilege.EDITOR));

        //TEST #4
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to create an admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("viewer", Privilege.VIEWER), "testAdmin", "testPassword", Privilege.ADMIN));

        //TEST #5
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to create an editor
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("viewer", Privilege.VIEWER), "testEditor", "testPassword", Privilege.EDITOR));

        //TEST #6
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to create another viewer
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.createUser(new User("viewer", Privilege.VIEWER), "testViewer", "testPassword", Privilege.VIEWER));

        //TEST #7
        //Tests if an admin can create an editor
        databaseManager.createUser(new User("admin", Privilege.ADMIN), "testEditor", "testPassword", Privilege.EDITOR);
        assertThrows(UserAlreadyExists.class, () -> databaseManager.createUser(new User("admin", Privilege.ADMIN), "testEditor", "testPassword", Privilege.EDITOR));

        //TEST #8
        //Tests if an admin can create a viewer
        databaseManager.createUser(new User("admin", Privilege.ADMIN), "testViewer1", "testPassword", Privilege.VIEWER);
        assertThrows(UserAlreadyExists.class, () -> databaseManager.createUser(new User("admin", Privilege.ADMIN), "testViewer1", "testPassword", Privilege.VIEWER));

        //TEST #9
        //Tests if an editor can create a viewer
        databaseManager.createUser(new User("editor", Privilege.EDITOR), "testViewer2", "testPassword", Privilege.VIEWER);
        assertThrows(UserAlreadyExists.class, () -> databaseManager.createUser(new User("admin", Privilege.ADMIN), "testViewer2", "testPassword", Privilege.VIEWER));

        //TEST #10
        //Tests if an UserAlreadyExists exception is thrown
        //When a user tries to create a user that already exists
        assertThrows(UserAlreadyExists.class, () -> databaseManager.createUser(new User("admin", Privilege.ADMIN), "testEditor", "testPassword", Privilege.EDITOR));
    }

    /**
     * Tests the deleteUser() method
     * Also tests the checkPrivilege() method indirectly
     * @throws UserDoesNotExist exception thrown when the user we're trying to delete doesn't exist
     * @throws UnauthorisedOperation exception thrown when the user is not allowed to create this new user
     */
    @Test
    @Order(5)
    void testDeleteUser() throws UserDoesNotExist, UnauthorisedOperation {
        //TEST #1
        //Tests if an UnauthorisedOperation exception is thrown
        //When an admin tries to delete another admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "admin"));

        //TEST #2
        //Tests if an UnauthorisedOperation exception is thrown
        //When an editor tries to delete an admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("editor", Privilege.EDITOR), "admin"));

        //TEST #3
        //Tests if an UnauthorisedOperation exception is thrown
        //When an editor tries to delete another editor
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("editor", Privilege.EDITOR), "testEditor"));

        //TEST #4
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to delete an admin
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("viewer", Privilege.VIEWER), "admin"));

        //TEST #5
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to delete an editor
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("viewer", Privilege.VIEWER), "testEditor"));

        //TEST #6
        //Tests if an UnauthorisedOperation exception is thrown
        //When a viewer tries to delete another viewer
        assertThrows(UnauthorisedOperation.class, () -> databaseManager.deleteUser(new User("viewer", Privilege.VIEWER), "testViewer1"));

        //TEST #7
        //Tests if an admin can delete an editor
        databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testEditor");
        assertThrows(UserDoesNotExist.class, () -> databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testEditor"));

        //TEST #8
        //Tests if an admin can delete a viewer
        databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testViewer1");
        assertThrows(UserDoesNotExist.class, () -> databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testViewer1"));

        //TEST #9
        //Tests if an editor can delete a viewer
        databaseManager.deleteUser(new User("editor", Privilege.EDITOR), "testViewer2");
        assertThrows(UserDoesNotExist.class, () -> databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testViewer2"));

        //TEST #10
        //Tests if an UserDoesNotExist exception is thrown
        //When a user tries to delete a user that doesn't exist
        assertThrows(UserDoesNotExist.class, () -> databaseManager.deleteUser(new User("admin", Privilege.ADMIN), "testEditor"));
    }

    /**
     * Tests the checkPassword() method
     * Also tests the hashPassword() method indirectly
     * @throws UserDoesNotExist exception thrown when the user we're trying to delete doesn't exist
     */
    @Test
    @Order(3)
    void testCheckPassword() throws UserDoesNotExist {
        //TEST #1
        //Tests if an UserDoesNotExist exception is thrown
        //When the password of a user that doesn't exist is checked
        assertThrows(UserDoesNotExist.class, () -> databaseManager.checkPassword(new User("testAdmin", Privilege.ADMIN), "admin"));

        //TEST #2
        //Tests if false is returned
        //When the password of the user doesn't match the given password
        assertFalse(databaseManager.checkPassword(new User("admin", Privilege.ADMIN), "testPassword"));

        //TEST #3
        //Tests if true is returned
        //When the password of the user matches the given password
        assertTrue(databaseManager.checkPassword(new User("admin", Privilege.ADMIN), "admin"));
    }

    /**
     * Tests the changePassword() method
     * Also tests the hashPassword() method indirectly
     * @throws UserDoesNotExist exception thrown when the user we're trying to delete doesn't exist
     */
    @Test
    @Order(4)
    void testChangePassword() throws UserDoesNotExist {
        //TEST #1
        //Tests if an UserDoesNotExist exception is thrown
        //When the password being changed belongs to a user that doesn't exist
        assertThrows(UserDoesNotExist.class, () -> databaseManager.changePassword(new User("testAdmin", Privilege.ADMIN), "admin"));

        //TEST #2
        //Tests if the password of a user can be changed
        databaseManager.changePassword(new User("testEditor", Privilege.EDITOR), "password");
        assertTrue(databaseManager.checkPassword(new User("testEditor", Privilege.EDITOR), "password"));
    }
}