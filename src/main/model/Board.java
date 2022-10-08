package model;

import model.piece.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<String, Piece> boardPieces;
    private List<Piece> capturedPieces;
    private int xsize;
    private int ysize;

    // EFFECTS: creates a new board at the start of the game, with pieces at their respective positions,
    //          no captured pieces, a horizontal size of 3 and vertical of 4
    public Board() {
        boardPieces = new HashMap<>();
        boardPieces.put("B2", new Piece(2, 2, Team.WHITE));
        boardPieces.put("B3", new Piece(2, 3, Team.BLACK));
        capturedPieces = new ArrayList<>();
        xsize = 3;
        ysize = 4;
    }
}
