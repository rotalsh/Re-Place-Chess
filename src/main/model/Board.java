package model;

import model.piece.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ui.Game.capitalizeFirstOnly;

// The board representing the game, keeps track of pieces on the board and in captured, whether the game has ended,
//  whose turn it is, and the list of moves made so far
public class Board {
    private Piece[][] boardPieces;
    private List<Piece> capturedPieces;
    private int gameState;
    private Team turn;
    private Team kingInEnemyLines;
    private List<String> movesMade;
    private List<String> literalMoves;

    // textState represents the way text will be printed
    // 0 is text wrapped, 1 is with line breaks, 2 is no numbers, and 3 is literal moves
    private int textState;

    // EFFECTS: creates a new board at the start of the game, with pieces at their respective positions,
    //          no captured pieces, no moves made, no literal moves made,
    //          a horizontal size of 3 and vertical of 4, and starts as white's turn
    //          gameState is 0 normally, 1 if king captured, 2 if king in enemy lines, 3 if king successfully stays
    public Board() {
        rePlaceBoardStart();
        capturedPieces = new ArrayList<>();
        movesMade = new LinkedList<>();
        literalMoves = new LinkedList<>();
        gameState = 0;
        turn = Team.WHITE;
        textState = 0;
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

    // REQUIRES: pieceAtMovePos is not null
    // MODIFIES: this
    // EFFECTS: add piece to list of captured pieces
    //          if queen, add a pawn instead
    public void addToCapturedPieces(Piece pieceAtMovePos) {
        if (pieceAtMovePos instanceof Queen) {
            capturedPieces.add(new Pawn(0, 0, pieceAtMovePos.getTeam()));
        } else {
            capturedPieces.add(pieceAtMovePos);
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the turn from white to black and vice versa
    public void changeTurn() {
        if (turn.equals(Team.WHITE)) {
            turn = Team.BLACK;
        } else {
            turn = Team.WHITE;
        }
    }

    // MODIFIES: this
    // EFFECTS: ends the game if king is captured or king stays a turn in enemy lines
    public void endGame() {
        King king = new King(notTurn());
        if (capturedPieces.contains(king)) {
            gameState = 1;
        } else if (gameState == 2 && turn.equals(kingInEnemyLines)) {
            gameState = 3;
            movesMade.add("#");
        }
    }

    // REQUIRES: piece is not null, movePos is a position on the board
    // EFFECTS: returns true if the piece can be placed on the given position of the board, false otherwise
    public boolean canPlace(Piece piece, Vector movePos) {
        int x = movePos.getXcomp();
        int y = movePos.getYcomp();
        if (boardPieces[y][x] != null) {
            return false;
        }
        if (piece.getTeam().equals(Team.WHITE)) {
            if (y == 0) {
                return false;
            }
        } else {
            if (y == boardPieces.length - 1) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: piece is not null, movePos is an unoccupied position on the board
    // MODIFIES: this
    // EFFECTS: if a piece of same type as given is in captured, places it on the given position and returns true.
    //          if not, do nothing and return false
    public boolean placePiece(Piece piece, Vector movePos) {
        int x = movePos.getXcomp();
        int y = movePos.getYcomp();
        for (Piece capturedPiece : capturedPieces) {
            if (piece.equals(capturedPiece)) {
                addMove(capturedPiece, movePos);
                capturedPiece.placePiece(movePos);
                boardPieces[y][x] = capturedPiece;
                capturedPieces.remove(capturedPiece);
                changeTurn();
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: promotes a pawn if it is on the last row relative to its team
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

    // REQUIRES: piece is not null, vec is a position on the board
    // MODIFIES: this
    // EFFECTS: replaces a pawn of some team at given vec with a queen of same team at same position
    public void promotePawn(Piece piece, Vector vec) {
        int x = vec.getXcomp();
        int y = vec.getYcomp();
        boardPieces[y][x] = new Queen(x, y, piece.getTeam());
    }

    // MODIFIES: this
    // EFFECTS: checks if king is in enemy lines, if so change state of game
    public void checkKingInEnemyLines() {
        for (int j = 0; j < boardPieces[0].length; j++) {
            Piece piece = boardPieces[0][j];
            if ((piece instanceof King) && piece.getTeam().equals(Team.WHITE)) {
                kingIsInEnemyLines(Team.WHITE);
            }
        }
        int lastRow = boardPieces.length - 1;
        for (int j = 0; j < boardPieces[lastRow].length; j++) {
            Piece piece = boardPieces[lastRow][j];
            if ((piece instanceof King) && piece.getTeam().equals(Team.BLACK)) {
                kingIsInEnemyLines(Team.BLACK);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the team that can win from king staying in enemy lines to given unless already given
    public void kingIsInEnemyLines(Team team) {
        if (gameState != 2) {
            gameState = 2;
            kingInEnemyLines = team;
        }
    }

    // REQUIRES: piecePos and movePos are positions on the board
    // MODIFIES: this
    // EFFECTS: places piece at piecePos on movePos if possible, and if so, capture piece at movePos if there,
    //          add the move to list of moves, change the turn, check for pawn promotion and game end, and return true.
    //          do nothing and return false if piece of same team is at piecePos
    public boolean moveFoundPiece(Vector piecePos, Vector movePos) {
        Piece currPiece = boardPieces[piecePos.getYcomp()][piecePos.getXcomp()];
        Piece pieceAtMovePos = boardPieces[movePos.getYcomp()][movePos.getXcomp()];
        if (currPiece.canTake(pieceAtMovePos)) {
            addMove(currPiece, pieceAtMovePos, movePos);
            boardPieces[movePos.getYcomp()][movePos.getXcomp()] = currPiece;
            currPiece.placePiece(movePos);
            boardPieces[piecePos.getYcomp()][piecePos.getXcomp()] = null;
            if (pieceAtMovePos != null) {
                pieceAtMovePos.setOppositeTeam();
                addToCapturedPieces(pieceAtMovePos);
            }
            changeTurn();
            checkPromotion();
            checkKingInEnemyLines();
            endGame();
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: currPiece is not null, movePos is a position on the board
    // MODIFIES: this
    // EFFECTS: adds the string representation of a placing of a piece into list of moves and to literal moves
    //          and logs an event indicating that a move (piece placement) has been made and added to list of moves
    public void addMove(Piece currPiece, Vector movePos) {
        String pieceLetter = currPiece.getLetter();
        String moveString = vectorToString(movePos);
        String total = "@" + pieceLetter + moveString;
        literalMoves.add(total);
        movesMade.add(total);
        EventLog.getInstance().logEvent(new Event(capitalizeFirstOnly(turn.name()) + "'s move "
                + total + " added."));
    }

    // REQUIRES: currPiece is not null, movePos is a position on the board
    // MODIFIES: this
    // EFFECTS: adds the string representation of a moving of a piece into list of moves
    //          also adds the literal move (no captures/promotion) to list of literal moves
    //          and logs an event indicating that a move has been made and added to list of moves
    public void addMove(Piece currPiece, Piece pieceAtMovePos, Vector movePos) {
        String pieceLetter = currPiece.getLetter();
        String extraLetter = "";
        if (canGetToSameRow(currPiece, movePos)) {
            extraLetter = posXToString(currPiece.getPosX());
        } else if (canGetToSameColumn(currPiece, movePos)) {
            extraLetter = posYToString(currPiece.getPosY());
        } else if (canGetToAnywhere(currPiece, movePos)) {
            extraLetter = posXToString(currPiece.getPosX());
        }
        String captureString = determineString(pieceAtMovePos);
        String captureKing = determineIfCaptureKing(pieceAtMovePos);
        String moveString = vectorToString(movePos);
        String promoteToQueen = determinePromotion(currPiece, movePos);
        String total = pieceLetter + extraLetter + captureString + moveString + promoteToQueen + captureKing;
        String literalTotal = pieceLetter + extraLetter + moveString;
        literalMoves.add(literalTotal);
        movesMade.add(total);
        EventLog.getInstance().logEvent(new Event(capitalizeFirstOnly(turn.name()) + "'s move "
                + total + " added."));
    }

    // REQUIRES: currPiece is not null, movePos is not null
    // EFFECTS: returns the string "=Q" if the piece is a pawn and the move promotes the pawn
    public String determinePromotion(Piece currPiece, Vector movePos) {
        if (!(currPiece instanceof  Pawn)) {
            return "";
        } else if (currPiece.getTeam().equals(Team.WHITE) && movePos.getYcomp() == 0) {
            return "=Q";
        } else if (currPiece.getTeam().equals(Team.BLACK) && movePos.getYcomp() == getBoardHeight() - 1) {
            return "=Q";
        } else {
            return "";
        }
    }

    // EFFECTS: returns "#" if the piece to be captured is a king
    public String determineIfCaptureKing(Piece pieceAtMovePos) {
        King king = new King(notTurn());
        if (king.equals(pieceAtMovePos)) {
            return "#";
        } else {
            return "";
        }
    }

    // REQUIRES: movePos is not null
    // EFFECTS: returns the string representation of a vector position on the board
    public String vectorToString(Vector movePos) {
        String x = posXToString(movePos.getXcomp());
        String y = posYToString(movePos.getYcomp());
        return x + y;
    }

    // EFFECTS: returns string representing of a number as a column on the board
    public String posXToString(int x) {
        return String.valueOf((char) (x + 97));
    }

    // EFFECTS: returns string representing of a number as a row on the board
    public String posYToString(int y) {
        return String.valueOf((char) (48 + getBoardHeight() - y));
    }

    // REQUIRES: currPiece is not null, movePos is a position on the board that currPiece can get to
    // EFFECTS: returns true if there is more than one piece of the same type and team in the row
    //          that can move to movePos, false otherwise
    public boolean canGetToSameRow(Piece currPiece, Vector movePos) {
        int row = currPiece.getPosY();
        int count = 0;
        for (int j = 0; j < boardPieces[row].length; j++) {
            if (currPiece.equals(boardPieces[row][j]) && boardPieces[row][j].validMove(movePos)) {
                count++;
            }
        }
        return count > 1;
    }

    // REQUIRES: currPiece is not null
    // EFFECTS: returns true if there is more than one piece of the same type and team in the column
    //          that can move to movePos, false otherwise
    public boolean canGetToSameColumn(Piece currPiece, Vector movePos) {
        int column = currPiece.getPosX();
        int count = 0;
        for (Piece[] boardPiece : boardPieces) {
            if (currPiece.equals(boardPiece[column]) && boardPiece[column].validMove(movePos)) {
                count++;
            }
        }
        return count > 1;
    }

    // REQUIRES: currPiece is not null
    // EFFECTS: returns true if there is more than one piece of the same type and team that can move to movePos,
    //          false otherwise
    public boolean canGetToAnywhere(Piece currPiece, Vector movePos) {
        int count = 0;
        for (Piece[] boardPiece : boardPieces) {
            for (Piece piece : boardPiece) {
                if (currPiece.equals(piece) && piece.validMove(movePos)) {
                    count++;
                }
            }
        }
        return count > 1;
    }

    // EFFECTS: returns "x" if there is a piece to be taken at pieceAtMovePos, "" if not
    public String determineString(Piece pieceAtMovePos) {
        if (pieceAtMovePos == null) {
            return "";
        } else {
            return "x";
        }
    }

    // REQUIRES: piece is not null, movePos is not null
    // EFFECTS: returns the position vector of the piece of same team that can move to given movePos,
    //          returns null if no such piece exists or more than one piece exists
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

    // REQUIRES: piece is not null, movePos is not null, 0 <= x < getBoardWidth()
    // EFFECTS: returns the position vector of the piece of same team on given column that can move to given movePos,
    //          returns null if no such piece exists or more than one piece exists
    public Vector getPiecePosFromColumn(Piece piece, Vector movePos, int x) {
        Vector vec = null;
        int count = 0;

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

    // REQUIRES: piece is not null, movePos is not null, 0 <= y < getBoardHeight()
    // EFFECTS: returns the position vector of the piece of same team on given row that can move to given movePos,
    //          returns null if no such piece exists or more than one piece exists
    public Vector getPiecePosFromRow(Piece piece, Vector movePos, int y) {
        Vector vec = null;
        int count = 0;

        for (int j = 0; j < boardPieces[y].length; j++) {
            if (piece.equals(boardPieces[y][j]) && boardPieces[y][j].validMove(movePos)) {
                vec = new Vector(j, y);
                count++;
            }
        }
        if (count == 1) {
            return vec;
        }

        return null;
    }

    // EFFECTS: returns the state of the board as a string
    //          shows which pieces on which team are on which squares,
    //          as well as the pieces in each player's captured pieces
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder(" -------------------\n");
        for (int i = 0; i < boardPieces.length; i++) {
            boardString.append(boardPieces.length - i);
            for (int j = 0; j < boardPieces[i].length; j++) {
                boardString.append("| ");
                if (boardPieces[i][j] == null) {
                    boardString.append("   ");
                } else {
                    boardString.append(boardPieces[i][j].toString());
                }
                boardString.append(" ");
            }
            boardString.append("|\n");
        }
        boardString.append("    a     b     c   \n");
        boardString.append(capturedPiecesAsString(capturedPieces, Team.BLACK));
        boardString.append(capturedPiecesAsString(capturedPieces, Team.WHITE));

        return boardString.toString();
    }

    // EFFECTS: returns the string format of captured pieces of given team
    public String capturedPiecesAsString(List<Piece> pieces, Team team) {
        StringBuilder piecesString = new StringBuilder(capitalizeFirstOnly(team.name()) + "'s captured pieces:");
        for (Piece piece : pieces) {
            if (piece.getTeam().equals(team)) {
                piecesString.append(" ").append(piece);
            }
        }
        return piecesString + "\n";
    }

    // EFFECTS: returns the list of moves made in string formatted like this:
    //          1. Pxb3 Bxb3 2. @Pb2 Bc4 3. Rc2
    public String movesToString() {
        return movesToString("");
    }

    // EFFECTS: returns the list of moves made in string formatted like this with spacers before numbers:
    //          1. Pxb3 Bxb3 *spacer* 2. @Pb2 Bc4 *spacer* 3. Rc2
    public String movesToString(String spacer) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < movesMade.size(); i++) {
            if (i == 0) {
                string.append("1. ");
            } else if (i % 2 == 0) {
                string.append(spacer).append(i / 2 + 1).append(". ");
            }
            string.append(movesMade.get(i)).append(" ");
        }
        return string.toString();
    }

    // GETTERS

    // EFFECTS: returns whose turn it currently is
    public Team getTurn() {
        return turn;
    }

    // EFFECTS: returns whose turn it currently isn't
    public Team notTurn() {
        if (turn.equals(Team.WHITE)) {
            return Team.BLACK;
        } else {
            return Team.WHITE;
        }
    }

    // EFFECTS: returns the board with its pieces
    public Piece[][] getBoardPieces() {
        return boardPieces;
    }

    // EFFECTS: returns list of captured pieces
    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    // EFFECTS: returns list of moves made
    public List<String> getMovesMade() {
        return movesMade;
    }

    // EFFECTS: returns list of literal moves made
    public List<String> getLiteralMoves() {
        return literalMoves;
    }

    // EFFECTS: returns true if game is over
    public int getGameState() {
        return gameState;
    }

    // EFFECTS: returns the height of the board
    public int getBoardHeight() {
        return boardPieces.length;
    }

    // EFFECTS: returns the width of the board
    public int getBoardWidth() {
        return boardPieces[0].length;
    }

    // EFFECTS: returns the team of the king first in enemy lines
    public Team getKingInEnemyLines() {
        return kingInEnemyLines;
    }

    // EFFECTS: return the current textState
    public int getTextState() {
        return textState;
    }

    // MODIFIES: this
    // EFFECTS: sets current textState to given ts
    //          and logs an event indicating the textState has changed
    public void setTextState(int ts) {
        this.textState = ts;
        String style = textStateString(ts);
        EventLog.getInstance().logEvent(new Event("Printing style of moves changed to " + style + "."));
    }

    public String textStateString(int ts) {
        switch (ts) {
            case 0:
                return "Wrap Text";
            case 1:
                return "Line Break";
            case 2:
                return "Moves Only";
            default:
                return "Literal Moves";
        }
    }

    // Method name and structure taken from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    // EFFECTS: returns a board's moves as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("moves made", movesToJson());
        json.put("text style", getTextState());
        return json;
    }

    // EFFECTS: returns literal moves that have been made on this board as a JSON array
    private JSONArray movesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String move : literalMoves) {
            jsonArray.put(move);
        }
        return jsonArray;
    }
}
