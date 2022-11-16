package model.piece;

import model.Team;
import model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {
    King k1;
    King k2;

    @BeforeEach
    public void runBefore() {
        k1 = new King(Team.WHITE);
        k2 = new King(1, 0, Team.BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(Team.WHITE, k1.getTeam());

        assertEquals(Team.BLACK, k2.getTeam());
        assertEquals(1, k2.getPosX());
        assertEquals(0, k2.getPosY());
        assertEquals(8, k2.getMoves().size());
        assertEquals(1, k2.getMagnitude());

        assertTrue(k2.getMoves().contains(new Vector(1, 1)));
        assertTrue(k2.getMoves().contains(new Vector(-1, 1)));
        assertTrue(k2.getMoves().contains(new Vector(1, -1)));
        assertTrue(k2.getMoves().contains(new Vector(-1, -1)));
        assertTrue(k2.getMoves().contains(new Vector(1, 0)));
        assertTrue(k2.getMoves().contains(new Vector(-1, 0)));
        assertTrue(k2.getMoves().contains(new Vector(0, -1)));
        assertTrue(k2.getMoves().contains(new Vector(0, 1)));
    }

    @Test
    public void testEquals() {
        assertFalse(k1.equals(null));
        assertFalse(k1.equals(k2));
        assertFalse(k1.equals(4));
        assertTrue(k1.equals(k1));
        assertTrue(k1.equals(new King(Team.WHITE)));
        assertFalse(k1.equals(new Bishop(Team.WHITE)));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(Team.WHITE), k1.hashCode());
        assertEquals(Objects.hash(Team.BLACK), k2.hashCode());
    }

    @Test
    public void testGetLetter() {
        assertEquals("K", k1.getLetter());
        assertEquals("K", k2.getLetter());
    }
}
