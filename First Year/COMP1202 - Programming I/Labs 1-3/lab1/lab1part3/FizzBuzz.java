/*

Syntax:

Error: ';' expected in "i = i + 1", so added ";".

Error: Unexpected type in "if (i % 5 = 0)", so changed to "==" as it's equating, not assigning.

Error: Mispelt System.out.println("FizzBuzz"); as System.out.printLn("FizzBuzz");.

Logical:

- Changed i = 0 to i = 1 

- Increment already included at change of state in FOR statement.

- FizzBuzz included, so multiple of 3 and 5.

- Statements appeared multiple times if each condition is reached, so need elif statements.

*/

public class FizzBuzz { // Creates the class stub for the game.

	public static void main(String[] args) { // Creates main method stub.

		for (int i = 1; i <= 60; i++) { // IF statement, starts at one, finished at 60, increments at one.

			if (i % 15 == 0) { // MOD the product of the two factors.

				System.out.println("FizzBuzz");

			} // If not, MOD the count with 3 to identify factor.
	
			else if (i % 3 == 0) {

				System.out.println("Fizz");
		
			} // If not, MOD the count with 5 to identify factor.

			else if (i % 5 == 0) {

				System.out.println("Buzz");
		
			}


			else { // IF not, print the current count.

				System.out.println(i); // This prints a line with text.

			}
    
		}
	 

	}

}