package model.piece;

import model.Team;
import model.Vector;

// A bishop is a piece that can move in the four diagonals at magnitude 1
//  Since it is a piece, it has an x and y position, a team, and a list of moves and magnitude of move
public class Bishop extends Piece {

    // EFFECTS: instantiates bishop and its team
    public Bishop(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the bishop with given x and y positions and its team,
    //          along with its four diagonal moves of magnitude 1
    public Bishop(int x, int y, Team team) {
        super(x, y, team);
        add(new Vector(1, 1));
        add(new Vector(-1, 1));
        add(new Vector(1, -1));
        add(new Vector(-1, -1));
        magnitude = 1;
    }

    // EFFECTS: returns the string "B"
    @Override
    public String getLetter() {
        return "B";
    }
}
