package ui;

import model.Board;
import model.Vector;
import model.piece.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Persistence portion modeled after WorkRoomApp in https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Game application UI
public class Game {
    private static final String JSON_STORE = "./data/game.json";
    private Scanner scanner;
    private Board board;
    private boolean keepPlaying;
    private JsonWriter jsonWriter;

    // MODIFIES: this
    // EFFECTS: initializes fields, makes new board, don't run game application
    public Game() {
        board = new Board();
    }

    // MODIFIES: this
    // EFFECTS: initializes fields, takes given board, runs the game application
    public Game(Board board) {
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        this.board = board;
        keepPlaying = true;
        runGame();
    }

    // MODIFIES: this
    // EFFECTS: processes user input, gives information on how game ended and allows user to save board
    private void runGame() {
        System.out.println("Press m in game to see list of moves made in game so far.");
        while (keepPlaying) {
            System.out.print(board);
            System.out.println(capitalizeFirstOnly(board.getTurn().name()) + "'s turn.");
            String input = scanner.next();
            interpret(input);
            quitPlayingIfGameOver();
        }
        if (board.getGameState() % 2 == 1) {
            gameEnded();
        } else {
            saveBoard();
        }
    }

    // MODIFIES: this
    // EFFECTS: makes the application stop once the game is over
    public void quitPlayingIfGameOver() {
        if (board.getGameState() % 2 == 1) {
            keepPlaying = false;
        }
    }

    // EFFECTS: gives information about how the game ended
    public void gameEnded() {
        if (board.getGameState() == 1) {
            kingCapturedText();
        } else {
            kingInEnemyLinesText();
        }
        System.out.println("Moves made:");
        System.out.println(board.movesToString());
    }

    // EFFECTS: gives information about who won when the game ends by king capture
    public void kingCapturedText() {
        System.out.println(board);
        System.out.print(capitalizeFirstOnly(board.notTurn().name()) + " has captured ");
        System.out.println(capitalizeFirstOnly(board.getTurn().name()) + "'s king.");
        System.out.println(capitalizeFirstOnly(board.notTurn().name()) + " wins!");
    }

    // EFFECTS: gives information about who won when the game ends by king staying in enemy lines for a turn
    public void kingInEnemyLinesText() {
        System.out.println(board);
        System.out.print(capitalizeFirstOnly(board.getTurn().name()) + "'s king has stayed in enemy lines ");
        System.out.println("for one turn.");
        System.out.println(capitalizeFirstOnly(board.getTurn().name()) + " wins!");
    }

    // MODIFIES: this
    // EFFECTS: interprets user input
    public void interpret(String input) {
        if (input.equals("q")) {
            keepPlaying = false;
        } else if (input.equals("m")) {
            System.out.println("Moves made:");
            System.out.println(board.movesToString());
        } else if (String.valueOf(input.charAt(0)).equals("@")) {
            placeInterpret(input);
        } else {
            moveInterpret(input);
        }
    }

    // MODIFIES: this
    // EFFECTS: interprets a user move
    public void moveInterpret(String input) {
        if (input.length() > 4 || input.length() < 3) {
            System.out.println("Invalid notation! You've formatted your move incorrectly.");
        }
        Piece piece = getPiece(String.valueOf(input.charAt(0)));
        if (piece == null) {
            System.out.println("Invalid notation! That is not a piece.");
        } else if (input.length() == 3) {
            moveInterpretThreeLetters(input, piece);
        } else {
            moveInterpretFourLetters(input, piece);
        }
    }

    // REQUIRES: input is 3 characters long
    // MODIFIES: this
    // EFFECTS: interpret a user move that is specifically three letters long
    public void moveInterpretThreeLetters(String input, Piece piece) {
        int x = getXPlace(input.charAt(1));
        int y = getYPlace(input.charAt(2));
        if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
            System.out.println("Invalid move! This is not a place on the board.");
        } else {
            Vector piecePos = board.getPiecePos(piece, new Vector(x, y));
            moveFromPiecePos(piecePos, new Vector(x, y));
        }
    }

    // REQUIRES: input is 4 characters long
    // MODIFIES: this
    // EFFECTS: interpret a user move that is specifically four letters long
    public void moveInterpretFourLetters(String input, Piece piece) {
        Vector piecePos;
        int x = getXPlace(input.charAt(2));
        int y = getYPlace(input.charAt(3));
        if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
            System.out.println("Invalid move! This is not a place on the board.");
            return;
        }
        int startingX = getXPlace(input.charAt(1));
        int startingY = getYPlace(input.charAt(1));
        if (startingX >= 0 && startingX < board.getBoardWidth()) {
            piecePos = board.getPiecePosFromColumn(piece, new Vector(x, y), startingX);
        } else if (startingY >= 0 && startingY < board.getBoardHeight()) {
            piecePos = board.getPiecePosFromRow(piece, new Vector(x, y), startingY);
        } else {
            System.out.println("Invalid notation! Your piece cannot be there.");
            return;
        }
        moveFromPiecePos(piecePos, new Vector(x, y));
    }

    // REQUIRES: movePos is a position on the board
    // MODIFIES: this
    // EFFECTS: either tells player that their move is invalid or does the move on the board
    public void moveFromPiecePos(Vector piecePos, Vector movePos) {
        if (piecePos == null) {
            System.out.println("Invalid move! You do not have this piece on the board,");
            System.out.println("you do not have a piece of this type that can move to this square,");
            System.out.println("or you have more than one of this piece that can move to this square.");
        } else {
            if (!board.moveFoundPiece(piecePos, movePos)) {
                System.out.println("Invalid move! You cannot move your piece there.");
            }
        }
    }

    // REQUIRES: input is not null
    // MODIFIES: this
    // EFFECTS: either tells user their move is invalid or places the piece on the board
    public void placeInterpret(String input) {
        if (input.length() != 4) {
            System.out.println("Invalid notation! You've formatted your move incorrectly.");
        }
        Piece piece = getPiece(String.valueOf(input.charAt(1)));
        if (piece == null) {
            System.out.println("Invalid notation! That is not a piece.");
        } else {
            int x = getXPlace(input.charAt(2));
            int y = getYPlace(input.charAt(3));
            if (x < 0 || x >= board.getBoardWidth() || y < 0 || y >= board.getBoardHeight()) {
                System.out.println("Invalid move! This is not a place on the board.");
            } else if (!board.canPlace(piece, new Vector(x, y))) {
                System.out.println("Invalid move! You cannot place a piece here.");
            } else if (!board.placePiece(piece, new Vector(x, y))) {
                System.out.println("Invalid move! You don't have this piece in your captured pieces.");
            }
        }
    }

    // EFFECTS: returns X position on board from a character
    public int getXPlace(char c) {
        return (c - 97);
    }

    // EFFECTS: returns Y position on board from a character
    public int getYPlace(char c) {
        return (48 + board.getBoardHeight() - c);
    }

    // EFFECTS: returns the piece associated with given string
    public Piece getPiece(String piece) {
        switch (piece) {
            case "K":
                return new King(board.getTurn());
            case "Q":
                return new Queen(board.getTurn());
            case "B":
                return new Bishop(board.getTurn());
            case "P":
                return new Pawn(board.getTurn());
            case "R":
                return new Rook(board.getTurn());
            default:
                return null;
        }
    }

    // EFFECTS: returns current board

    public Board getBoard() {
        return board;
    }

    // MODIFIES: this
    // EFFECTS: sets the textState of board to ts
    public void setBoardTextState(int ts) {
        board.setTextState(ts);
    }


    // EFFECTS: saves the board's moves to file
    private void saveBoard() {
        try {
            jsonWriter.open();
            jsonWriter.write(board);
            jsonWriter.close();
            System.out.println("Saved state of board to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // REQUIRES: str is not null or an empty string
    // EFFECTS: returns a new string which is the old string but only the first letter is capitalized
    public static String capitalizeFirstOnly(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
