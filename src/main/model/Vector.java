package model;

import static java.lang.Math.abs;

public class Vector {
    private int xcomp;
    private int ycomp;

    // TODO specifications
    public Vector(int x, int y) {
        xcomp = x;
        ycomp = y;
    }

    // TODO specifications
    public int getXcomp() {
        return xcomp;
    }

    // TODO specifications
    public int getYcomp() {
        return ycomp;
    }

    // TODO specifications
    public Vector addVector(Vector vec) {
        int newX = vec.getXcomp() + this.xcomp;
        int newY = vec.getYcomp() + this.ycomp;
        return new Vector(newX, newY);
    }

    // TODO specifications
    public Vector subVector(Vector vec) {
        int newX = vec.getXcomp() - this.xcomp;
        int newY = vec.getYcomp() - this.ycomp;
        return new Vector(newX, newY);
    }

    // TODO specifications
    public boolean isStrictlySmaller(Vector vec) {
        return (abs(xcomp) < abs(vec.getXcomp())) && (abs(ycomp) < abs(vec.getYcomp()));
    }

    // TODO specifications
    public boolean isZero() {
        return (xcomp == 0) && (ycomp == 0);
    }

    // TODO specifications
    public boolean hasSwitchedDirections(Vector vec) {
        if (xcomp == 0 && vec.getXcomp() != 0) {
            return false;
        } else if (ycomp == 0 && vec.getYcomp() != 0) {
            return false;
        } else if ((xcomp > 0 && vec.getXcomp() < 0) || (xcomp < 0 && vec.getXcomp() > 0)) {
            return false;
        } else if ((ycomp > 0 && vec.getYcomp() < 0) || (ycomp < 0 && vec.getYcomp() > 0)) {
            return false;
        } else {
            return true;
        }
    }

    // TODO specifications
    public void flipDirection() {
        xcomp = -xcomp;
        ycomp = -ycomp;
    }
}
