package model;

import model.piece.Bishop;

import static java.lang.Math.abs;

// A two-dimensional vector with an x-component and a y-component
public class Vector {
    private int xcomp;
    private int ycomp;

    // EFFECTS: makes a vector with given x and y components
    public Vector(int x, int y) {
        xcomp = x;
        ycomp = y;
    }

    // REQUIRES: vec is not null
    // EFFECTS: return a new vector which equals given vector minus this vector
    public Vector subVector(Vector vec) {
        int newX = vec.getXcomp() - this.xcomp;
        int newY = vec.getYcomp() - this.ycomp;
        return new Vector(newX, newY);
    }

    // REQUIRES: vec is not null
    // EFFECTS: returns true if absolute values of both components of this vector is smaller than given vector
    public boolean isStrictlySmaller(Vector vec) {
        return (abs(xcomp) < abs(vec.getXcomp())) && (abs(ycomp) < abs(vec.getYcomp()));
    }

    // EFFECTS: returns true if this vector is the zero vector
    public boolean isZero() {
        return (xcomp == 0) && (ycomp == 0);
    }

    // REQUIRES: vec is not null
    // EFFECTS: returns true if this vector is pointing in a different quadrant or axis compared to given vector
    public boolean hasSwitchedDirections(Vector vec) {
        if ((xcomp == 0 && vec.getXcomp() != 0) || (xcomp != 0 && vec.getXcomp() == 0)) {
            return true;
        } else if ((ycomp == 0 && vec.getYcomp() != 0) || (ycomp != 0 && vec.getYcomp() == 0)) {
            return true;
        } else if ((xcomp > 0 && vec.getXcomp() < 0) || (xcomp < 0 && vec.getXcomp() > 0)) {
            return true;
        } else if ((ycomp > 0 && vec.getYcomp() < 0) || (ycomp < 0 && vec.getYcomp() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: reverses direction of vector
    public void reverseDirection() {
        xcomp = -xcomp;
        ycomp = -ycomp;
    }

    // GETTERS

    // EFFECTS: get the x component of the vector
    public int getXcomp() {
        return xcomp;
    }

    // EFFECTS: get the y component of the vector
    public int getYcomp() {
        return ycomp;
    }

    // EFFECTS: returns true if the obj is a vector with same x and y component as this, false otherwise
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Vector)) {
            return false;
        }
        Vector vector = (Vector) obj;
        return (vector.getXcomp() == this.getXcomp()) && (vector.getYcomp() == this.getYcomp());
    }
}
