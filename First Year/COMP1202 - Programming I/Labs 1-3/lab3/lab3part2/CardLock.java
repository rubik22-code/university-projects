package lab3part2;

public class CardLock {

    SmartCard lastSmartCardSeen;
    Boolean unlock;

    Boolean studentAccess = false;

    public void swipeCard(SmartCard owner) {
        lastSmartCardSeen = owner;
    }

    public SmartCard getLastCardSeen() {
        return lastSmartCardSeen;
    }

    public Boolean isUnlocked() {
        if (lastSmartCardSeen.isStaff() == true || studentAccess == true) {
            unlock = true;
        }
        else {
            unlock = false;
        }
        return unlock;

    }

    public void toggleStudentAccess() {
        studentAccess = !studentAccess;
    }

}
