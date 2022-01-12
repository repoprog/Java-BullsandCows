package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();

        System.out.println("Please, enter the secret code's length:");
        String cLength = scanner.nextLine().trim();
        if (!cLength.matches("\\d+")) {
            System.out.println("Error: " + cLength + " isn't a valid number.");
            return;
        } else {
            game.setCodeLength(Integer.parseInt(cLength));
        }

        System.out.println("Input the number of possible symbols in the code:");
        String providedRange = scanner.nextLine().trim();
        if (!providedRange.matches("\\d+")) {
            System.out.println("Error: " + providedRange + " isn't a valid number.");
            return;
        } else {
            game.setSymbolsRange(Integer.parseInt(providedRange));
        }
        game.play();
    }
}

class Game {
    private String[] secretCode;
    private int symbolsRange;
    private int codeLength;

    public void play() {
        if (isCodePropertiesValid()) {
            System.out.println("The secret is prepared: " + showStars() + showRange());
            secretCode = generateCode();
            System.out.println("Okay, let's start a game!");
            startGame();
        }
    }

    public void setSymbolsRange(int symbolsRange) {
        this.symbolsRange = symbolsRange;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public boolean isCodePropertiesValid() {
        if (symbolsRange > 36 || codeLength == 0) {
            System.out.println("Error: code length can't be 0. Max symbol range is 36.");
            return false;
        } else if (codeLength > symbolsRange) {
            System.out.printf("Error: it's not possible to generate a code with a length " +
                    "of %d with %d unique symbols.)\n", codeLength, symbolsRange);
            return false;
        }
        return true;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean finished = false;
        int turns = 0;
        while (!finished) {
            System.out.printf("Turn %s: \n", ++turns);
            String[] guessCode = scanner.nextLine().split("");
            finished = isGameFinished(guessCode);
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }

    public String showStars() {
        StringBuilder stars = new StringBuilder();
        while (codeLength != stars.length()) {
            stars.append("*");
        }
        return stars.toString();
    }

    public String showRange() {
        char symbol = Character.forDigit(symbolsRange - 1, 36);
        //symbolsRange <= 9 ? Character.forDigit(symbolsRange, 10) : (char) ('a' + (symbolsRange - 11));
        return symbolsRange <= 10 ? "(0-" + symbol + ")." : "(0-9, a-" + symbol + ").";
    }

    // search for random, no-repeating chars from symbolsRange eg.(0-1,a-z)
    private String[] generateCode() {
        Random random = new Random();
        StringBuilder secretCode = new StringBuilder();
        while (secretCode.length() < codeLength) {
            char randomChar = Character.forDigit(random.nextInt(symbolsRange), 36);
            boolean firstDigit = secretCode.length() == 0;
            if (firstDigit) {
                secretCode.append(randomChar);
            } else {
                for (int j = 0; j < secretCode.length(); j++) {
                    if (randomChar == secretCode.charAt(j)) {
                        break;
                    } else if (j == secretCode.length() - 1) {
                        secretCode.append(randomChar);
                    }
                }
            }
        }
        return secretCode.toString().split("");
    }

    public boolean isGameFinished(String[] guessCode) {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < guessCode.length; i++) {
            for (int j = 0; j < secretCode.length; j++) {
                if (i == j && guessCode[i].equals(secretCode[j])) {
                    bulls++;
                } else if (guessCode[i].equals(secretCode[j])) {
                    cows++;
                }
            }
        }
        printResult(bulls, cows);
        return bulls == secretCode.length;
    }

    public void printResult(int bulls, int cows) {
        String cowsMsg = String.format(cows > 1 ? "%s cows" : " %s cow", cows);
        String bullsMsg = String.format(bulls > 1 ? "%s bulls" : "%s bull", bulls);
        if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None.");
        } else if (bulls != 0 && cows != 0) {
            System.out.printf("Grade: %s, %s.\n", bullsMsg, cowsMsg);
        } else if (bulls == 0) {
            System.out.printf("Grade: %s.\n ", cowsMsg);
        } else {
            System.out.printf("Grade: %s.\n", bullsMsg);
        }
    }
}