package model.piece;

import model.Team;
import model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {
    Pawn p1;
    Pawn p2;

    @BeforeEach
    public void runBefore() {
        p1 = new Pawn(Team.WHITE);
        p2 = new Pawn(2, 1, Team.BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(Team.WHITE, p1.getTeam());

        assertEquals(Team.BLACK, p2.getTeam());
        assertEquals(2, p2.getPosX());
        assertEquals(1, p2.getPosY());
        assertEquals(1, p2.getMoves().size());
        assertEquals(1, p2.getMagnitude());

        assertTrue(p2.getMoves().contains(new Vector(0, -1)));
    }

    @Test
    public void testEquals() {
        assertFalse(p1.equals(null));
        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(4));
        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(new Pawn(Team.WHITE)));
        assertFalse(p1.equals(new King(Team.WHITE)));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(Team.WHITE), p1.hashCode());
        assertEquals(Objects.hash(Team.BLACK), p2.hashCode());
    }

    @Test
    public void testGetLetter() {
        assertEquals("P", p1.getLetter());
        assertEquals("P", p2.getLetter());
    }
}
