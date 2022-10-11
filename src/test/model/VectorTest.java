package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {
    Vector v00;
    Vector v25;
    Vector v10;
    Vector v01;
    Vector vm5m1;
    Vector v42;
    Vector vm20;
    Vector v0m3;
    Vector v2m5;

    @BeforeEach
    public void runBefore() {
        v00 = new Vector(0, 0);
        v25 = new Vector(2, 5);
        v10 = new Vector(1, 0);
        v01 = new Vector(0, 1);
        vm5m1 = new Vector(-5, -1);
        v42 = new Vector (4, 2);
        vm20 = new Vector(-2, 0);
        v0m3 = new Vector(0, -3);
        v2m5 = new Vector(2, -5);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, v00.getXcomp());
        assertEquals(0, v00.getYcomp());
        assertEquals(2, v25.getXcomp());
        assertEquals(5, v25.getYcomp());
        assertEquals(-5, vm5m1.getXcomp());
        assertEquals(-1, vm5m1.getYcomp());
    }

    @Test
    public void testSubVector() {
        Vector compareVector1 = v00.subVector(v25);
        assertEquals(2, compareVector1.getXcomp());
        assertEquals(5, compareVector1.getYcomp());
        Vector compareVector2 = v25.subVector(vm5m1);
        assertEquals(-7, compareVector2.getXcomp());
        assertEquals(-6, compareVector2.getYcomp());
    }

    @Test
    public void testIsStrictlySmaller() {
        assertTrue(v00.isStrictlySmaller(v25));
        assertFalse(v25.isStrictlySmaller(v00));
        assertTrue(v00.isStrictlySmaller(vm5m1));
        assertFalse(v25.isStrictlySmaller(vm5m1));
        assertFalse(vm5m1.isStrictlySmaller(v25));
        assertFalse(v10.isStrictlySmaller(v01));
        assertFalse(v01.isStrictlySmaller(v10));
    }

    @Test
    public void testIsZero() {
        assertTrue(v00.isZero());
        assertFalse(v10.isZero());
        assertFalse(v01.isZero());
        assertFalse(v25.isZero());
    }

    @Test
    public void testHasSwitchedDirectionsFirstIf() {
        assertTrue(v00.hasSwitchedDirections(v01));
        assertTrue(v00.hasSwitchedDirections(v25));
        assertTrue(v25.hasSwitchedDirections(v00));
        assertFalse(v25.hasSwitchedDirections(v42));
    }

    @Test
    public void testHasSwitchedDirectionsSecondIf() {
        assertFalse(v10.hasSwitchedDirections(v10));
        assertTrue(v10.hasSwitchedDirections(v25));
        assertTrue(vm5m1.hasSwitchedDirections(vm20));
        assertTrue(v42.hasSwitchedDirections(vm5m1));
    }

    @Test
    public void testHasSwitchedDirectionsThirdIf() {
        assertTrue(v25.hasSwitchedDirections(vm5m1));
        assertTrue(vm5m1.hasSwitchedDirections(v25));
        assertFalse(v25.hasSwitchedDirections(v42));
        assertFalse(vm5m1.hasSwitchedDirections(vm5m1));
    }

    @Test
    public void testHasSwitchedDirectionsFourthIf() {
        assertFalse(v25.hasSwitchedDirections(v25));
        assertTrue(v25.hasSwitchedDirections(v2m5));
        assertTrue(v2m5.hasSwitchedDirections(v25));
        assertFalse(v2m5.hasSwitchedDirections(v2m5));
    }

    @Test
    public void testReverseDirection() {
        v00.reverseDirection();
        assertEquals(0, v00.getXcomp());
        assertEquals(0, v00.getYcomp());

        v25.reverseDirection();
        assertEquals(-2, v25.getXcomp());
        assertEquals(-5, v25.getYcomp());

        v10.reverseDirection();
        assertEquals(-1, v10.getXcomp());
        assertEquals(0, v10.getYcomp());
    }

    @Test
    public void testReverseDirectionTwice() {
        v25.reverseDirection();
        assertEquals(-2, v25.getXcomp());
        assertEquals(-5, v25.getYcomp());

        v25.reverseDirection();
        assertEquals(2, v25.getXcomp());
        assertEquals(5, v25.getYcomp());
    }

    @Test
    public void testEquals() {
        assertFalse(v00.equals(v25));
        assertFalse(v25.equals(null));
        assertFalse(v25.equals(1));
        assertTrue(v25.equals(new Vector (2, 5)));
        assertTrue(v00.equals(v00));
    }
}