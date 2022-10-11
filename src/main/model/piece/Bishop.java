package model.piece;

import model.Team;
import model.Vector;

// A bishop is a piece that can move in the four diagonals at magnitude 1
public class Bishop extends Piece {

    // EFFECTS: instantiates bishop and its team
    public Bishop(Team team) {
        super(team);
    }

    // EFFECTS: instantiates the bishop with given x and y positions and its team,
    //          along with its four diagonal moves of magnitude 1
    public Bishop(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(1, 1));
        moves.add(new Vector(-1, 1));
        moves.add(new Vector(1, -1));
        moves.add(new Vector(-1, -1));
        magnitude = 1;
    }


    // EFFECTS: returns true if given object is a bishop of the same team
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Bishop)) {
            return false;
        }
        Bishop bishop = (Bishop) obj;
        return bishop.getTeam() == this.getTeam();
    }

    // EFFECTS: returns the string "B"
    @Override
    public String getLetter() {
        return "B";
    }
}
