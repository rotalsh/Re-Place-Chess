package model.piece;

import model.Team;
import model.Vector;

public class Bishop extends Piece {
    public Bishop(Team team) {
        super(team);
    }

    public Bishop(int x, int y, Team team) {
        super(x, y, team);
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
        return "B_" + teamLetter;
    }

    // TODO specifications
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
}
