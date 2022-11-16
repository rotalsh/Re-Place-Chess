package model.piece;

import model.Vector;
import model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// An abstract representation of a piece, with an X and Y position, a team,
//  and the direction and magnitude of the moves it can make
public abstract class Piece {

    protected int posX;
    protected int posY;
    protected Team team;
    protected List<Vector> moves;
    protected int magnitude;

    // EFFECTS: instantiates a piece and its team
    public Piece(Team team) {
        this.team = team;
    }

    // EFFECTS: creates a piece on given team at given position with a list of empty moves
    public Piece(int x, int y, Team team) {
        posX = x;
        posY = y;
        this.team = team;
        moves = new ArrayList<>();
    }


    // REQUIRES: movePos is not null
    // EFFECTS: returns true if moving to movePos is a valid move for the piece, false otherwise
    public boolean validMove(Vector movePos) {
        Vector oldMoveVec = getPosVec().subVector(movePos);
        for (Vector move : moves) {
            Vector myMove = new Vector(move.getXcomp(), move.getYcomp());
            if (team == Team.BLACK) {
                myMove.reverseDirection();
            }
            int count = 0;
            while (true) {
                Vector newMoveVec = myMove.subVector(oldMoveVec);
                count++;
                if (newMoveVec.isZero() && count <= magnitude) {
                    return true;
                } else if (count > magnitude) {
                    break;
                } else if (!newMoveVec.isStrictlySmaller(oldMoveVec) || newMoveVec.hasSwitchedDirections(oldMoveVec)) {
                    break;
                }
                oldMoveVec = newMoveVec;
            }
        }
        return false;
    }

    // EFFECTS: returns true if this piece can take otherPiece, false otherwise
    public boolean canTake(Piece otherPiece) {
        if (otherPiece == null) {
            return true;
        } else if (this.team != otherPiece.getTeam()) {
            return true;
        } else {
            return false;
        }
    }

    // GETTERS

    // EFFECTS: returns the letter associated with the piece
    public abstract String getLetter();

    // EFFECTS: returns the position of the piece as a vector
    public Vector getPosVec() {
        return new Vector(posX, posY);
    }

    // EFFECTS: returns the team of the piece
    public Team getTeam() {
        return team;
    }

    // EFFECTS: returns the X position of the piece
    public int getPosX() {
        return posX;
    }

    // EFFECTS: returns the Y position of the piece
    public int getPosY() {
        return posY;
    }

    // EFFECTS: returns the moves of the piece
    public List<Vector> getMoves() {
        return moves;
    }

    // EFFECT: returns the magnitude of the piece
    public int getMagnitude() {
        return magnitude;
    }

    // SETTERS

    // REQUIRES: vec is not null
    // MODIFIES: this
    // EFFECTS: places piece at given position vector
    public void placePiece(Vector vec) {
        posX = vec.getXcomp();
        posY = vec.getYcomp();
    }

    // MODIFIES: this
    // EFFECTS: sets the team of piece from white to black and vice versa
    public void setOppositeTeam() {
        if (team.equals(Team.WHITE)) {
            team = Team.BLACK;
        } else {
            team = Team.WHITE;
        }
    }

    // EFFECTS: returns the piece in string format, as PieceLetter_TeamLetter
    @Override
    public String toString() {
        String teamLetter;
        if (team.equals(Team.WHITE)) {
            teamLetter = "W";
        } else {
            teamLetter = "B";
        }
        return getLetter() + "_" + teamLetter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeam());
    }

    // EFFECTS: returns true if given object is a piece of the same team, false otherwise
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return getTeam() == piece.getTeam();
    }
}
