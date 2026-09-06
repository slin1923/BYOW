package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdRandom;
import java.util.List;
import java.util.ArrayList;

/**
 * No Room Left Behind is a class which facilitates what to do
 * with an empty room.
 * @author Dayne Tran, Sean Lin
 */
public class NoRoomLeftBehind {
    /**
     * our World.
     */
    private TETile[][] world;

    /**
     * list of rooms.
     */
    private List<Room> rooms;

    /**
     * Constructor.
     * @param ourWorld **
     * @param ourRooms **
     * @param seed **
     */
    public NoRoomLeftBehind(TETile[][] ourWorld,
                            List<Room> ourRooms, long seed) {
        world = ourWorld;
        rooms = ourRooms;
        StdRandom.setSeed(seed);
    }

    /**
     * Extends north hall by one unit.
     * @param x **
     * @param y **
     */
    private void buildNorthHallOnce(int x, int y) {
        world[x][y + 1] = Tileset.FLOOR;
        world[x + 1][y + 1] = Tileset.WALL;
        world[x - 1][y + 1] = Tileset.WALL;
    }

    /**
     * Extends south hall by one unit.
     * @param x **
     * @param y **
     */
    private void buildSouthHallOnce(int x, int y) {
        world[x][y - 1] = Tileset.FLOOR;
        world[x + 1][y - 1] = Tileset.WALL;
        world[x - 1][y - 1] = Tileset.WALL;
    }

    /**
     * Extends east hall by one unit.
     * @param x **
     * @param y **
     */
    private void buildEastHallOnce(int x, int y) {
        world[x + 1][y] = Tileset.FLOOR;
        world[x + 1][y + 1] = Tileset.WALL;
        world[x + 1][y - 1] = Tileset.WALL;
    }

    /**
     * Extends west hall by one unit.
     * @param x **
     * @param y **
     */
    private void buildWestHallOnce(int x, int y) {
        world[x - 1][y] = Tileset.FLOOR;
        world[x - 1][y + 1] = Tileset.WALL;
        world[x - 1][y - 1] = Tileset.WALL;
    }

    /**
     * CRUCIAL PUBLIC METHOD to the class.
     */
    public void joinTheFun() {
        List<Room> badRooms = new ArrayList<>();
        for (Room r : rooms) {
            if (isRoomLonely(r)) {
                List<Point> northPoints = validNorthPoints(r);
                List<Point> southPoints = validSouthPoints(r);
                List<Point> eastPoints = validEastPoints(r);
                List<Point> westPoints = validWestPoints(r);
                if (northPoints.size() + southPoints.size()
                        + westPoints.size() + eastPoints.size() == 0) {
                    badRooms.add(r);
                }
                if (northPoints.size() > 0) {
                    Point n = northPoints.get(StdRandom.uniform
                            (0, northPoints.size()));
                    int x = n.getX();
                    int y = n.getY();
                    while (!world[x][y + 1].equals(Tileset.FLOOR)) {
                        buildNorthHallOnce(x, y);
                        y += 1;
                    }
                }
                if (southPoints.size() > 0) {
                    Point s = southPoints.get(StdRandom.uniform
                            (0, southPoints.size()));
                    int x = s.getX();
                    int y = s.getY();
                    while (!world[x][y - 1].equals(Tileset.FLOOR)) {
                        buildSouthHallOnce(x, y);
                        y -= 1;
                    }
                }
                if (eastPoints.size() > 0) {
                    Point e = eastPoints.get(StdRandom.uniform
                            (0, eastPoints.size()));
                    int x = e.getX();
                    int y = e.getY();
                    while (!world[x + 1][y].equals(Tileset.FLOOR)) {
                        buildEastHallOnce(x, y);
                        x += 1;
                    }
                }
                if (westPoints.size() > 0) {
                    Point w = westPoints.get(StdRandom.uniform
                            (0, westPoints.size()));
                    int x = w.getX();
                    int y = w.getY();
                    while (!world[x - 1][y].equals(Tileset.FLOOR)) {
                        buildWestHallOnce(x, y);
                        x -= 1;
                    }
                }
            }
        }
        removeRooms(badRooms);
    }

    /**
     * removes all rooms in a given list.
     * room removal means removing from list of rooms
     * and blacking out the room.
     * @param rooms1 **list of rooms**
     */
    private void removeRooms(List<Room> rooms1) {
        for (Room r : rooms1) {
            for (int i = r.getwWall(); i <= r.geteWall(); i++) {
                for (int j = r.getnWall(); j <= r.getsWall(); j++) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
            rooms.remove(r);
        }
    }

    /**
     * determines if room has any halls coming out of it.
     * @param r **room to be considered**
     * @return whether the room is lonely.
     */
    private boolean isRoomLonely(Room r) {
        int west = r.getwWall();
        int east = r.geteWall();
        int north = r.getnWall();
        int south = r.getsWall();
        for (int i = south + 1; i < north; i++) {
            if (world[west][i].equals(Tileset.FLOOR)
                    || world[east][i].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        for (int i = west + 1; i < east; i++) {
            if (world[i][north].equals(Tileset.FLOOR)
                    || world[i][south].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if coordinates are a valid starting point for
     * a hall.
     * @param x ** xcoord **
     * @param y ** ycoord **
     * @return true if valid, false if not.
     */
    private boolean isValidNorthStartingPoint(int x, int y) {
        for (int i = y + 1; i < world[0].length; i++) {
            if (world[x][i].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[x + 1][i].equals(Tileset.FLOOR)
                    || world[x - 1][i].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        return false;
    }

    /**
     * Determines if coordinates are a valid starting point for
     * a hall.
     * @param x ** xcoord **
     * @param y ** ycoord **
     * @return true if valid, false if not.
     */
    private boolean isValidSouthStartingPoint(int x, int y) {
        for (int i = y - 1; i >= 0; i--) {
            if (world[x][i].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[x + 1][i].equals(Tileset.FLOOR)
                    || world[x - 1][i].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        return false;
    }

    /**
     * Determines if coordinates are a valid starting point for
     * a hall.
     * @param x ** xcoord **
     * @param y ** ycoord **
     * @return true if valid, false if not.
     */
    private boolean isValidWestStartingPoint(int x, int y) {
        for (int i = x - 1; i > 0; i--) {
            if (world[i][y].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[i][y + 1].equals(Tileset.FLOOR)
                    || world[i][y - 1].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        return false;
    }

    /**
     * Determines if coordinates are a valid starting point for
     * a hall.
     * @param x ** xcoord **
     * @param y ** ycoord **
     * @return true if valid, false if not.
     */
    private boolean isValidEastStartingPoint(int x, int y) {
        for (int i = x + 1; i < world.length; i++) {
            if (world[i][y].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[i][y + 1].equals(Tileset.FLOOR)
                    || world[i][y - 1].equals(Tileset.FLOOR)) {
                return false;
            }
        }
        return false;
    }

    /**
     * List of Valid * points to start hallways from.
     * @param r ** room being considered **
     * @return List of points on * side of room where hallways
     * can be built from.
     */
    private List<Point> validNorthPoints(Room r) {
        List<Point> validPoints = new ArrayList<>();
        int y = r.getnWall() - 1;
        for (int i = r.getwWall() + 1; i < r.geteWall(); i++) {
            if (isValidNorthStartingPoint(i, y)) {
                validPoints.add(new Point(i, y));
            }
        }
        return validPoints;
    }

    /**
     * List of Valid * points to start hallways from.
     * @param r ** room being considered **
     * @return List of points on * side of room where hallways
     * can be built from.
     */
    private List<Point> validSouthPoints(Room r) {
        List<Point> validPoints = new ArrayList<>();
        int y = r.getsWall() + 1;
        for (int i = r.getwWall() + 1; i < r.geteWall(); i++) {
            if (isValidSouthStartingPoint(i, y)) {
                validPoints.add(new Point(i, y));
            }
        }
        return validPoints;
    }

    /**
     * List of Valid * points to start hallways from.
     * @param r ** room being considered **
     * @return List of points on * side of room where hallways
     * can be built from.
     */
    private List<Point> validEastPoints(Room r) {
        List<Point> validPoints = new ArrayList<>();
        int x = r.geteWall() - 1;
        for (int i = r.getsWall() + 1; i < r.getnWall(); i++) {
            if (isValidEastStartingPoint(x, i)) {
                validPoints.add(new Point(x, i));
            }
        }
        return validPoints;
    }

    /**
     * List of Valid * points to start hallways from.
     * @param r ** room being considered **
     * @return List of points on * side of room where hallways
     * can be built from.
     */
    private List<Point> validWestPoints(Room r) {
        List<Point> validPoints = new ArrayList<>();
        int x = r.getwWall() + 1;
        for (int i = r.getsWall() + 1; i < r.getnWall(); i++) {
            if (isValidWestStartingPoint(x, i)) {
                validPoints.add(new Point(x, i));
            }
        }
        return validPoints;
    }
}
