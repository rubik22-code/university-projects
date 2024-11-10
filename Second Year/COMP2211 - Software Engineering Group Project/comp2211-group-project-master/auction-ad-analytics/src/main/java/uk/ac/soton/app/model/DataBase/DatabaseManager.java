package uk.ac.soton.app.model.DataBase;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;
import uk.ac.soton.app.enums.Privilege;
import uk.ac.soton.app.exceptions.*;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Class used to manage the database containing each user's username, password and privilege
 */
public class DatabaseManager {

    //Connection to the database
    private final MongoClient connection;

    /**
     * Creates an object used to manage the database containing each user's username, password and privilege
     * @throws ConnectionError exception thrown when a connection to the database can't be made
     */
    public DatabaseManager() throws ConnectionError {
        try {connection = MongoClients.create("mongodb+srv://das1g22:admin@cluster0.vwrfbu7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");}
        catch (IllegalArgumentException e) { throw new ConnectionError(); }
    }

    /**
     * Hashes a password
     * @param password password being hashed
     * @return hashed password
     */
    private static byte[] hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return ( messageDigest.digest(password.getBytes()) );
        } catch ( Exception ignore ) {}
        return ( null );
    }

    /**
     * Authenticates a user's login credentials
     * @param username username used to log in
     * @param password password used to log in
     * @return returns the user's privilege if their login credentials are valid
     * @throws InvalidLogin exception thrown when a user's login credentials aren't valid
     */
    public Privilege authenticateLogin(String username, String password) throws InvalidLogin {
        //Fetches the user's record in the database
        MongoDatabase db = connection.getDatabase("userLoginsDB");
        MongoCollection<Document> userLogins = db.getCollection("userLoginsCollection");
        Document query = new Document("_id", username);
        Document user = userLogins.find(query).first();

        //If the user doesn't exist or their credentials are wrong
        //Throws an InvalidLogin exception
        //Otherwise, returns the user's privilege
        if ( user == null ) {throw new InvalidLogin();}
        Binary binaryPassword = (Binary) user.get("password");
        if (!Arrays.equals(binaryPassword.getData(), hashPassword(password))) {throw new InvalidLogin();}
        else {return Privilege.valueOf(user.getString("privilege"));}
    }

    /**
     * Checks if a privilege is greater than another privilege
     * @param p1 privilege
     * @param p2 another privilege
     * @return whether the first privilege is higher than the second privilege or not
     */
    private Boolean checkPrivilege(Privilege p1, Privilege p2) {
        if (p1 == Privilege.VIEWER) return false;
        else if (p1 == Privilege.EDITOR && p2 != Privilege.VIEWER) return false;
        else return (!(p1 == Privilege.ADMIN && p2 == Privilege.ADMIN));
    }

    /**
     * Creates a new user and adds them to the database
     * @param user user that is creating this new user
     * @param username username of this new user
     * @param password password of this new user
     * @param privilege privilege of this new user
     * @throws UnauthorisedOperation exception thrown when the user is not allowed to create this new user
     * @throws UserAlreadyExists exception thrown when the user we're trying to create already exists
     */
    public void createUser(User user, String username, String password, Privilege privilege) throws UnauthorisedOperation, UserAlreadyExists {
        //If the user is not allowed to create this new user
        //Throws an UnauthorisedOperation exception
        if (!checkPrivilege(user.accessLevel(), privilege)) throw new UnauthorisedOperation();

        //Fetches the database
        MongoDatabase db = connection.getDatabase("userLoginsDB");
        MongoCollection<Document> userLogins = db.getCollection("userLoginsCollection");

        //If the user we're trying to create already exists
        //Throws an UserAlreadyExists exception
        Document query = new Document("_id", username);
        Document possibleUser = userLogins.find(query).first();
        if (possibleUser != null) throw new UserAlreadyExists();

        //Creates the new user
        Document newUser = new Document("_id", username).append("password", hashPassword(password)).append("privilege", privilege.name());
        userLogins.insertOne(newUser);
    }

    /**
     * Deletes a user and removes them from the database
     * @param user user that is deleting a user
     * @param username username of this user
     * @throws UserDoesNotExist exception thrown when the user we're trying to delete doesn't exist
     * @throws UnauthorisedOperation exception thrown when the user is not allowed to delete this user
     */
    public void deleteUser(User user, String username) throws UserDoesNotExist, UnauthorisedOperation {
        //Fetches the database
        MongoDatabase db = connection.getDatabase("userLoginsDB");
        MongoCollection<Document> userLogins = db.getCollection("userLoginsCollection");

        //If the user we're trying to delete doesn't exist
        //Throws an UserDoesNotExists exception
        Document query = new Document("_id", username);
        Document possibleUser = userLogins.find(query).first();
        if (possibleUser == null) throw new UserDoesNotExist();

        //If the user is not allowed to delete this user
        //Throws an UnauthorisedOperation exception
        Privilege possibleUserPrivilege = Privilege.valueOf(possibleUser.getString("privilege"));
        if (!checkPrivilege(user.accessLevel(), possibleUserPrivilege)) throw new UnauthorisedOperation();

        //Deletes the new user
        userLogins.deleteOne(possibleUser);
    }

    /**
     * Checks if a given password matches a user's current password
     * @param user user whose password is being checked
     * @param password password that we're checking against
     * @return whether the passwords match or not
     */
    public boolean checkPassword(User user, String password) throws UserDoesNotExist {
        //Fetches the database
        MongoDatabase db = connection.getDatabase("userLoginsDB");
        MongoCollection<Document> userLogins = db.getCollection("userLoginsCollection");

        //Finds the user's record on the database
        Document query = new Document("_id", user.username());
        Document databaseUser = userLogins.find(query).first();
        if (databaseUser == null) throw new UserDoesNotExist();

        //Checks if the passwords match
        Binary binaryPassword = (Binary) databaseUser.get("password");
        return Arrays.equals(binaryPassword.getData(), hashPassword(password));
    }

    /**
     * Updates a user's password on the database
     * @param user user whose password is being updated
     * @param password new password of this user
     * @throws UserDoesNotExist exception thrown when the user we're trying to delete doesn't exist
     */
    public void changePassword(User user, String password) throws UserDoesNotExist {
        //Fetches the database
        MongoDatabase db = connection.getDatabase("userLoginsDB");
        MongoCollection<Document> userLogins = db.getCollection("userLoginsCollection");

        //Finds the user's record on the database
        Document query = new Document("_id", user.username());
        Document databaseUser = userLogins.find(query).first();
        if (databaseUser == null) throw new UserDoesNotExist();

        //Updates the user's password on the database
        Document update = new Document("$set", new Document("password", hashPassword(password)));
        userLogins.updateOne(databaseUser, update);
    }
}