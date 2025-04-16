package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;

/**
 * Class Hallmaker.
 * @author Dayne Tran, Sean Lin
 */
public class Hallmaker {
    /**
     * world.
     */
    private TETile[][] world;
    /**
     * list of rooms.
     */
    private ArrayList<Room> rooms;
    /**
     * number of horizontal halls to be made.
     */
    private int numLatHalls;
    /**
     * number of vertical halls to be made.
     */
    private int numLonHalls;
    /**
     * Helper class used to prevent room isolation.
     */
    private NoRoomLeftBehind nRLB;

    /**
     * constructor for Hallmaker.
     * @param ourWorld **
     * @param ourRooms **
     * @param seed **
     */
    public Hallmaker(TETile[][] ourWorld,
                      ArrayList<Room> ourRooms, long seed) {
        world = ourWorld;
        rooms = ourRooms;
        StdRandom.setSeed(seed);
        numLatHalls = rooms.size() * 4;
        numLonHalls = rooms.size() * 4;
        nRLB = new NoRoomLeftBehind(ourWorld, ourRooms, seed);
    }

    /**
     * public method that adds halls to given world and rooms.
     */
    public void addHalls() {
        addLatHalls();
        addLonHalls();
        makeTilesUniform();
        nRLB.joinTheFun();
    }

    /**
     * adds all latitudinal halls.
     */
    private void addLatHalls() {

        ArrayList<Point> startPoints = new ArrayList<>();
        startPoints = genLatHallStartingPoints();

        for (Point p : startPoints) {
            if (validLatHallStarterPoint(p)) {
                int x = p.getX() + 1;
                int y = p.getY();
                world[x][y] = Tileset.GRASS;
                while (!world[x + 1][y].equals(Tileset.FLOOR)) {
                    world[x + 1][y] = Tileset.GRASS;
                    world[x + 1][y + 1] = Tileset.SAND;
                    world[x + 1][y - 1] = Tileset.SAND;
                    x += 1;
                }
                world[x][y] = Tileset.GRASS;
                world[x][y + 1] = Tileset.WALL;
                world[x][y - 1] = Tileset.WALL;
            }

        }
    }

    /**
     * randomly generates locations of lateral halls.
     * @return List of points.
     */
    private ArrayList<Point> genLatHallStartingPoints() {
        ArrayList<Point> latHalls = new ArrayList<Point>();

        for (int i = 0; i < numLatHalls; i++) {
            int randRoomIndex = (int) Math.floor(StdRandom.uniform
                    (0.0, 1.0) * rooms.size());
            Room randRoom = rooms.get(randRoomIndex);
            int nBound = randRoom.getnWall();
            int sBound = randRoom.getsWall() + 1;
            Point starterPoint = new Point(randRoom.geteWall() - 1,
                    StdRandom.uniform(sBound, nBound));
            latHalls.add(starterPoint);
        }
        return latHalls;
    }

    /**
     * validates whether a hall can be made from a given.
     * starter point.
     * @param p **
     * @return true
     */
    private boolean validLatHallStarterPoint(Point p) {
        int yCoord = p.getY();
        int xCoord = p.getX() + 2;
        if (world[p.getX() + 1][p.getY() + 1].equals(Tileset.GRASS)
                || world[p.getX() + 1][p.getY() - 1].equals(Tileset.GRASS)) {
            return false;
        }
        while (xCoord < world.length - 1) {
            if (world[xCoord][yCoord].equals(Tileset.WALL)
                    && world[xCoord + 1][yCoord].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[xCoord][yCoord + 1].equals(Tileset.WALL)
                    || world[xCoord][yCoord - 1].equals(Tileset.WALL)) {
                return false;
            } else if (world[xCoord][yCoord + 1].equals(Tileset.SAND)
                    || world[xCoord][yCoord - 1].equals(Tileset.SAND)) {
                return false;
            } else if (world[xCoord][yCoord + 1].equals(Tileset.GRASS)
                    || world[xCoord][yCoord - 1].equals(Tileset.GRASS)) {
                return false;
            }
            xCoord++;
        }
        return false;
    }

    /**
     * adds all latitudinal halls.
     */
    private void addLonHalls() {

        ArrayList<Point> startPoints = new ArrayList<>();
        startPoints = genLonHallStartingPoints();

        for (Point p : startPoints) {
            if (validLonHallStarterPoint(p)) {
                int x = p.getX();
                int y = p.getY() + 1;
                world[x][y] = Tileset.MOUNTAIN;
                while (!world[x][y + 1].equals(Tileset.FLOOR)) {
                    if (world[x][y + 1].equals(Tileset.GRASS)) {
                        world[x ][y + 1] = Tileset.AVATAR;
                    } else {
                        world[x][y + 1] = Tileset.MOUNTAIN;
                    }
                    world[x + 1][y + 1] = Tileset.LOCKED_DOOR;
                    world[x - 1][y + 1] = Tileset.LOCKED_DOOR;
                    y += 1;
                }
                world[x][y] = Tileset.MOUNTAIN;
                world[x + 1][y] = Tileset.WALL;
                world[x - 1][y] = Tileset.WALL;
            }

        }
    }

    /**
     * randomly generates locations of lateral halls.
     * @return List of points.
     */
    private ArrayList<Point> genLonHallStartingPoints() {
        ArrayList<Point> lonHalls = new ArrayList<Point>();

        for (int i = 0; i < numLonHalls; i++) {
            int randRoomIndex = (int) Math.floor(StdRandom.uniform
                    (0.0, 1.0) * rooms.size());
            Room randRoom = rooms.get(randRoomIndex);
            int eBound = randRoom.geteWall();
            int wBound = randRoom.getwWall() + 1;
            Point starterPoint = new Point(StdRandom.uniform
                    (wBound, eBound), randRoom.getnWall() - 1);
            lonHalls.add(starterPoint);
        }
        return lonHalls;
    }

    /**
     * validates whether a hall can be made from a given.
     * starter point.
     * @param p **
     * @return true
     */
    private boolean validLonHallStarterPoint(Point p) {
        int yCoord = p.getY() + 2;
        int xCoord = p.getX();
        if (world[p.getX() + 1][p.getY() + 1].equals(Tileset.MOUNTAIN)
                || world[p.getX() - 1][p.getY() + 1].equals(Tileset.MOUNTAIN)) {
            return false;
        }
        while (yCoord < world[0].length) {
            if (yCoord == world[0].length - 1) {
                return false;
            }
            if (world[xCoord][yCoord].equals(Tileset.WALL)
                    && world[xCoord][yCoord + 1].equals(Tileset.FLOOR)) {
                return true;
            } else if (world[xCoord + 1][yCoord].equals(Tileset.WALL)
                    || world[xCoord - 1][yCoord].equals(Tileset.WALL)) {
                return false;
            } else if (world[xCoord + 1][yCoord].equals(Tileset.LOCKED_DOOR)
                    || world[xCoord - 1][yCoord].equals(Tileset.LOCKED_DOOR)) {
                return false;
            } else if (world[xCoord + 1][yCoord].equals(Tileset.MOUNTAIN)
                    || world[xCoord - 1][yCoord].equals(Tileset.MOUNTAIN)) {
                return false;
            }
            yCoord++;
        }
        return false;
    }

    /**
     * visually cleans up the world.
     */
    public void makeTilesUniform() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j].equals(Tileset.SAND)) {
                    world[i][j] = Tileset.WALL;
                } else if (world[i][j].equals(Tileset.GRASS)) {
                    world[i][j] = Tileset.FLOOR;
                } else if (world[i][j].equals(Tileset.LOCKED_DOOR)) {
                    world[i][j] = Tileset.WALL;
                } else if (world[i][j].equals(Tileset.MOUNTAIN)) {
                    world[i][j] = Tileset.FLOOR;
                } else if (world[i][j].equals(Tileset.AVATAR)) {
                    world[i][j] = Tileset.FLOOR;
                    world[i + 1][j] = Tileset.FLOOR;
                    world[i - 1][j] = Tileset.FLOOR;
                }
            }
        }
    }
}
