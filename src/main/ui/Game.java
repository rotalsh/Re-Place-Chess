package ui;

import model.Board;
import model.Team;
import model.Vector;
import model.piece.Pawn;

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
                board.movePiece(new Pawn(Team.BLACK), new Vector(1, 2));
            }
        }
    }
}
