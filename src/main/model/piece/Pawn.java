package model.piece;

import model.Vector;
import model.Team;

public class Pawn extends Piece {

    // TODO specifications
    public Pawn(Team team) {
        super(team);
    }

    // TODO specifications
    public Pawn(int x, int y, Team team) {
        super(x, y, team);
        moves.add(new Vector(0, -1));
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
        return "P_" + teamLetter;
    }

    // TODO specifications
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

    @Override
    public String getLetter() {
        return "P";
    }
}
