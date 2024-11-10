/*

Error: Asked for brackets for equal and comparsion IF statements.

Error: numberToGuess = new Integer(10); has been deprecated, so switched to "numberToGuess = 10;".

Error: Cannot find symbol in "numberToGuess = myToolbox.getRandomInteger(10);", so placed variable assignment after creating Toolbox.

*/

public class GuessingGame { // This writes the class stub called GuessingGame, where the file must also be called this.

	public static void main(String[] args){ // This is the main method stub.

		Integer numberToGuess; // Defines integers.
		Integer guessedNumber;

		Toolbox myToolbox = new Toolbox(); // Makes a Toolbox

		numberToGuess = myToolbox.getRandomInteger(10); // Assigning variable to random value.

		System.out.println("Welcome to the Guessing Game!"); // This prints a line with text.

		guessedNumber = myToolbox.readIntegerFromCmd();

		if (numberToGuess.equals(guessedNumber)) { // IF statement for two variables to equal.

			System.out.println("right");
		}

		if (numberToGuess > guessedNumber) { // IF statement if guess is too low.

			System.out.println("too low");
		}
		
		if (numberToGuess < guessedNumber) { // IF statement if guess is too high.

			System.out.println("too high");
		}
		

	}

}