package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

/**
 * class which EXECUTES the expansion of all worlds!
 * @author Dayne Tran, Sean Lin
 */
public class Expander {
    /**
     * instance variable which holds the 2-D array of the world.
     * Expander class will physically modify this object.
     */
    private TETile[][] world;

    /**
     * List of rooms which exist in the world.
     * Expander class will physically modify the
     * CONTENTS of this object by modifying rooms when expansion occurs.
     */
    private ArrayList<Room> rooms;

    private final ArrayList<Point> points;

    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * constructor for Expander class.
     * @param ourWorld ** loads the world into the class **
     * @param ourRooms ** loads the master list of rooms into the class**
     * @param seed **seed used for StdRandom**
     */
    public Expander(TETile[][] ourWorld, ArrayList<Room> ourRooms, long seed) {
        world = ourWorld;
        rooms = ourRooms;
        points = new ArrayList<>();
        StdRandom.setSeed(seed);
    }

    /**
     * accessor method for the world.
     * @return ** 2-D array of world **
     */
    public TETile[][] getWorld() {
        return world;
    }

    /**
     * A rather long and scary method that could probably be better engineered.
     *
     * expandAll expands all rooms using the following procedure:
     *      1. Each room is given 5 free expansions, meaning the
     *      room is allowed to expand 5 times with certainty.
     *          a. however each call to expand possibly could still
     *          do nothing in the case the room is blocked in the
     *          direction it chose to expand in.
     *      2. After free expansions, Iterate through list of rooms
     *      repetitively until all rooms have terminated growing.
     *          a. if room is already terminated, SKIP IT.
     *          b. if room is not terminated, check to see if it should
     *          because either it is blocked on all sides or it
     *          chooses to by the keepGoing probability.
     *      3. Execute an expansion
     */
    public void expandAll() {
        int numTerminated = 0;
        for (int i = 0; i < 10; i++) {
            for (Room room : rooms) {
                if (StdRandom.bernoulli(room.getLatP())) {
                    if (StdRandom.bernoulli(0.5)) {
                        if (!nBlocked(room)) {
                            growNorth(room);
                        }
                    } else if (!sBlocked(room)) {
                        growSouth(room);
                    }
                } else {
                    if (StdRandom.bernoulli(0.5)) {
                        if (!eBlocked(room)) {
                            growEast(room);
                        }
                    } else {
                        if (!wBlocked(room)) {
                            growWest(room);
                        }
                    }
                }
            }
        }
        while (numTerminated < rooms.size()) {
            for (Room room : rooms) {
                if (!room.isGrowthTerminated()) {
                    if (!room.blocked()) {
                        if (!StdRandom.bernoulli(room.getKeepGoing())) {
                            room.setTerminated(true);
                            numTerminated += 1;
                        } else if (StdRandom.bernoulli(room.getLatP())) {
                            if (StdRandom.bernoulli(0.5)) {
                                if (!nBlocked(room)) {
                                    growNorth(room);
                                }
                            } else if (!sBlocked(room)) {
                                growSouth(room);
                            }
                        }
                    } else {
                        if (StdRandom.bernoulli(0.5)) {
                            if (!eBlocked(room)) {
                                growEast(room);
                            }
                        } else if (!wBlocked(room)) {
                            growWest(room);
                        }
                    }
                } else {
                    room.setTerminated(true);
                    numTerminated += 1;
                }
            }
        }

        for (Room room : rooms) {
            if ((Math.abs(room.getwWall() - room.geteWall()) > 2)
                    & (Math.abs(room.getsWall() - room.getnWall()) > 2)) {
                int chance = StdRandom.uniform(1, 3);
                for (int i = 1; i <= chance; i += 1) {
                    int energyBallX = StdRandom.uniform(room.getwWall() + 1, room.geteWall() - 1);
                    int energyBallY = StdRandom.uniform(room.getsWall() + 1, room.getnWall() - 1);
                    world[energyBallX][energyBallY] = Tileset.FLOWER;
                    points.add(new Point(energyBallX, energyBallY));
                }
            }
        }
    }


    /**
     * Checks to see if * side is blocked.
     * Definition of blocked is if room was to extend * wall by one unit,
     * would the resulting wall collide with another wall?
     * @param room ** room to expand **
     * @return **whether the room is blocked in the * direction **
     */
    private boolean nBlocked(Room room) {
        if (room.getnWall() == world[0].length - 1) {
            room.setnBlocked(true);
            return true;
        }
        for (int i = room.getwWall(); i <= room.geteWall(); i++) {
            if (world[i][room.getnWall() + 1].equals(Tileset.WALL)) {
                room.setnBlocked(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if * side is blocked.
     * Definition of blocked is if room was to extend * wall by one unit,
     * would the resulting wall collide with another wall?
     * @param room ** room to expand **
     * @return **whether the room is blocked in the * direction **
     */
    private boolean wBlocked(Room room) {
        if (room.getwWall() == 0) {
            room.setwBlocked(true);
            return true;
        }
        for (int i = room.getsWall(); i <= room.getnWall(); i++) {
            if (world[room.getwWall() - 1][i].equals(Tileset.WALL)) {
                room.setwBlocked(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if * side is blocked.
     * Definition of blocked is if room was to extend * wall by one unit,
     * would the resulting wall collide with another wall?
     * @param room ** room to expand **
     * @return **whether the room is blocked in the * direction **
     */
    private boolean eBlocked(Room room) {
        if (room.geteWall() == world.length - 1) {
            room.seteBlocked(true);
            return true;
        }
        for (int i = room.getsWall(); i <= room.getnWall(); i++) {
            if (world[room.geteWall() + 1][i].equals(Tileset.WALL)) {
                room.seteBlocked(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if * side is blocked.
     * Definition of blocked is if room was to extend * wall by one unit,
     * would the resulting wall collide with another wall?
     * @param room ** room to expand **
     * @return **whether the room is blocked in the * direction **
     */
    private boolean sBlocked(Room room) {
        if (room.getsWall() == 0) {
            room.setsBlocked(true);
            return true;
        }
        for (int i = room.getwWall(); i <= room.geteWall(); i++) {
            if (world[i][room.getsWall() - 1].equals(Tileset.WALL)) {
                room.setsBlocked(true);
                return true;
            }
        }
        return false;
    }

    /**
     * executes the growth of a given room.
     * execution SHOULD be preceded by a call *Blocked
     * to determine if growth is legal.
     *
     * @param room **room to grow**
     */
    private void growNorth(Room room) {
        for (int i = room.getwWall(); i < room.geteWall() + 1; i += 1) {
            world[i][room.getnWall() + 1] = Tileset.WALL;
        }
        for (int i = room.getwWall() + 1; i < room.geteWall(); i += 1) {
            world[i][room.getnWall()] = Tileset.FLOOR;
        }
        room.incrementWall('n');
    }

    /**
     * executes the growth of a given room.
     * execution SHOULD be preceded by a call *Blocked
     * to determine if growth is legal.
     *
     * @param room **room to grow**
     */
    private void growSouth(Room room) {
        for (int i = room.getwWall(); i < room.geteWall() + 1; i += 1) {
            world[i][room.getsWall() - 1] = Tileset.WALL;
        }
        for (int i = room.getwWall() + 1; i < room.geteWall(); i += 1) {
            world[i][room.getsWall()] = Tileset.FLOOR;
        }
        room.incrementWall('s');
    }

    /**
     * executes the growth of a given room.
     * execution SHOULD be preceded by a call *Blocked
     * to determine if growth is legal.
     *
     * @param room **room to grow**
     */
    private void growWest(Room room) {
        for (int i = room.getsWall(); i < room.getnWall() + 1; i += 1) {
            world[room.getwWall() - 1][i] = Tileset.WALL;
        }
        for (int i = room.getsWall() + 1; i < room.getnWall(); i += 1) {
            world[room.getwWall()][i] = Tileset.FLOOR;

        }
        room.incrementWall('w');
    }

    /**
     * executes the growth of a given room.
     * execution SHOULD be preceded by a call *Blocked
     * to determine if growth is legal.
     *
     * @param room **room to grow**
     */
    private void growEast(Room room) {
        for (int i = room.getsWall(); i < room.getnWall() + 1; i += 1) {
            world[room.geteWall() + 1][i] = Tileset.WALL;
        }
        for (int i = room.getsWall() + 1; i < room.getnWall(); i += 1) {
            world[room.geteWall()][i] = Tileset.FLOOR;

        }
        room.incrementWall('e');
    }
}
