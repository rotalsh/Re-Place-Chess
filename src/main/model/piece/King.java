package model.piece;

import model.Team;
import model.Vector;

// A king is a piece that can move in all eight directions at magnitude 1
//  Since it is a piece, it has an x and y position, a team, and a list of moves and magnitude of move
public class King extends Piece {

    // EFFECTS: instantiates king and its team
    public King(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the king with given x and y positions and its team,
    //          along with all eight possible moves of magnitude 1
    public King(int x, int y, Team team) {
        super(x, y, team);
        add(new Vector(0, -1));
        add(new Vector(0, 1));
        add(new Vector(-1, 0));
        add(new Vector(1, 0));
        add(new Vector(1, 1));
        add(new Vector(-1, 1));
        add(new Vector(1, -1));
        add(new Vector(-1, -1));
        magnitude = 1;
    }

    // EFFECTS: returns the string "K"
    @Override
    public String getLetter() {
        return "K";
    }
}
