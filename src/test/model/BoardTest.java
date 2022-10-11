package model;

import model.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board b;
    Pawn pw;
    Pawn pb;
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
        assertFalse(b.isGameOver());
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
        assertFalse(b.isGameOver());

        b.addToCapturedPieces(kw);
        b.endGame();
        assertFalse(b.isGameOver());

        b.addToCapturedPieces(kb);
        b.endGame();
        assertTrue(b.isGameOver());
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
        assertEquals(null, b.getBoardPieces()[0][0]);
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
        assertEquals(null, b.getBoardPieces()[2][1]);
        assertEquals(pw, b.getBoardPieces()[1][1]);
        assertTrue(b.getCapturedPieces().contains(pw));
        assertEquals(1, b.getCapturedPieces().size());

        assertEquals(rb, b.getBoardPieces()[0][0]);
        assertEquals(null, b.getBoardPieces()[1][0]);
        assertTrue(b.moveFoundPiece(v00, v01));
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(null, b.getBoardPieces()[0][0]);
        assertEquals(rb, b.getBoardPieces()[1][0]);
        assertTrue(b.getCapturedPieces().contains(pw));
        assertEquals(1, b.getCapturedPieces().size());
    }

    @Test
    public void testMoveFoundPieceNoCaptureThenCapture() {
        assertEquals(rw, b.getBoardPieces()[3][2]);
        assertEquals(Team.WHITE, b.getTurn());
        assertEquals(null, b.getBoardPieces()[2][2]);

        assertTrue(b.moveFoundPiece(v23, v22));
        assertEquals(null, b.getBoardPieces()[3][2]);
        assertEquals(rw, b.getBoardPieces()[2][2]);
        assertTrue(b.getCapturedPieces().isEmpty());
        assertEquals(Team.BLACK, b.getTurn());

        assertEquals(pw, b.getBoardPieces()[2][1]);
        assertEquals(pb, b.getBoardPieces()[1][1]);
        assertTrue(b.moveFoundPiece(v11, v12));
        assertEquals(pb, b.getBoardPieces()[2][1]);
        assertEquals(null, b.getBoardPieces()[1][1]);
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
        assertEquals(null, b.getBoardPieces()[2][1]);
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
    }

    @Test
    public void testAddMoveNoCaptureThenCapture() {
        assertTrue(b.getMovesMade().isEmpty());
        b.addMove(rw, null, v22);
        assertEquals(1, b.getMovesMade().size());
        assertTrue(b.getMovesMade().contains("Rc2"));

        b.addMove(pb, pw, v12);
        assertEquals(2, b.getMovesMade().size());
        assertTrue(b.getMovesMade().contains("Rc2"));
        assertTrue(b.getMovesMade().contains("Pxb2"));
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
    }

    @Test
    public void testAddMoveSameColumn() {
        b.addToCapturedPieces(rw2);
        assertTrue(b.placePiece(rw2, v21));
        assertEquals(1, b.getMovesMade().size());

        b.addMove(rw, null, v22);
        assertEquals(2, b.getMovesMade().size());
        assertEquals("R1c2", b.getMovesMade().get(1));
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
    public void testSameRow() {
        assertFalse(b.sameRow(rw));
        b.moveFoundPiece(v13, v22);
        b.changeTurn();
        b.moveFoundPiece(v12, v11);
        b.changeTurn();
        b.moveFoundPiece(v03, v12);
        b.changeTurn();
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v03);
        assertTrue(b.sameRow(rw));
    }

    @Test
    public void testSameColumn() {
        assertFalse(b.sameColumn(rw));
        b.addToCapturedPieces(rw2);
        b.placePiece(rw2, v21);
        assertTrue(b.sameColumn(rw2));
    }

    @Test
    public void testDetermineString() {
        assertEquals("", b.determineString(null));
        assertEquals("x", b.determineString(pw));
    }
}
