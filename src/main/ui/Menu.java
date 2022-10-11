package ui;

import java.util.Scanner;

// Menu for the game - can start game or look at rules
public class Menu {
    private Scanner scanner;
    private boolean keepGoing;

    // MODIFIES: this
    // EFFECTS: runs the menu
    public Menu() {
        runMenu();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runMenu() {
        scanner = new Scanner(System.in);
        keepGoing = true;
        while (keepGoing) {
            System.out.println("Press n for new game, r for rules and how to play, and q to quit.");
            String input = scanner.next();
            menuInterpret(input);
        }
    }

    // MODIFIES: this
    // EFFECTS: interprets user input
    public void menuInterpret(String input) {
        if (input.equals("q")) {
            keepGoing = false;
        } else if (input.equals("n")) {
            keepGoing = false;
            new Game();
        } else if (input.equals("r")) {
            printRules();
            printHowToPlay();
        } else {
            System.out.println("That is not a recognized command.");
        }
    }

    // EFFECTS: prints out the rules of the game
    public void printRules() {
        System.out.println("Each player starts with one pawn, one bishop, one rook, and one king.");
        System.out.println("Each piece can only move one square at a time. The pawn can move only one square forward,");
        System.out.println("the rook can move one square in the four cardinal directions,");
        System.out.print("the bishop only on the four diagonals,");
        System.out.println("and the king can move in all 8 directions.");
        System.out.print("The pawn can be promoted to a queen when it reaches the last row relative to its team. ");
        System.out.println("A queen can move in all directions except the two diagonal back.");
        System.out.print("A piece is captured when the piece of the opposing team is to occupy the same space");
        System.out.println(" after the move is made.");
        System.out.println("Each turn, a player makes a move either by moving one of their existing pieces");
        System.out.println("or by placing one of their captured pieces down. Pieces cannot be placed on top of");
        System.out.println("already placed pieces or at the very last row of the board relative to the player.");
        System.out.println("Captured queens cannot be placed as queens as must be placed as pawns.");
        System.out.println("Players must make a move each turn - a player cannot skip their turn.");
        System.out.println("A player wins if they capture their opponent's king.");
    }

    // EFFECTS: prints out how to play the game / how to input moves to terminal
    public void printHowToPlay() {
        System.out.println("\nTo make a move, type your move into the terminal when it's your turn in this format:");
        System.out.println("PieceLetter(extraDeterminer)placeToMoveTo.");
        System.out.println("So, for example, if I wanted to move my pawn to the space b3, I would type Pb3.");
        System.out.println("The extraDeterminer is required if a player is in possession of more than one piece");
        System.out.println("of the same type that can move to the same space.");
        System.out.print("For example, if I have two rooks, one on a2 and on c2, and I wanted to move the one on");
        System.out.println("a2 to b2, I would have to type Rab2.");
        System.out.print("Similarly, if I have two rooks, one on b1 and one on b3, and i wanted to move the one on");
        System.out.println("b1 to b2, I would have to type R1b2.");
        System.out.println("To place a piece down, use the format:");
        System.out.println("@PieceLetterplaceToMoveTo.");
        System.out.println("So, if I wanted to place a pawn down at a2, I would type @Pa2.\n");
    }
}
