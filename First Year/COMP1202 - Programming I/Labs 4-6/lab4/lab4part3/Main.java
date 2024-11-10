package lab4part3;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        UserGroup users = new UserGroup();
        users.addSampleData();
        users.printUsernames();

        Iterator<User> userIterator = users.getUserIterator();

        UserGroup administrators = new UserGroup();
        administrators.printUsernames();

        while(userIterator.hasNext()) {
            User currentUser = userIterator.next();

            if (currentUser.getUserType().equals("admin")) {
                administrators.getUsers().add(currentUser);
            }
        }
        ArrayList<User> allUsersAdmin = administrators.getUsers();

        allUsersAdmin.get(allUsersAdmin.size() - 1).setUserType("user"); // Inconsistency as userType in administrator is "user".

        System.out.println(users);
        System.out.println(administrators);

    }
}
