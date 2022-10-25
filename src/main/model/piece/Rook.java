package model.piece;

import model.Team;
import model.Vector;

// A rook is a piece that can move in the four cardinals at magnitude 1
//  Since it is a piece, it has an x and y position, a team, and a list of moves and magnitude of move
public class Rook extends Piece {

    // EFFECTS: instantiates rook and its team
    public Rook(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the rook with given x and y positions and its team,
    //          along with its moves in the four cardinal directions of magnitude 1
    public Rook(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
        moves.add(new Vector(0, 1));
        moves.add(new Vector(-1, 0));
        moves.add(new Vector(1, 0));
        magnitude = 1;
    }

    // EFFECTS: returns the string "R"
    @Override
    public String getLetter() {
        return "R";
    }
}
