package model.piece;

import model.Team;
import model.Vector;

public class King extends Piece {
    public King(Team team) {
        super(team);
    }

    public King(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
        moves.add(new Vector(0, 1));
        moves.add(new Vector(-1, 0));
        moves.add(new Vector(1, 0));
        moves.add(new Vector(1, 1));
        moves.add(new Vector(-1, 1));
        moves.add(new Vector(1, -1));
        moves.add(new Vector(-1, -1));
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
        return "K_" + teamLetter;
    }

    // TODO specifications
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof King)) {
            return false;
        }
        King king = (King) obj;
        return king.getTeam() == this.getTeam();
    }
}
