package ui;

import model.Board;
import model.Vector;
import model.piece.*;

import java.util.Scanner;

public class Game {
    private Scanner scanner;
    private Board board;
    private boolean keepPlaying;

    public Game() {
        runGame();
    }

    private void runGame() {
        this.board = new Board();
        keepPlaying = true;
        while (keepPlaying) {
            System.out.println(board);
            System.out.println(board.getTurn() + "'s turn.");
            scanner = new Scanner(System.in);
            String input = scanner.next();
            interpret(input);
            quitPlayingIfGameOver();
        }
        if (board.isGameOver()) {
            gameOverText();
        }
    }

    public void quitPlayingIfGameOver() {
        if (board.isGameOver()) {
            keepPlaying = false;
        }
    }

    public void gameOverText() {
        System.out.println(board);
        System.out.println(board.notTurn() + " has captured " + board.getTurn() + "'s king.");
        System.out.println(board.notTurn() + " wins!");
    }

    public void interpret(String input) {
        if (input.equals("q")) {
            keepPlaying = false;
        } else if (String.valueOf(input.charAt(0)).equals("@")) {
            placeInterpret(input);
        } else {
            moveInterpret(input);
        }
    }

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

    // TODO specifications
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

    // TODO specifications
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
        if (startingX >= 0 && startingX <= board.getBoardWidth()) {
            piecePos = board.getPiecePosFromColumn(piece, new Vector(x, y), startingX);
        } else if (startingY >= 0 && startingY <= board.getBoardHeight()) {
            piecePos = board.getPiecePosFromRow(piece, new Vector(x, y), startingY);
        } else {
            System.out.println("Invalid notation! Your piece cannot be there.");
            return;
        }
        moveFromPiecePos(piecePos, new Vector(x, y));
    }

    public void moveFromPiecePos(Vector piecePos, Vector movePos) {
        if (piecePos == null) {
            System.out.println("Invalid move! You do not have this piece on the board,");
            System.out.println("you do not have a piece of this type that can move to this square,");
            System.out.println("or you have two of this piece that can move to this square.");
        } else {
            if (!board.moveFoundPiece(piecePos, movePos)) {
                System.out.println("Invalid move! You cannot move your piece there.");
            }
        }
    }

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

    // TODO
    public int getXPlace(char c) {
        return (c - 97);
    }

    // TODO
    public int getYPlace(char c) {
        return (48 + board.getBoardHeight() - c);
    }

    // TODO
    public Piece getPiece(String piece) {
        if (piece.equals("K")) {
            return new King(board.getTurn());
        } else if (piece.equals("Q")) {
            return new Queen(board.getTurn());
        } else if (piece.equals("B")) {
            return new Bishop(board.getTurn());
        } else if (piece.equals("P")) {
            return new Pawn(board.getTurn());
        } else if (piece.equals("R")) {
            return new Rook(board.getTurn());
        } else {
            return null;
        }
    }
}
