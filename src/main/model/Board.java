package model;

import model.piece.*;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private Piece[][] boardPieces;
    private List<Piece> capturedPieces;
    private boolean gameOver;
    private Team turn;
    private List<String> movesMade;

    // EFFECTS: creates a new board at the start of the game, with pieces at their respective positions,
    //          no captured pieces, a horizontal size of 3 and vertical of 4, and starts as white's turn
    public Board() {
        rePlaceBoardStart();
        capturedPieces = new ArrayList<>();
        movesMade = new ArrayList<>();
        gameOver = false;
        turn = Team.WHITE;
    }

    // MODIFIES: this
    // EFFECTS: sets up the board according to the re-place board start
    public void rePlaceBoardStart() {
        boardPieces = new Piece[][]{
                {new Rook(0, 0, Team.BLACK),
                        new King(1, 0, Team.BLACK),
                        new Bishop(2, 0, Team.BLACK)},
                {null, new Pawn(1, 1, Team.BLACK), null},
                {null, new Pawn(1, 2, Team.WHITE), null},
                {new Bishop(0, 3, Team.WHITE),
                        new King(1, 3, Team.WHITE),
                        new Rook(2, 3, Team.WHITE)}};
    }

    // TODO specifications
    public void addToCapturedPieces(Piece pieceAtMovePos) {
        if (pieceAtMovePos instanceof Queen) {
            capturedPieces.add(new Pawn(pieceAtMovePos.getTeam()));
        } else {
            capturedPieces.add(pieceAtMovePos);
        }
    }

    // TODO specifications
    public void changeTurn() {
        switch (turn) {
            case WHITE:
                turn = Team.BLACK;
                break;
            case BLACK:
                turn = Team.WHITE;
                break;
        }
    }

    // TODO specifications
    public void endGame() {
        King king = new King(notTurn());
        if (capturedPieces.contains(king)) {
            gameOver = true;
        }
    }

    // TODO specifications
    public boolean placePiece(Piece piece, Vector movePos) {
        int x = movePos.getXcomp();
        int y = movePos.getYcomp();
        for (Piece capturedPiece : capturedPieces) {
            if (piece.equals(capturedPiece)) {
                capturedPiece.placePiece(movePos);
                boardPieces[y][x] = capturedPiece;
                capturedPieces.remove(capturedPiece);
                changeTurn();
                return true;
            }
        }
        return false;
    }

    // TODO specifications
    public boolean canPlace(Piece piece, Vector movePos) {
        int x = movePos.getXcomp();
        int y = movePos.getYcomp();
        if (boardPieces[y][x] != null) {
            return false;
        }
        switch (piece.getTeam()) {
            case WHITE:
                if (y == 0) {
                    return false;
                }
                break;
            case BLACK:
                if (y == boardPieces.length - 1) {
                    return false;
                }
                break;
        }
        return true;
    }

    // TODO specifications
    public void checkPromotion() {
        for (int j = 0; j < boardPieces[0].length; j++) {
            Piece piece = boardPieces[0][j];
            if ((piece instanceof Pawn) && piece.getTeam().equals(Team.WHITE)) {
                promotePawn(piece, new Vector(j, 0));
            }
        }
        int lastRow = boardPieces.length - 1;
        for (int j = 0; j < boardPieces[lastRow].length; j++) {
            Piece piece = boardPieces[lastRow][j];
            if ((piece instanceof Pawn) && piece.getTeam().equals(Team.BLACK)) {
                promotePawn(piece, new Vector(j, lastRow));
            }
        }
    }

    public void promotePawn(Piece piece, Vector vec) {
        int x = vec.getXcomp();
        int y = vec.getYcomp();
        boardPieces[y][x] = new Queen(x, y, piece.getTeam());
    }

    // TODO specifications
    public boolean moveFoundPiece(Vector piecePos, Vector movePos) {
        Piece currPiece = boardPieces[piecePos.getYcomp()][piecePos.getXcomp()];
        Piece pieceAtMovePos = boardPieces[movePos.getYcomp()][movePos.getXcomp()];
        if (currPiece.canTake(pieceAtMovePos)) {
            boardPieces[movePos.getYcomp()][movePos.getXcomp()] = currPiece;
            currPiece.move(movePos);
            boardPieces[piecePos.getYcomp()][piecePos.getXcomp()] = null;
            if (pieceAtMovePos != null) {
                pieceAtMovePos.setOppositeTeam();
                addToCapturedPieces(pieceAtMovePos);
            }
            changeTurn();
            checkPromotion();
            endGame();
            return true;
        } else {
            return false;
        }
    }

    // TODO specifications
    public Vector getPiecePos(Piece piece, Vector movePos) {
        Vector vec = null;
        int count = 0;
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                if (piece.equals(boardPieces[i][j]) && boardPieces[i][j].validMove(movePos)) {
                    vec = new Vector(j, i);
                    count++;
                }
            }
        }
        if (count == 1) {
            return vec;
        }
        return null;
    }

    // TODO specifications
    public Vector getPiecePosFromColumn(Piece piece, Vector movePos, int x) {
        Vector vec = null;
        int count = 0;
        if (x >= boardPieces[0].length) {
            return null;
        }

        for (int i = 0; i < boardPieces.length; i++) {
            if (piece.equals(boardPieces[i][x]) && boardPieces[i][x].validMove(movePos)) {
                vec = new Vector(x, i);
                count++;
            }
        }
        if (count == 1) {
            return vec;
        }
        return null;
    }

    // TODO specifications
    public Vector getPiecePosFromRow(Piece piece, Vector movePos, int y) {
        Vector vec = null;
        int count = 0;
        if (y >= boardPieces.length) {
            return null;
        }

        for (int j = 0; j < boardPieces[y].length; j++) {
            if (piece.equals(boardPieces[y][j]) && boardPieces[y][j].validMove(movePos)) {
                vec = new Vector(j, y);
            }
        }
        if (count == 1) {
            return vec;
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
        boardString += capturedPiecesAsString(capturedPieces, Team.BLACK);
        boardString += capturedPiecesAsString(capturedPieces, Team.WHITE);

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
            if (piece.getTeam().equals(team)) {
                piecesString += " " + piece.toString();
            }
        }
        return piecesString + "\n";
    }

    // TODO specifications
    public Team getTurn() {
        return turn;
    }

    // TODO specifications
    public boolean isGameOver() {
        return gameOver;
    }

    public String movesToString() {
        return null; // stub
    }

    public int getBoardHeight() {
        return boardPieces.length;
    }

    public int getBoardWidth() {
        return boardPieces[0].length;
    }

    public Team notTurn() {
        switch (turn) {
            case WHITE:
                return Team.BLACK;
            case BLACK:
                return Team.WHITE;
        }
        return null;
    }
}
