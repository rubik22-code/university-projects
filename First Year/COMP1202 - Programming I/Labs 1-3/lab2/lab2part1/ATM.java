/*

Forget ; throughout go() method.

Forget ; for "balance = balance - withdraw;"

*/

public class ATM { // Creates the class for the ATM.

	Toolbox myToolbox = new Toolbox(); // Makes a Toolbox

	public static void main (String[] args) { // Creates the main method for ATM.
		ATM myATM = new ATM(); // Create ATM object.
		myATM.go(); // Call a method called go().
	}

	public void go() { // Go method.

		System.out.println("Welcome to online ATM banking"); // PRINT statements.
		System.out.println("How much do you want in your account?");

		int balance = myToolbox.readIntegerFromCmd(); // User input.
		
		System.out.println(balance);

	}
}