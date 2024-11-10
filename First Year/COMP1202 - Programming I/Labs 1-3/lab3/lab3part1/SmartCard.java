/*
Smart card, a card lock, and a door. Each will require a class.
*/

package lab3part1;

public class SmartCard { // Class for SmartCard.
    String owner; // Class attribute
    Boolean staff = false;
    public SmartCard(String myOwner) { // Class constructor for SmartCard.
        owner = myOwner; // Value for class attribute.
    }

    public static void main (String[] args) {
        SmartCard newSmartCard = new SmartCard("Anna Undergrad"); // Creating a new smartcard object.
        newSmartCard.getOwner(); // Calling a method to get the owner's name.
    }
    public String getOwner() { // Printing the owner's name.
        return owner;
    }

    public Boolean isStaff() {
        return staff;
    }

    public Boolean setStaff(Boolean newStaff) {
        staff = newStaff;
        return staff;
    }
}
