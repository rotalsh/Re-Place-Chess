package model.piece;

import model.Team;
import model.Vector;

// A queen is a piece that can move in all directions except the two diagonal back of magnitude 1
public class Queen extends Piece {

    // EFFECTS: instantiates queen and its team
    public Queen(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the pawn with given x and y positions and its team,
    //          along with its moves in all directions except the two diagonal back of magnitude 1
    public Queen(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
        moves.add(new Vector(0, 1));
        moves.add(new Vector(-1, 0));
        moves.add(new Vector(1, 0));
        moves.add(new Vector(1, -1));
        moves.add(new Vector(-1, -1));
        magnitude = 1;
    }

    // EFFECTS: returns true if given object is a queen of the same team
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Queen)) {
            return false;
        }
        Queen queen = (Queen) obj;
        return queen.getTeam() == this.getTeam();
    }

    // EFFECTS: returns the string "Q"
    @Override
    public String getLetter() {
        return "Q";
    }
}
