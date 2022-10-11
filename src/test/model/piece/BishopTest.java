package model.piece;

import model.Team;
import model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    Bishop b1;
    Bishop b2;

    @BeforeEach
    public void runBefore() {
        b1 = new Bishop(Team.WHITE);
        b2 = new Bishop(1, 2, Team.BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(Team.WHITE, b1.getTeam());

        assertEquals(Team.BLACK, b2.getTeam());
        assertEquals(1, b2.getPosX());
        assertEquals(2, b2.getPosY());
        assertEquals(4, b2.getMoves().size());
        assertEquals(1, b2.getMagnitude());

        assertTrue(b2.getMoves().contains(new Vector(1, 1)));
        assertTrue(b2.getMoves().contains(new Vector(-1, 1)));
        assertTrue(b2.getMoves().contains(new Vector(1, -1)));
        assertTrue(b2.getMoves().contains(new Vector(-1, -1)));
    }

    @Test
    public void testEquals() {
        assertFalse(b1.equals(null));
        assertFalse(b1.equals(b2));
        assertFalse(b1.equals(4));
        assertTrue(b1.equals(b1));
        assertTrue(b1.equals(new Bishop(Team.WHITE)));
        assertFalse(b1.equals(new King(Team.WHITE)));
    }

    @Test
    public void testGetLetter() {
        assertEquals("B", b1.getLetter());
        assertEquals("B", b2.getLetter());
    }
}
