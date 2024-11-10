package lab4part3;

import java.util.ArrayList;
import java.util.Iterator;

public class UserGroup {

    int indexOfUser = 0;

    String removeUserVar;

    ArrayList<User> users;

    public UserGroup () {
        users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addSampleData() {

        User newUser1 = new User("pat1", "user", "patA");
        User newUser2 = new User("pat2", "editor", "patB");
        User newUser3 = new User("pat3", "admin", "patC");
        User newUser4 = new User("pat4", "user", "patD");
        User newUser5 = new User("pat5", "editor", "patE");
        User newUser6 = new User("pat6", "admin", "patF");
        User newUser7 = new User("pat7", "user", "patG");
        User newUser8 = new User("pat8", "editor", "patH");
        User newUser9 = new User("pat9", "admin", "patI");
        User newUser10 = new User("pat10", "user", "patJ");

        users.add(newUser1);
        users.add(newUser2);
        users.add(newUser3);
        users.add(newUser4);
        users.add(newUser5);
        users.add(newUser6);
        users.add(newUser7);
        users.add(newUser8);
        users.add(newUser9);
        users.add(newUser10);
    }

    public User getUser(int indexOfUser) {
        return users.get(indexOfUser);
    }

    public void printUsernames() {
        for (User i : users) {
            System.out.println(i.getUsername() + " " + i.getUserType());
        }
    }

    public void removeFirstUser() {
        users.remove(0);
    }

    public void removeLastUser() {
        int sizeOfUsers = users.size();
        users.remove(sizeOfUsers - 1);
    }

    public void removeUser(String removeUserVar) {

        Iterator<User> tempUserIterator = users.iterator();

        while (tempUserIterator.hasNext()) {
            User i = tempUserIterator.next();

            if (i.getUsername().equals(removeUserVar)) {
                tempUserIterator.remove();
            }
        }
    }
    public Iterator<User> getUserIterator() {
        return users.iterator();
    }
}



