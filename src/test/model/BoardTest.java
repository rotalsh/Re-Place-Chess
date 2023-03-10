package model;

import model.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board b;
    Pawn pw;
    Pawn pb;
    Bishop bw;
    Bishop bb;
    Rook rw;
    Rook rw2;
    Rook rb;
    Queen qw;
    Queen qb;
    King kw;
    King kb;
    Vector v00;
    Vector v01;
    Vector v02;
    Vector v03;
    Vector v10;
    Vector v11;
    Vector v12;
    Vector v13;
    Vector v20;
    Vector v21;
    Vector v22;
    Vector v23;

    @BeforeEach
    public void runBefore() {
        b = new Board();
        pw = new Pawn(1, 2, Team.WHITE);
        pb = new Pawn(1, 1, Team.BLACK);
        bw = new Bishop(2, 1, Team.WHITE);
        bb = new Bishop(2, 0, Team.BLACK);
        rw = new Rook(2, 3, Team.WHITE);
        rw2 = new Rook(0, 3, Team.WHITE);
        rb = new Rook(0, 0, Team.BLACK);
        qw = new Queen(0, 0, Team.WHITE);
        qb = new Queen(2, 3, Team.BLACK);
        kw = new King(1, 3, Team.WHITE);
        kb = new King(1, 0, Team.BLACK);
        v00 = new Vector(0, 0);
        v01 = new Vector(0, 1);
        v02 = new Vector(0, 2);
        v03 = new Vector(0, 3);
        v10 = new Vector(1, 0);
        v11 = new Vector(1, 1);
        v12 = new Vector(1, 2);
        v13 = new Vector(1, 3);
        v20 = new Vector(2, 0);
        v21 = new Vector(2, 1);
        v22 = new Vector(2, 2);
        v23 = new Vector(2, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3,b.getBoardWidth());
        assertEquals(4, b.getBoardHeight());
        assertEquals(0, b.getMovesMade().size());
        assertEquals(0, b.getCapturedPieces().size());
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(Team.BLACK, b.notTurn());
        assertEquals(0, b.getGameState());
        assertNull(b.getKingInEnemyLines());
        assertEquals(0, b.getTextState());
    }

    @Test
    public void testAddToCapturedPieces() {
        b.addToCapturedPieces(pw);
        assertEquals(new Pawn(Team.WHITE), b.getCapturedPieces().get(0));
        assertEquals(1, b.getCapturedPieces().size());
    }

    @Test
    public void testAddToCapturedPiecesTwice() {
        b.addToCapturedPieces(rw);
        b.addToCapturedPieces(pb);
        assertEquals(2, b.getCapturedPieces().size());
        assertTrue(b.getCapturedPieces().contains(rw));
        assertTrue(b.getCapturedPieces().contains(pb));
    }

    @Test
    public void testAddToCapturePiecesQueenAndRook() {
        b.addToCapturedPieces(qw);
        b.addToCapturedPieces(rb);
        assertEquals(2, b.getCapturedPieces().size());
        assertTrue(b.getCapturedPieces().contains(pw));
        assertTrue(b.getCapturedPieces().contains(rb));
    }

    @Test
    public void testChangeTurnTwiceAndTestNotTurn() {
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(Team.BLACK, b.notTurn());
        b.changeTurn();
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(Team.WHITE, b.notTurn());
        b.changeTurn();
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(Team.BLACK, b.notTurn());
    }

    @Test
    public void testEndGameNotEndThenEnd() {
        b.endGame();
        assertEquals(0, b.getGameState());

        b.addToCapturedPieces(kw);
        b.endGame();
        assertEquals(0, b.getGameState());

        b.addToCapturedPieces(kb);
        b.endGame();
        assertEquals(1, b.getGameState());
        assertFalse(b.getMovesMade().contains("#"));
    }

    @Test
    public void testEndGameFromKingInEnemyLines() {
        b.moveFoundPiece(v13, v22);
        b.moveFoundPiece(v10, v01);
        b.moveFoundPiece(v22, v21);
        b.changeTurn();
        b.moveFoundPiece(v21, v10);
        b.endGame();
        assertEquals(2, b.getGameState());
        b.changeTurn();
        b.endGame();
        assertEquals(3, b.getGameState());
        assertTrue(b.getMovesMade().contains("#"));
    }

    @Test
    public void testEndGameKingInEnemyLinesButCapture() {
        b.moveFoundPiece(v13, v22);
        b.moveFoundPiece(v10, v01);
        b.moveFoundPiece(v22, v21);
        b.changeTurn();
        b.moveFoundPiece(v21, v10);
        b.endGame();
        assertEquals(2, b.getGameState());
        b.moveFoundPiece(v00, v10);
        b.endGame();
        assertEquals(1, b.getGameState());
        assertFalse(b.getMovesMade().contains("#"));
    }

    @Test
    public void testCanPlaceSimple() {
        assertFalse(b.canPlace(rb, v11));
        assertTrue(b.canPlace(rb, v01));
    }

    @Test
    public void testCanPlaceEdges() {
        b.moveFoundPiece(v23, v22);
        b.moveFoundPiece(v00, v01);
        assertFalse(b.canPlace(rb, v23));
        assertFalse(b.canPlace(rw, v00));
        assertTrue(b.canPlace(pw, v23));
        assertTrue(b.canPlace(pb, v00));
    }

    @Test
    public void testPlacePiece() {
        assertEquals(Team.WHITE, b.getTurn());

        assertFalse(b.placePiece(rw, v01));
        assertEquals(Team.WHITE, b.getTurn());

        b.addToCapturedPieces(rw);
        assertTrue(b.placePiece(rw, v01));
        assertEquals(Team.BLACK, b.getTurn());

        b.addToCapturedPieces(rb);
        b.addToCapturedPieces(pw);
        assertFalse(b.placePiece(pb, v22));
        assertEquals(Team.BLACK, b.getTurn());
    }

    @Test
    public void testCheckPromotionNoPromotion() {
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
        b.checkPromotion();
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
    }

    @Test
    public void testCheckPromotionYesPromotion() {
        b.moveFoundPiece(v23, v22);
        b.moveFoundPiece(v00, v01);

        b.addToCapturedPieces(pw);
        b.addToCapturedPieces(pb);

        b.placePiece(pw, v00);
        assertEquals(pw, b.getBoardPieces()[0][0]);
        b.checkPromotion();
        assertEquals(qw, b.getBoardPieces()[0][0]);

        b.placePiece(pb, v23);
        assertEquals(pb, b.getBoardPieces()[3][2]);
        b.checkPromotion();
        assertEquals(qb, b.getBoardPieces()[3][2]);
    }

    @Test
    public void testCheckPromotionNoPromotionOppositePawn() {
        b.moveFoundPiece(v23, v22);
        b.moveFoundPiece(v00, v01);

        b.addToCapturedPieces(pw);
        b.addToCapturedPieces(pb);

        b.placePiece(pw, v23);
        assertEquals(pw, b.getBoardPieces()[3][2]);
        b.checkPromotion();
        assertEquals(pw, b.getBoardPieces()[3][2]);

        b.placePiece(pb, v00);
        assertEquals(pb, b.getBoardPieces()[0][0]);
        b.checkPromotion();
        assertEquals(pb, b.getBoardPieces()[0][0]);
    }

    @Test
    public void testPromotePawn() {
        b.moveFoundPiece(v23, v22);
        b.addToCapturedPieces(pw);
        b.placePiece(pw, v23);
        assertEquals(pw, b.getBoardPieces()[3][2]);
        b.promotePawn(pw, v23);
        assertEquals(qw, b.getBoardPieces()[3][2]);
    }

    @Test
    public void testCheckKingInEnemyLinesNoKing() {
        b.checkKingInEnemyLines();
        assertEquals(0, b.getGameState());
        assertNull(b.getKingInEnemyLines());
        b.moveFoundPiece(v13, v22);
        assertEquals(0, b.getGameState());
        assertNull(b.getKingInEnemyLines());
    }

    @Test
    public void testCheckKingInEnemyLinesWhiteKing() {
        b.moveFoundPiece(v13, v22);
        b.moveFoundPiece(v10, v01);
        b.moveFoundPiece(v22, v21);
        b.changeTurn();
        b.moveFoundPiece(v21, v10);
        assertEquals(2, b.getGameState());
        assertEquals(Team.WHITE, b.getKingInEnemyLines());
    }

    @Test
    public void testCheckKingInEnemyLinesBlackKing() {
        b.moveFoundPiece(v13, v22);
        b.moveFoundPiece(v10, v01);
        b.changeTurn();
        b.moveFoundPiece(v01, v02);
        b.changeTurn();
        b.moveFoundPiece(v02, v13);
        assertEquals(2, b.getGameState());
        assertEquals(Team.BLACK, b.getKingInEnemyLines());
    }

    @Test
    public void testCheckKingInEnemyLinesTwoKings() {
        b.moveFoundPiece(v13, v22);
        b.moveFoundPiece(v10, v01);
        b.moveFoundPiece(v22, v21);
        b.moveFoundPiece(v01, v02);
        b.moveFoundPiece(v21, v10);
        assertEquals(2, b.getGameState());
        b.moveFoundPiece(v02, v13);
        assertEquals(3, b.getGameState());
        assertEquals(Team.WHITE, b.getKingInEnemyLines());
    }

    @Test
    public void testMoveFoundPieceFalse() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(kw, b.getBoardPieces()[3][1]);
        assertEquals(Team.WHITE, b.getTurn());
        assertFalse(b.moveFoundPiece(v23, v13));
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(kw, b.getBoardPieces()[3][1]);
    }

    @Test
    public void testMoveFoundPieceFalseThenTrue() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(kw, b.getBoardPieces()[3][1]);
        assertEquals(Team.WHITE, b.getTurn());
        assertFalse(b.moveFoundPiece(v23, v13));
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(kw, b.getBoardPieces()[3][1]);

        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.moveFoundPiece(v23, v22));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(rw, b.getBoardPieces()[2][2]);
    }

    @Test
    public void testMoveFoundPieceNoCapture() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.moveFoundPiece(v23, v22));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(rw, b.getBoardPieces()[2][2]);
    }

    @Test
    public void testMoveFoundPieceNoCaptureTwice() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.getCapturedPieces().isEmpty());

        assertTrue(b.moveFoundPiece(v23, v22));
        assertEquals(Team.BLACK, b.getTurn());
        assertTrue(b.getCapturedPieces().isEmpty());
        assertEquals(rw, b.getBoardPieces()[2][2]);

        assertTrue(b.moveFoundPiece(v00, v01));
        assertEquals(Team.WHITE, b.getTurn());
        assertNull(b.getBoardPieces()[0][0]);
        assertEquals(rb, b.getBoardPieces()[1][0]);
        assertTrue(b.getCapturedPieces().isEmpty());
    }

    @Test
    public void testMoveFoundPieceCaptureThenNoCapture() {
        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
        assertEquals(Team.WHITE, b.getTurn());

        assertTrue(b.moveFoundPiece(v12, v11));
        assertEquals(Team.BLACK, b.getTurn());
        assertNull(b.getBoardPieces()[2][1]);
        assertEquals(pw, b.getBoardPieces()[1][1]);
        assertTrue(b.getCapturedPieces().contains(pw));
        assertEquals(1, b.getCapturedPieces().size());

        assertEquals(rb, b.getBoardPieces()[0][0]);
        assertNull(b.getBoardPieces()[1][0]);
        assertTrue(b.moveFoundPiece(v00, v01));
        assertEquals(Team.WHITE, b.getTurn());
        assertNull(b.getBoardPieces()[0][0]);
        assertEquals(rb, b.getBoardPieces()[1][0]);
        assertTrue(b.getCapturedPieces().contains(pw));
        assertEquals(1, b.getCapturedPieces().size());
    }

    @Test
    public void testMoveFoundPieceNoCaptureThenCapture() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(Team.WHITE, b.getTurn());
        assertNull(b.getBoardPieces()[2][2]);

        assertTrue(b.moveFoundPiece(v23, v22));
        assertNull(b.getBoardPieces()[3][2]);
        assertEquals(rw, b.getBoardPieces()[2][2]);
        assertTrue(b.getCapturedPieces().isEmpty());
        assertEquals(Team.BLACK, b.getTurn());

        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
        assertTrue(b.moveFoundPiece(v11, v12));
        assertEquals(pb, b.getBoardPieces()[2][1]);
        assertNull(b.getBoardPieces()[1][1]);
        assertTrue(b.getCapturedPieces().contains(pb));
        assertEquals(1, b.getCapturedPieces().size());
        assertEquals(Team.WHITE, b.getTurn());
    }

    @Test
    public void testMoveFoundPieceCaptureTwice() {
        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
        assertEquals(Team.WHITE, b.getTurn());

        assertTrue(b.moveFoundPiece(v12, v11));
        assertNull(b.getBoardPieces()[2][1]);
        assertEquals(pw, b.getBoardPieces()[1][1]);
        assertTrue(b.getCapturedPieces().contains(pw));
        assertEquals(1, b.getCapturedPieces().size());
        assertEquals(Team.BLACK, b.getTurn());

        assertEquals(bb, b.getBoardPieces()[0][2]);
        assertTrue(b.moveFoundPiece(v20, v11));
        assertTrue(b.getCapturedPieces().contains(pw));
        assertTrue(b.getCapturedPieces().contains(pb));
        assertEquals(2, b.getCapturedPieces().size());
        assertEquals(bb, b.getBoardPieces()[1][1]);
        assertEquals(Team.WHITE, b.getTurn());
    }

    @Test
    public void testAddMovePlace() {
        assertTrue(b.getMovesMade().isEmpty());

        b.addMove(pw, v22);
        assertEquals(1, b.getMovesMade().size());
        assertTrue(b.getMovesMade().contains("@Pc2"));
        assertTrue(b.getLiteralMoves().contains("@Pc2"));
    }

    @Test
    public void testAddMoveNoCaptureThenCapture() {
        assertTrue(b.getMovesMade().isEmpty());
        b.addMove(rw, null, v22);
        assertEquals(1, b.getMovesMade().size());
        assertTrue(b.getMovesMade().contains("Rc2"));
        assertTrue(b.getLiteralMoves().contains("Rc2"));

        b.addMove(pb, pw, v12);
        assertEquals(2, b.getMovesMade().size());
        assertTrue(b.getMovesMade().contains("Rc2"));
        assertTrue(b.getMovesMade().contains("Pxb2"));
        assertTrue(b.getLiteralMoves().contains("Rc2"));
        assertTrue(b.getLiteralMoves().contains("Pb2"));
    }

    @Test
    public void testAddMoveSameRow() {
        assertTrue(b.getMovesMade().isEmpty());
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.moveFoundPiece(v13, v22));
        assertEquals(Team.BLACK, b.getTurn());
        b.changeTurn();
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(1, b.getMovesMade().size());

        assertTrue(b.moveFoundPiece(v12, v11));
        assertEquals(Team.BLACK, b.getTurn());
        b.changeTurn();
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(2, b.getMovesMade().size());

        assertTrue(b.moveFoundPiece(v03, v12));
        assertEquals(Team.BLACK, b.getTurn());
        b.changeTurn();
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(3, b.getMovesMade().size());

        b.addToCapturedPieces(rw2);
        assertTrue(b.placePiece(rw2, v03));
        assertEquals(4, b.getMovesMade().size());

        b.addMove(rw, null, v13);
        assertEquals(5, b.getMovesMade().size());
        assertEquals("Rcb1", b.getMovesMade().get(4));
        assertEquals("Rcb1", b.getLiteralMoves().get(4));

        b.moveFoundPiece(v03, v13);
        assertEquals(6, b.getMovesMade().size());

        b.addMove(rw, null, v03);
        assertEquals(7, b.getMovesMade().size());
        assertEquals("Ra1", b.getMovesMade().get(6));
        assertEquals("Ra1", b.getLiteralMoves().get(6));
    }

    @Test
    public void testAddMoveSameColumn() {
        b.addToCapturedPieces(rw2);
        assertTrue(b.placePiece(rw2, v21));
        assertEquals(1, b.getMovesMade().size());

        b.addMove(rw, null, v22);
        assertEquals(2, b.getMovesMade().size());
        assertEquals("R1c2", b.getMovesMade().get(1));
        assertEquals("R1c2", b.getLiteralMoves().get(1));
    }

    @Test
    public void testAddMoveNotSameRowOrColumn() {
        b.addToCapturedPieces(bw);
        assertTrue(b.placePiece(bw, v21));
        assertEquals(1, b.getMovesMade().size());

        b.addMove(bw, null, v12);
        assertEquals(2, b.getMovesMade().size());
        assertEquals("Bcb2", b.getMovesMade().get(1));
        assertEquals("Bcb2", b.getLiteralMoves().get(1));
    }

    @Test
    public void testAddMovePromotion() {
        assertTrue(b.getMovesMade().isEmpty());
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.moveFoundPiece(v12, v11));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(1, b.getMovesMade().size());

        assertTrue(b.moveFoundPiece(v10, v01));
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(2, b.getMovesMade().size());

        assertTrue(b.moveFoundPiece(v11, v10));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(3, b.getMovesMade().size());
        assertEquals("Pb4=Q", b.getMovesMade().get(2));
        assertEquals("Pb4", b.getLiteralMoves().get(2));
    }

    @Test
    public void testDeterminePromotion() {
        assertEquals("=Q", b.determinePromotion(pw, v00));
        assertEquals("", b.determinePromotion(pw, v02));
        assertEquals("=Q", b.determinePromotion(pb, v03));
        assertEquals("", b.determinePromotion(pb, v00));
    }

    @Test
    public void testAddMoveCaptureKing() {
        assertTrue(b.getMovesMade().isEmpty());
        assertEquals(Team.WHITE, b.getTurn());
        assertTrue(b.moveFoundPiece(v12, v11));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(1, b.getMovesMade().size());

        b.changeTurn();
        assertEquals(Team.WHITE, b.getTurn());

        assertTrue(b.moveFoundPiece(v11, v10));
        assertEquals(Team.BLACK, b.getTurn());
        assertEquals(2, b.getMovesMade().size());
        assertEquals("Pxb4=Q#", b.getMovesMade().get(1));
        assertEquals("Pb4", b.getLiteralMoves().get(1));
    }

    @Test
    public void testDetermineIfCaptureKing() {
        assertEquals("#", b.determineIfCaptureKing(kb));
        assertEquals("", b.determineIfCaptureKing(kw));
        assertEquals("", b.determineIfCaptureKing(rw));
        assertEquals("", b.determineIfCaptureKing(rb));
    }

    @Test
    public void testVectorToString() {
        assertEquals("a4", b.vectorToString(v00));
        assertEquals("c2", b.vectorToString(v22));
    }

    @Test
    public void testPosXToString() {
        assertEquals("a", b.posXToString(0));
        assertEquals("b", b.posXToString(1));
    }

    @Test
    public void testPosYToString() {
        assertEquals("4", b.posYToString(0));
        assertEquals("3", b.posYToString(1));
    }

    @Test
    public void testCanGetToSameRow() {
        assertFalse(b.canGetToSameRow(rw, v13));
        b.moveFoundPiece(v13, v22);
        b.changeTurn();
        b.moveFoundPiece(v12, v11);
        b.changeTurn();
        b.moveFoundPiece(v03, v12);
        b.changeTurn();
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v03);
        assertTrue(b.canGetToSameRow(rw, v13));
        b.changeTurn();
        b.moveFoundPiece(v03, v13);
        assertFalse(b.canGetToSameRow(rw, v03));
    }

    @Test
    public void testCanGetToSameColumn() {
        assertFalse(b.canGetToSameColumn(rw, v22));
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v21);
        assertTrue(b.canGetToSameColumn(rw2, v22));
        b.changeTurn();
        b.moveFoundPiece(v21, v22);
        assertFalse(b.canGetToSameColumn(rw2, v21));
    }

    @Test
    public void testCanGetToAnyWhere() {
        assertFalse(b.canGetToAnywhere(bw, v12));
        b.addToCapturedPieces(bw);
        b.placePiece(bw, v21);
        assertTrue(b.canGetToAnywhere(bw, v12));
        b.changeTurn();
        b.moveFoundPiece(v21, v10);
        assertFalse(b.canGetToAnywhere(bw, v12));
    }

    @Test
    public void testDetermineString() {
        assertEquals("", b.determineString(null));
        assertEquals("x", b.determineString(pw));
    }

    @Test
    public void testGetPiecePosNoSetup() {
        assertNull(b.getPiecePos(qw, v11));
        assertEquals(rw.getPosVec(), b.getPiecePos(rw2, v22));
    }

    @Test
    public void testGetPiecePosSameRow() {
        assertFalse(b.canGetToSameRow(rw, v13));
        b.moveFoundPiece(v13, v22);
        b.changeTurn();
        b.moveFoundPiece(v12, v11);
        b.changeTurn();
        b.moveFoundPiece(v03, v12);
        b.changeTurn();
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v03);
        assertNull(b.getPiecePos(rw, v13));
    }

    @Test
    public void testGetPiecePosSameColumn() {
        assertFalse(b.canGetToSameColumn(rw, v22));
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v21);
        assertNull(b.getPiecePos(rw, v22));
    }

    @Test
    public void testGetPiecePosInvalidMove() {
        assertNull(b.getPiecePos(rw, v11));
        assertNull(b.getPiecePos(rw2, v23));
    }

    @Test
    public void testGetPieceFromColumnPosNoSetup() {
        assertNull(b.getPiecePosFromColumn(qw, v11, 2));
        assertEquals(rw.getPosVec(), b.getPiecePosFromColumn(rw2, v22, 2));
    }

    @Test
    public void testGetPieceFromColumnPosSameRow() {
        assertFalse(b.canGetToSameRow(rw, v13));
        b.moveFoundPiece(v13, v22);
        b.changeTurn();
        b.moveFoundPiece(v12, v11);
        b.changeTurn();
        b.moveFoundPiece(v03, v12);
        b.changeTurn();
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v03);
        assertEquals(rw.getPosVec(), b.getPiecePosFromColumn(rw, v13, 2));
        assertNull(b.getPiecePosFromColumn(rw, v13, 1));
        assertEquals(rw2.getPosVec(), b.getPiecePosFromColumn(rw, v13, 0));
    }

    @Test
    public void testGetPiecePosFromColumnSameColumn() {
        assertFalse(b.canGetToSameColumn(rw, v22));
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v21);
        assertNull(b.getPiecePosFromColumn(rw, v22, 2));
        assertNull(b.getPiecePosFromColumn(rw, v22, 0));

    }

    @Test
    public void testGetPiecePosFromColumnInvalidMove() {
        assertNull(b.getPiecePosFromColumn(rw, v11, 0));
        assertNull(b.getPiecePosFromColumn(rw2, v23, 2));
    }

    @Test
    public void testGetPieceFromRowPosNoSetup() {
        assertNull(b.getPiecePosFromRow(qw, v11, 2));
        assertEquals(rw.getPosVec(), b.getPiecePosFromRow(rw2, v22, 3));
    }

    @Test
    public void testGetPieceFromRowPosSameRow() {
        assertFalse(b.canGetToSameRow(rw, v13));
        b.moveFoundPiece(v13, v22);
        b.changeTurn();
        b.moveFoundPiece(v12, v11);
        b.changeTurn();
        b.moveFoundPiece(v03, v12);
        b.changeTurn();
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v03);
        assertNull(b.getPiecePosFromRow(rw, v13, 2));
        assertNull(b.getPiecePosFromRow(rw, v13, 3));
    }

    @Test
    public void testGetPiecePosFromRowSameColumn() {
        assertFalse(b.canGetToSameColumn(rw, v22));
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v21);
        assertEquals(rw.getPosVec(), b.getPiecePosFromRow(rw, v22, 3));
        assertNull(b.getPiecePosFromRow(rw, v22, 2));
        assertEquals(rw2.getPosVec(), b.getPiecePosFromRow(rw, v22, 1));

    }

    @Test
    public void testGetPiecePosFromRowInvalidMove() {
        assertNull(b.getPiecePosFromRow(rw, v11, 2));
        assertNull(b.getPiecePosFromRow(rw2, v23, 3));
    }

    @Test
    public void testGetPiecePosAndValidMove() {
        assertEquals(rb.getPosVec(), b.getPiecePos(rb, v01));
        assertNull(b.getPiecePos(rb, v02));
        assertNull(b.getPiecePos(rw, v21));
    }

    @Test
    public void testGetPiecePosAndValidMoveWithSetup() {
        b.moveFoundPiece(v23, v22);
        b.changeTurn();
        b.moveFoundPiece(v13, v23);
        assertNull(b.getPiecePos(kw, v01));
        assertNull(b.getPiecePos(kw, v02));
        assertNull(b.getPiecePos(kw, v03));
        assertNull(b.getPiecePos(kw, v11));
        assertNull(b.getPiecePos(kw, v21));
        assertNull(b.getPiecePos(kw, v23));
    }

    @Test
    public void testToString() {
        String boardString = " -------------------\n";
        boardString += "4| R_B | K_B | B_B |\n";
        boardString += "3|     | P_B |     |\n";
        boardString += "2|     | P_W |     |\n";
        boardString += "1| B_W | K_W | R_W |\n";
        boardString += "    a     b     c   \n";
        boardString += "Black's captured pieces:\n";
        boardString += "White's captured pieces:\n";
        assertEquals(boardString, b.toString());

        b.moveFoundPiece(v12, v11);
        String newBoardString = " -------------------\n";
        newBoardString += "4| R_B | K_B | B_B |\n";
        newBoardString += "3|     | P_W |     |\n";
        newBoardString += "2|     |     |     |\n";
        newBoardString += "1| B_W | K_W | R_W |\n";
        newBoardString += "    a     b     c   \n";
        newBoardString += "Black's captured pieces:\n";
        newBoardString += "White's captured pieces: P_W\n";
        assertEquals(newBoardString, b.toString());
    }

    @Test
    public void testCapturedPiecesAsString() {
        assertEquals("White's captured pieces:\n",
                b.capturedPiecesAsString(b.getCapturedPieces(), b.getTurn()));
        b.addToCapturedPieces(pb);
        b.addToCapturedPieces(rb);
        assertEquals("White's captured pieces:\n",
                b.capturedPiecesAsString(b.getCapturedPieces(), b.getTurn()));
        assertEquals("Black's captured pieces: P_B R_B\n",
                b.capturedPiecesAsString(b.getCapturedPieces(), b.notTurn()));
    }

    @Test
    public void testMovesToString() {
        assertEquals("", b.movesToString());
        b.moveFoundPiece(v12, v11);
        b.moveFoundPiece(v20, v11);
        b.placePiece(pw, v12);
        b.moveFoundPiece(v11, v20);
        b.moveFoundPiece(v23, v22);
        assertEquals("1. Pxb3 Bxb3 2. @Pb2 Bc4 3. Rc2 ", b.movesToString());
        assertEquals("1. Pxb3 Bxb3 \n2. @Pb2 Bc4 \n3. Rc2 ", b.movesToString("\n"));
    }

    @Test
    public void testAddMoveLog() {
        EventLog el = EventLog.getInstance();
        el.clear();

        b.addMove(new Pawn(Team.WHITE), new Pawn(Team.BLACK), new Vector(1, 1));
        b.changeTurn();
        b.addMove(new Bishop(Team.BLACK), new Pawn(Team.WHITE), new Vector(1, 1));
        b.changeTurn();
        b.addMove(new Pawn(Team.WHITE), new Vector(1, 2));

        List<Event> l = new ArrayList<>();

        for (Event next : el) {
            l.add(next);
        }

        assertEquals("White's move Pxb3 added.", l.get(1).getDescription());
        assertEquals("Black's move Bxb3 added.", l.get(2).getDescription());
        assertEquals("White's move @Pb2 added.", l.get(3).getDescription());
    }

    @Test
    public void testSetTextState() {
        b.setTextState(2);
        assertEquals(2, b.getTextState());
    }

    @Test
    public void testSetTextStateLog() {
        EventLog el = EventLog.getInstance();
        el.clear();

        b.setTextState(3);
        b.setTextState(2);
        b.setTextState(1);
        b.setTextState(0);

        List<Event> l = new ArrayList<>();

        for (Event next : el) {
            l.add(next);
        }

        assertEquals("Printing style of moves changed to Literal Moves.", l.get(1).getDescription());
        assertEquals("Printing style of moves changed to Moves Only.", l.get(2).getDescription());
        assertEquals("Printing style of moves changed to Line Break.", l.get(3).getDescription());
        assertEquals("Printing style of moves changed to Wrap Text.", l.get(4).getDescription());
    }

    @Test
    public void testTextStateString() {
        assertEquals("Literal Moves", b.textStateString(3));
        assertEquals("Moves Only", b.textStateString(2));
        assertEquals("Line Break", b.textStateString(1));
        assertEquals("Wrap Text", b.textStateString(0));
    }
}
