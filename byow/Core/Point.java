package byow.Core;

/**
 * Simple class to keep track of points without using series of 2-D arrays.
 * Used only during room nucleus creation.
 * @author Dayne Tran, Sean Lin
 */
public class Point {
    /** xCoord of Point. */
    private int x;
    /** yCoord of Point. */
    private int y;

    /**
     * constructor for point.
     * @param xCoord **x**
     * @param yCoord **y**
     */
    public Point(int xCoord, int yCoord) {
        x = xCoord;
        y = yCoord;
    }

    /**
     * accessor for x.
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * accessor for y.
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * modifier for x coord.
     * @param newX **
     */
    public void setX(int newX) {
        x = newX;
    }

    /**
     * modifier for y coord.
     * @param newY **
     */
    public void setY(int newY) {
        y = newY;
    }
}
