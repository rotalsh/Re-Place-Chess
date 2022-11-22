package model;

import model.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test Class modelled after EventLogTest in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
public class EventLogTest {
    private Event e1;
    private Event e2;
    private Event e3;

    @BeforeEach
    public void loadEvents() {
        e1 = new Event("A1");
        e2 = new Event("A2");
        e3 = new Event("A3");
        EventLog el = EventLog.getInstance();
        el.logEvent(e1);
        el.logEvent(e2);
        el.logEvent(e3);
    }

    @Test
    public void testLogEvent() {
        List<Event> l = new ArrayList<>();

        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }

        assertTrue(l.contains(e1));
        assertTrue(l.contains(e2));
        assertTrue(l.contains(e3));
    }

    @Test
    public void testLogEventMoveMadeAndPrintStyleChanged() {
        Board board = new Board();
        board.addMove(new Pawn(Team.WHITE), new Pawn(Team.BLACK), new Vector(1, 1));
        board.setTextState(2);
        List<Event> l = new ArrayList<>();

        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }

        assertEquals("White's move Pxb3 added." ,l.get(4).getDescription());
        assertEquals("Printing style of moves changed to Moves Only.", l.get(5).getDescription());
    }

    @Test
    public void testClear() {
        EventLog el = EventLog.getInstance();
        el.clear();
        Iterator<Event> itr = el.iterator();
        assertTrue(itr.hasNext());   // After log is cleared, the clear log event is added
        assertEquals("Event log cleared.", itr.next().getDescription());
        assertFalse(itr.hasNext());
    }
}
