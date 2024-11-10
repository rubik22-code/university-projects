package lab4part2;

public class User {
    private String username;
    private String userType;
    private String name;

    public User(String myUserName, String myUserType, String firstName) {
        username = myUserName;
        userType = myUserType;
        name = firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public void setUserType(String myUserType) {
        this.userType = myUserType;
    }

}
