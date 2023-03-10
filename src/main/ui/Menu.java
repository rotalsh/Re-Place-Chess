package ui;

import model.Board;
import persistence.JsonReader;

import java.io.IOException;
import java.util.Scanner;

// Persistence portion modeled after WorkRoomApp in https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Menu for the game - can start game, look at rules, or quit application
public class Menu {
    private static final String JSON_STORE = "./data/game.json";
    private Scanner scanner;
    private boolean keepGoing;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: runs the menu
    public Menu() {
        runMenu();
    }

    // took ideas from the TellerApp
    // MODIFIES: this
    // EFFECTS: processes user input
    public void runMenu() {
        scanner = new Scanner(System.in);
        jsonReader = new JsonReader(JSON_STORE);
        keepGoing = true;
        while (keepGoing) {
            System.out.print("Press n for new game, l to load saved game, ");
            System.out.println("r for rules and how to play, and q to quit.");
            String input = scanner.next();
            menuInterpret(input);
        }
    }

    // MODIFIES: this
    // EFFECTS: interprets user input
    public void menuInterpret(String input) {
        switch (input) {
            case "q":
                keepGoing = false;
                break;
            case "n":
                keepGoing = false;
                new Game(new Board());
                break;
            case "r":
                printRules();
                printHowToPlay();
                break;
            case "l":
                keepGoing = false;
                loadBoard();
                break;
            default:
                System.out.println("That is not a recognized command.");
                break;
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
        System.out.print("A player wins if they capture their opponent's king, or if their king is able to stay");
        System.out.println(" a turn on the enemy's back row without getting captured.");
    }

    // EFFECTS: prints out how to play the game / how to input moves to terminal
    public void printHowToPlay() {
        System.out.println("\nTo make a move, type your move into the terminal when it's your turn in this format:");
        System.out.println("PieceLetter(extraDeterminer)PlaceToMoveTo.");
        System.out.println("So, for example, if I wanted to move my pawn to the space b3, I would type Pb3.");
        System.out.println("The extraDeterminer is required if a player is in possession of more than one piece");
        System.out.println("of the same type that can move to the same space.");
        System.out.print("For example, if I have two rooks, one on a2 and on c2, and I wanted to move the one on");
        System.out.println("a2 to b2, I would have to type Rab2.");
        System.out.print("Similarly, if I have two rooks, one on b1 and one on b3, and i wanted to move the one on");
        System.out.println("b1 to b2, I would have to type R1b2.");
        System.out.println("To place a piece down, use the format:");
        System.out.println("@PieceLetterPlaceToMoveTo.");
        System.out.println("So, if I wanted to place a pawn down at a2, I would type @Pa2.\n");
    }


    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadBoard() {
        try {
            Board bd = jsonReader.read();
            System.out.println("Loaded state of board from " + JSON_STORE);
            System.out.println("Moves made in loaded game " + bd.movesToString());
            new Game(bd);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
