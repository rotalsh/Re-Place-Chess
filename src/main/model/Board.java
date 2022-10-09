package model;

import model.piece.Pawn;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private Piece[][] boardPieces;
    private List<Piece> capturedWhitePieces;
    private List<Piece> capturedBlackPieces;
    private final int xsize;
    private final int ysize;

    // EFFECTS: creates a new board at the start of the game, with pieces at their respective positions,
    //          no captured pieces, a horizontal size of 3 and vertical of 4
    public Board() {
        xsize = 3;
        ysize = 4;
        boardPieces = new Piece[][]{
                {null, null, null},
                {null, new Pawn(1, 1, Team.BLACK), null},
                {null, new Pawn(1, 2, Team.WHITE), null},
                {null, null, null}};
        capturedWhitePieces = new ArrayList<>();
        capturedBlackPieces = new ArrayList<>();
    }

    public boolean movePiece(Piece piece, Vector movePos) {
        Vector piecePos = getPiecePos(piece);
        if (piecePos == null) {
            return false;
        }
        Piece currPiece = boardPieces[piecePos.getYcomp()][piecePos.getXcomp()];
        Piece pieceAtMovePos = boardPieces[movePos.getYcomp()][movePos.getXcomp()];
        if (currPiece.validMove(movePos)) {
            boardPieces[movePos.getYcomp()][movePos.getXcomp()] = currPiece;
            boardPieces[piecePos.getYcomp()][piecePos.getXcomp()] = null;
            if (pieceAtMovePos != null) {
                pieceAtMovePos.setCaptured();
                pieceAtMovePos.setOppositeTeam();
                placeInCaptured(pieceAtMovePos);
            }
            return true;
        } else {
            return false;
        }
    }

    public void placeInCaptured(Piece piece) {
        Team team = piece.getTeam();
        switch (team) {
            case WHITE:
                capturedWhitePieces.add(piece);
                break;
            case BLACK:
                capturedBlackPieces.add(piece);
                break;
        }
    }

    public Vector getPiecePos(Piece piece) {
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                if (piece.equals(boardPieces[i][j])) {
                    return new Vector(j, i);
                }
            }
        }
        return null;
    }

    // EFFECTS: returns the state of the board as a string
    @Override
    public String toString() {
        String boardString = "";
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                boardString += "| ";
                if (boardPieces[i][j] == null) {
                    boardString += "   ";
                } else {
                    boardString += boardPieces[i][j].toString();
                }
                boardString += " ";
            }
            boardString += "|\n";
        }
        boardString += capturedPiecesAsString(capturedBlackPieces, Team.BLACK);
        boardString += capturedPiecesAsString(capturedWhitePieces, Team.WHITE);

        return boardString;
    }

    public static String capturedPiecesAsString(List<Piece> pieces, Team team) {
        String piecesString = "";
        switch (team) {
            case WHITE:
                piecesString += "White's captured pieces:";
                break;
            case BLACK:
                piecesString += "Black's captured pieces:";
                break;
        }
        for (Piece piece : pieces) {
            piecesString += " " + piece.toString();
        }
        return piecesString + "\n";
    }
}
