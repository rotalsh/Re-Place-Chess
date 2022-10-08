package model.piece;

import model.Move;
import model.Team;

import java.util.ArrayList;
import java.util.List;

public class Piece {

    protected int posX;
    protected int posY;
    protected boolean captured;
    protected Team team;
    protected List<Move> moves;
    protected int magnitude;

    // EFFECTS: creates a piece on given team at given position
    public Piece(int x, int y, Team team) {
        posX = x;
        posY = y;
        captured = false;
        this.team = team;
        moves = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: moves the piece's x by dx and y by dy
    public void move(int dx, int dy) {
        posX += dx;
        posY += dy;
    }

    // GETTERS

    // EFFECTS: returns true if state of piece is captured;
    public boolean isCaptured() {
        return captured;
    }

    // SETTERS

    // MODIFIES: this
    // EFFECTS: sets state of piece to captured
    public void setCaptured() {
        captured = true;
    }

    // MODIFIES: this
    // EFFECTS: places piece at given position, sets state of piece to not captured
    public void placePiece(int x, int y) {
        posX = x;
        posY = y;
        captured = false;
    }

    // MODIFIES: this
    // EFFECTS: sets team of piece to given team
    public void setTeam(Team team) {
        this.team = team;
    }
}
