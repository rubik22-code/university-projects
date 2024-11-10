/*

Forget ; throughout go() method.

Forget ; for "balance = balance - withdraw;"

*/

public class ATM { // Creates the class for the ATM.

	Toolbox myToolbox = new Toolbox(); // Makes a Toolbox

	private int balance = 0;

	public static void main (String[] args) { // Creates the main method for ATM.
		ATM myATM = new ATM(); // Create ATM object.
		myATM.go(); // Call a method called go().
	}

	public void go() { // Go method.

		System.out.println("Welcome to online ATM banking"); // PRINT statements.
		System.out.println("How much do you want in your account?");

		balance = myToolbox.readIntegerFromCmd(); // User input.
		
		System.out.println("What do you want to do?");
		System.out.println("1 : Withdraw");
		System.out.println("2 : Deposit");
		System.out.println("3 : Inquire");
		System.out.println("4 : Quit");

		int option = myToolbox.readIntegerFromCmd();

		if (option == 1) {

			withdraw();

		}

		if (option == 2) {

			deposit();

		}

		if (option == 3) {

			inquire();

		}

		if (option == 4) {

			quit();

		}

	}

	public void withdraw() { // Withdraws the balance (method).

		System.out.println("*****************************************\n" + "              Withdrawal\n"+ "*****************************************");

		System.out.println("How much would you like to withdraw?");

		int withdraw = myToolbox.readIntegerFromCmd();

		balance = balance - withdraw;

		System.out.println("*****************************************\n" + "          Your new balance is " + balance + "\n*****************************************");

	}

	public void deposit() { // Despoits the balance (method).

		System.out.println("*****************************************\n" + "              Deposit\n"+ "*****************************************");

		System.out.println("How much would you like to deposit?");

		int deposit = myToolbox.readIntegerFromCmd();

		balance = balance + deposit;

		System.out.println("*****************************************\n" + "          Your new balance is " + balance + "\n*****************************************");

	}

	public void inquire() { // Inquires the balance (method).

		System.out.println("*****************************************\n" + "          Your new balance is " + balance + "\n*****************************************");

	}

	public void quit() { // Quits the program (method).

		System.out.println("*****************************************\n" + "          GoodBye!" + "\n*****************************************");

		return;

	}



}