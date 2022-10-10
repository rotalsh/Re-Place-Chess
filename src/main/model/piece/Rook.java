package model.piece;

import model.Team;
import model.Vector;

public class Rook extends Piece {

    // TODO specifications
    public Rook(Team team) {
        super(team);
    }

    // TODO specifications
    public Rook(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
        moves.add(new Vector(0, 1));
        moves.add(new Vector(-1, 0));
        moves.add(new Vector(1, 0));
        magnitude = 1;
    }

    // TODO specifications
    @Override
    public String toString() {
        String teamLetter = "";
        switch (team) {
            case WHITE:
                teamLetter = "W";
                break;
            case BLACK:
                teamLetter = "B";
                break;
        }
        return "R_" + teamLetter;
    }

    // TODO specifications
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Rook)) {
            return false;
        }
        Rook rook = (Rook) obj;
        return rook.getTeam() == this.getTeam();
    }
}
