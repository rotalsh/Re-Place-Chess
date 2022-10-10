package ui;

import model.Board;
import model.Team;
import model.Vector;
import model.piece.*;

import java.util.Scanner;

public class Game {
    private Scanner scanner;

    public Game() {
        runGame();
    }

    private void runGame() {
        Board board = new Board();
        while (true) {
            System.out.println(board.toString());
            scanner = new Scanner(System.in);
            String input = scanner.next();
            if (input.equals("a")) {
                board.movePiece(new Pawn(Team.WHITE), new Vector(1, 1));
            } else if (input.equals("aa")) {
                board.movePiece(new Pawn(Team.WHITE), new Vector(1, 0));
            } else if (input.equals("aaa")) {
                board.movePiece(new Pawn(Team.WHITE), new Vector(0, 1));
            } else if (input.equals("b")) {
                board.movePiece(new Rook(Team.BLACK), new Vector(1, 0));
            } else if (input.equals("c")) {
                board.movePiece(new Bishop(Team.BLACK), new Vector(1, 1));
            } else if (input.equals("p")) {
                board.placePiece(new Pawn(Team.WHITE), new Vector(1, 2));
            } else if (input.equals("k")) {
                board.placePiece(new Pawn(Team.WHITE), new Vector(1, 3));
            }
        }
    }
}
