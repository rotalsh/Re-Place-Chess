package model;

import model.piece.*;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private Piece[][] boardPieces;
    private List<Piece> capturedWhitePieces;
    private List<Piece> capturedBlackPieces;
    private boolean gameOver;

    // EFFECTS: creates a new board at the start of the game, with pieces at their respective positions,
    //          no captured pieces, a horizontal size of 3 and vertical of 4
    public Board() {
        boardPieces = new Piece[][]{
                {new Rook(0, 0, Team.BLACK),
                        new King(1, 0, Team.BLACK),
                        new Bishop(2, 0, Team.BLACK)},
                {null, new Pawn(1, 1, Team.BLACK), null},
                {null, new Pawn(1, 2, Team.WHITE), null},
                {new Bishop(0, 3, Team.WHITE),
                        new King(1, 3, Team.WHITE),
                        new Rook(2, 3, Team.WHITE)}};
        capturedWhitePieces = new ArrayList<>();
        capturedBlackPieces = new ArrayList<>();
        gameOver = false;
    }

    // TODO specifications
    public boolean movePiece(Piece piece, Vector movePos) {
        Vector piecePos = getPiecePos(piece);
        if (piecePos == null) {
            return false;
        }
        Piece currPiece = boardPieces[piecePos.getYcomp()][piecePos.getXcomp()];
        Piece pieceAtMovePos = boardPieces[movePos.getYcomp()][movePos.getXcomp()];
        if (currPiece.validMove(movePos) && currPiece.canTake(pieceAtMovePos)) {
            boardPieces[movePos.getYcomp()][movePos.getXcomp()] = currPiece;
            boardPieces[piecePos.getYcomp()][piecePos.getXcomp()] = null;
            if (pieceAtMovePos != null) {
                pieceAtMovePos.setCaptured();
                pieceAtMovePos.setOppositeTeam();
                placeInCaptured(pieceAtMovePos);
            }
            checkPromotion();
            return true;
        } else {
            return false;
        }
    }

    // TODO specifications
    public boolean placePiece(Piece piece, Vector movePos) {
        return false;
    }

    // TODO specifications
    public void checkPromotion() {
        for (int j = 0; j < boardPieces[0].length; j++) {
            Piece piece = boardPieces[0][j];
            if ((piece instanceof Pawn) && piece.getTeam().equals(Team.WHITE)) {
                promotePawn(piece, new Vector(0, j));
            }
        }
        int lastRow = boardPieces.length - 1;
        for (int j = 0; j < boardPieces[lastRow].length; j++) {
            Piece piece = boardPieces[lastRow][j];
            if ((piece instanceof Pawn) && piece.getTeam().equals(Team.BLACK)) {
                promotePawn(piece, new Vector(lastRow, j));
            }
        }
    }

    public void promotePawn(Piece piece, Vector vector) {
        int x = vector.getXcomp();
        int y = vector.getYcomp();
        boardPieces[x][y] = new Queen(x, y, piece.getTeam());
    }

    // TODO specifications
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

    // TODO specifications
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

    // TODO specifications
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
