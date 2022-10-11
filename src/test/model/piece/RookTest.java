package model.piece;

import model.Team;
import model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {
    Rook r1;
    Rook r2;

    @BeforeEach
    public void runBefore() {
        r1 = new Rook(Team.WHITE);
        r2 = new Rook(0, 3, Team.BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(Team.WHITE, r1.getTeam());

        assertEquals(Team.BLACK, r2.getTeam());
        assertEquals(0, r2.getPosX());
        assertEquals(3, r2.getPosY());
        assertEquals(4, r2.getMoves().size());
        assertEquals(1, r2.getMagnitude());

        assertTrue(r2.getMoves().contains(new Vector(1, 0)));
        assertTrue(r2.getMoves().contains(new Vector(-1, 0)));
        assertTrue(r2.getMoves().contains(new Vector(0, -1)));
        assertTrue(r2.getMoves().contains(new Vector(0, 1)));
    }

    @Test
    public void testEquals() {
        assertFalse(r1.equals(null));
        assertFalse(r1.equals(r2));
        assertFalse(r1.equals(4));
        assertTrue(r1.equals(r1));
        assertTrue(r1.equals(new Rook(Team.WHITE)));
        assertFalse(r1.equals(new Bishop(Team.WHITE)));
    }

    @Test
    public void testGetLetter() {
        assertEquals("R", r1.getLetter());
        assertEquals("R", r2.getLetter());
    }
}
