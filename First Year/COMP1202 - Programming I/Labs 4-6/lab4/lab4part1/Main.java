package lab4part1;

public class Main {
    public static void main(String[] args) {
        Toolbox myToolbox = new Toolbox(); // Makes Toolbox
        int numberToMultiply;
        int numberToSum = 0;
        int sumCounter = 1;
        int multiplierCounter = 0;

        numberToMultiply = myToolbox.readIntegerFromCmd();

        for (int i=0; i<20; i++) {
            multiplierCounter = multiplierCounter + numberToMultiply;
            System.out.println(multiplierCounter);
        }

        while (numberToSum < 500) {
            numberToSum = numberToSum + sumCounter;
            sumCounter = sumCounter + 1;
        }

        System.out.println(sumCounter - 1);

    }
}
