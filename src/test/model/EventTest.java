package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// Test Class modelled after EventTest in AlarmSystem project
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git
public class EventTest {

    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("White's move Pxb3 added.");
        d = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("White's move Pxb3 added.", e.getDescription());
        assertEquals(d.getTime(), e.getDate().getTime(), 1.0);
    }

    @Test
    public void testEqualsAndHashCode() {
        assertFalse(e.equals(null));
        assertFalse(e.equals(3));

        assertEquals(13 * e.getDate().hashCode() + e.getDescription().hashCode(), e.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "White's move Pxb3 added.", e.toString());
    }
}
