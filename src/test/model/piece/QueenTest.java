package model.piece;

import model.Team;
import model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
    Queen q1;
    Queen q2;

    @BeforeEach
    public void runBefore() {
        q1 = new Queen(Team.WHITE);
        q2 = new Queen(2, 3, Team.BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(Team.WHITE, q1.getTeam());

        assertEquals(Team.BLACK, q2.getTeam());
        assertEquals(2, q2.getPosX());
        assertEquals(3, q2.getPosY());
        assertEquals(6, q2.getMoves().size());
        assertEquals(1, q2.getMagnitude());

        assertTrue(q2.getMoves().contains(new Vector(1, -1)));
        assertTrue(q2.getMoves().contains(new Vector(-1, -1)));
        assertTrue(q2.getMoves().contains(new Vector(1, 0)));
        assertTrue(q2.getMoves().contains(new Vector(-1, 0)));
        assertTrue(q2.getMoves().contains(new Vector(0, -1)));
        assertTrue(q2.getMoves().contains(new Vector(0, 1)));
    }

    @Test
    public void testEquals() {
        assertFalse(q1.equals(null));
        assertFalse(q1.equals(q2));
        assertFalse(q1.equals(4));
        assertTrue(q1.equals(q1));
        assertTrue(q1.equals(new Queen(Team.WHITE)));
        assertFalse(q1.equals(new Bishop(Team.WHITE)));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(Team.WHITE), q1.hashCode());
        assertEquals(Objects.hash(Team.BLACK), q2.hashCode());
    }

    @Test
    public void testGetLetter() {
        assertEquals("Q", q1.getLetter());
        assertEquals("Q", q2.getLetter());
    }
}
