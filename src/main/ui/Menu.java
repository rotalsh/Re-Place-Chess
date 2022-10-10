package ui;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private boolean keepGoing;

    public Menu() {
        runMenu();
    }

    public void runMenu() {
        scanner = new Scanner(System.in);
        keepGoing = true;
        while (keepGoing) {
            System.out.println("Press n for new game, r for rules and how to play, and q to quit.");
            String input = scanner.next();
            menuInterpret(input);
        }
    }

    public void menuInterpret(String input) {
        if (input.equals("q")) {
            keepGoing = false;
        } else if (input.equals("n")) {
            keepGoing = false;
            new Game();
        } else if (input.equals("r")) {
            System.out.println("Work in progress.");
        } else {
            System.out.println("That is not a recognized command.");
        }
    }
}
