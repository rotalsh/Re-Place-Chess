package model.piece;

import model.Move;
import model.Team;

public class Pawn extends Piece {

    public Pawn(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Move(0, 1));
        magnitude = 1;
    }
}
