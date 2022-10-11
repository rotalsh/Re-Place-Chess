package model.piece;

import model.Vector;
import model.Team;

// A pawn is a piece that can move in the single forward direction at magnitude 1
//  Since it is a piece, it has an x and y position, a team, and a list of moves and magnitude of move

public class Pawn extends Piece {

    // EFFECTS: instantiates pawn and its team
    public Pawn(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the pawn with given x and y positions and its team,
    //          along with its single move forward of magnitude 1
    public Pawn(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
        magnitude = 1;
    }

    // EFFECTS: returns true if given object is a pawn of the same team, false otherwise
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Pawn)) {
            return false;
        }
        Pawn pawn = (Pawn) obj;
        return pawn.getTeam() == this.getTeam();
    }

    // EFFECTS: returns the string "P"
    @Override
    public String getLetter() {
        return "P";
    }
}
