package model.piece;

import model.Vector;
import model.Team;

import java.util.ArrayList;
import java.util.List;

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

    // EFFECTS: creates a piece on given team at given position
    public Piece(int x, int y, Team team) {
        posX = x;
        posY = y;
        this.team = team;
        moves = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: moves the piece's x by dx and y by dy
    public void move(int dx, int dy) {
        posX += dx;
        posY += dy;
    }

    // MODIFIES: this
    // EFFECTS: moves the piece's x and y to the given vector
    public void move(Vector vec) {
        posX = vec.getXcomp();
        posY = vec.getYcomp();
    }

    // GETTERS

    public Vector getPosVec() {
        return new Vector(posX, posY);
    }

    public Team getTeam() {
        return team;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    // SETTERS


    // MODIFIES: this
    // EFFECTS: places piece at given position
    public void placePiece(int x, int y) {
        posX = x;
        posY = y;
    }

    // MODIFIES: this
    // EFFECTS: places piece at given position according to vector sets state of piece to not captured
    public void placePiece(Vector vec) {
        posX = vec.getXcomp();
        posY = vec.getYcomp();
    }

    // MODIFIES: this
    // EFFECTS: sets team of piece to given team
    public void setTeam(Team team) {
        this.team = team;
    }

    public void setOppositeTeam() {
        switch (team) {
            case WHITE:
                team = Team.BLACK;
                break;
            case BLACK:
                team = Team.WHITE;
                break;
        }
    }

    // TODO specifications
    public boolean validMove(Vector movePos) {
        Vector oldMoveVec = getPosVec().subVector(movePos);
        for (Vector move : moves) {
            if (team == Team.BLACK) {
                move.flipDirection();
            }
            int count = 0;
            while (true) {
                Vector newMoveVec = move.subVector(oldMoveVec);
                count++;
                if (newMoveVec.isZero() && count <= magnitude) {
                    return true;
                } else if (count > magnitude) {
                    break;
                } else if (!newMoveVec.isStrictlySmaller(oldMoveVec)
                        || !newMoveVec.hasSwitchedDirections(oldMoveVec)) {
                    break;
                }
                oldMoveVec = newMoveVec;
            }
        }
        return false;
    }

    // TODO specifications
    public boolean canTake(Piece otherPiece) {
        if (otherPiece == null) {
            return true;
        } else if (this.team != otherPiece.getTeam()) {
            return true;
        } else {
            return false;
        }
    }

    public abstract String getLetter();
}
