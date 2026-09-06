package byow.Core;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Class which represents a single ROOM in the World.
 * @author Dayne Tran, Sean Lin
 */
public class Room {
    /** p is the nucleus of the room.
     * It is the origin from which the room expands. */
    private Point p;
    /** keeps track of the location of the * wall of the room. */
    private int wWall;
    /** keeps track of the location of the * wall of the room. */
    private int eWall;
    /** keeps track of the location of the * wall of the room. */
    private int nWall;
    /** keeps track of the location of the * wall of the room. */
    private int sWall;

    /**
     * Probability that the room chooses to continue
     * expanding when told to expand.
     * This is chosen from a Uniform distribution
     * between 0.7 and 0.8.
     * This probability is heavily skewed for the reason that
     * rooms should be generously allowed to expand to avoid rooms
     * that are only size 1 or 2 squares.
     *
     * if growthTerminated == true, this value ceases to be useful
     */
    private double keepGoing;

    /**
     * Probability that the room chooses to expand in the
     * North/South direction given that it chooses to expand.
     * This probability is chosen from a gaussian distribution
     * centered on 0.5 with a Standard Deviation of 0.1.
     *
     * if growthTerminated == true, this value ceases to be useful.
     */
    private double latP;

    /**
     * boolean which keeps track of whether or not the Room has
     * chosen to terminate expansion.
     * If this value is true, any subsequent call to grow this
     * room will be ignored.
     *
     * growthTerminated becomes true under 2 stop cases:
     *      1. Room has decided to stop growing based on a "bad"
     *      keepGoing probability.
     *      2. Room is blocked on all 4 sides and can no longer
     *      physically expand.
     *      (wBlocked & eBlocked & nBlocked & sBlocked) == true;
     */
    private boolean growthTerminated;

    /**
     * Booleans which keeps track status of * side as blocked or not.
     */
    private boolean wBlocked;
    /**
     * Booleans which keeps track status of * side as blocked or not.
     */
    private boolean eBlocked;
    /**
     * Booleans which keeps track status of * side as blocked or not.
     */
    private boolean nBlocked;
    /**
     * Booleans which keeps track status of * side as blocked or not.
     */
    private boolean sBlocked;

    /**
     * Constructor for the room class.
     * @param point ** nucleus of the Room **
     * @param wSeed ** seed given to room from user input in Engine class **
     */

    public Room(Point point, long wSeed) {
        p = point;
        wWall = p.getX() - 1;
        eWall = p.getX() + 1;
        nWall = p.getY() + 1;
        sWall = p.getY() - 1;
        StdRandom.setSeed(wSeed + wWall + eWall + nWall + sWall);
        final double R1 = 0.7;
        final double R2 = 0.8;
        final double E = 0.5;
        final double S = 0.1;
        keepGoing = StdRandom.uniform(R1, R2);
        latP = StdRandom.gaussian(E, S);
        if (latP < 0) {
            latP = 0;
        }
        if (latP > 1) {
            latP = 1;
        }
        growthTerminated = false;
        wBlocked = false;
        nBlocked = false;
        wBlocked = false;
        sBlocked = false;
    }

    /**
     * accessor for room nucleus.
     * @return ** nucleus point **
     */
    public Point getPoint() {
        return p;
    }

    /**
     * accessors for locations of walls.
     * @return **int location of wall**
     */
    public int getsWall() {
        return sWall;
    }
    /**
     * accessors for locations of walls.
     * @return **int location of wall**
     */
    public int geteWall() {
        return eWall;
    }
    /**
     * accessors for locations of walls.
     * @return **int location of wall**
     */
    public int getwWall() {
        return wWall;
    }
    /**
     * accessors for locations of walls.
     * @return **int location of wall**
     */
    public int getnWall() {
        return nWall;
    }

    /**
     * growth method which takes in a character as a parameter
     * and based on character chooses which direction to grow into.
     * @param c ** externally-determined parameter that denotes
     *          which direction to expand in **
     */
    public void incrementWall(Character c) {
        if (c.equals('n')) {
            nWall += 1;
        } else if (c.equals('w')) {
            wWall -= 1;
        } else if (c.equals('e')) {
            eWall += 1;
        } else {
            sWall -= 1;
        }
    }

    /**
     * modifier for growthTerminated.
     * used to set growthTerminated to true.
     * Once true, this value cannot return to false.
     * @param value ** new value to assign **
     */
    public void setTerminated(boolean value) {
        growthTerminated = value;
    }

    /**
     * accessor for growthTerminated.
     * @return **current value for growthTerminated**
     */
    public boolean isGrowthTerminated() {
        return growthTerminated;
    }

    /**
     * accessor for keepGoing.
     * @return **Probability value that room keeps Growing**
     */
    public double getKeepGoing() {
        return keepGoing;
    }

    /**
     * accessor for latP.
     * @return **Probability value latP**
     */
    public double getLatP() {
        return latP;
    }

    /**
     * boolean that simply aggregates the values of all four *Blocked booleans.
     * @return **true iff all for sides of room are blocked**
     */
    public boolean blocked() {
        return wBlocked & nBlocked & eBlocked & sBlocked;
    }

    /**
     * modifiers for *Blocked.
     * These methods are used to set *Blocked to true.
     * Once true, these values cannot return to false.
     * @param blocked ** new value **
     */
    public void seteBlocked(boolean blocked) {
        eBlocked = blocked;
    }
    /**
     * modifiers for *Blocked.
     * These methods are used to set *Blocked to true.
     * Once true, these values cannot return to false.
     * @param blocked ** new value **
     */
    public void setnBlocked(boolean blocked) {
        nBlocked = blocked;
    }
    /**
     * modifiers for *Blocked.
     * These methods are used to set *Blocked to true.
     * Once true, these values cannot return to false.
     * @param blocked ** new value **
     */
    public void setsBlocked(boolean blocked) {
        sBlocked = blocked;
    }
    /**
     * modifiers for *Blocked.
     * These methods are used to set *Blocked to true.
     * Once true, these values cannot return to false.
     * @param blocked ** new value **
     */
    public void setwBlocked(boolean blocked) {
        wBlocked = blocked;
    }
}
