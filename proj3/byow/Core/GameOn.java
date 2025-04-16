package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import byow.TileEngine.TERenderer;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

/**
 * GameOn class handles the gameplay.
 * All basic and ambitious game mechanics should be in here.
 * If any Game mechanics need to be added THIS IS THE SPOT!!
 * @author Dayne Tran, Sean Lin
 */
public class GameOn {
    /**
     * Point that keeps track of where the current user avatarG is.
     */
    private final Point avatarG;

    private boolean blind = true;

    /**
     * Point that keeps track of where the current avatarB is.
     */
    private final Point avatarB;

    /**
     * Counter that keeps track of the points for good player
     */
    private int pointsG;

    /**
     * Counter that keeps track of the points for bad player
     */
    private int pointsB;

    /**
     * 2D array that keeps track of the state of current world.
     */
    private final TETile[][] world;

    private final TERenderer ter = new TERenderer();

    /**
     * File that game saves state of world into when user :q.
     */
    private final File f;

    /**
     * seed that the current world is using.
     */
    private final long seed;

    /**
     * String that keeps track of the current in-game command the user inputs.
     * At the moment, only takes ":" and "q" because that is the only useful
     * command.
     */
    private String currentCommand;
    private int pointsAvail;
    ArrayList<Point> alreadyGone = new ArrayList<>();


    /**
     * Constructor for class.
     */
    public GameOn(TETile[][] ourWorld, File file, long s, int avatarGX, int avatarGY,
                  int avatarBX, int avatarBY, int[] scores) {
        world = ourWorld;
        f = file;
        seed = s;
        currentCommand = "";
        avatarG = new Point(avatarGX, avatarGY);
        avatarB = new Point(avatarBX, avatarBY);
        initiateAvatarStartPoint();
        pointsG = scores[0];
        pointsB = scores[1];
        for (int i = 0; i < ourWorld.length; i += 1) {
            for (int j = 0; j < ourWorld[0].length; j += 1) {
                if (world[i][j] == Tileset.FLOWER) {
                    pointsAvail += 1;
                }
            }
        }
    }

    /**
     * allows avatarG to respond to a char used as an input.
     * This is used ONLY IN interactWithStringInput.
     * This is used ONLY FOR auto-grading purposes.
     * @param c **char input**
     */
    public void respondToKeysExternally(char c) {
        if (c == 'w' || c == 'W') {
            if (upOpen(avatarG)) {
                moveUp(avatarG);
            }
        } else if (c == 'a' || c == 'A') {
            if (leftOpen(avatarG)) {
                moveLeft(avatarG);
            }
        } else if (c == 's' || c == 'S') {
            if (downOpen(avatarG)) {
                moveDown(avatarG);
            }
        } else if (c == 'd' || c == 'D') {
            if (rightOpen(avatarG)) {
                moveRight(avatarG);
            }
            // Avatar B movement
        } else if (c == 'i' || c == 'I') {
            if (upOpen(avatarB)) {
                moveUp(avatarB);
            }
        } else if (c == 'j' || c == 'J') {
            if (leftOpen(avatarB)) {
                moveLeft(avatarB);
            }
        } else if (c == 'k' || c == 'K') {
            if (downOpen(avatarB)) {
                moveDown(avatarB);
            }
        } else if (c == 'l' || c == 'L') {
            if (rightOpen(avatarB)) {
                moveRight(avatarB);
            }
        } else if (c == 'b' || c == 'B') {
            blind = !blind;
        } else if (c == ':') {
            HeadsUpDisplay.updateCommandExternally(c);
            currentCommand += c;
        } else if (c == 'q' || c == 'Q') {
            HeadsUpDisplay.updateCommandExternally(c);
            currentCommand += c;
        }
    }

    public boolean sameSpot() {
        return avatarB.getX() == avatarG.getX() & avatarB.getY() == avatarG.getY();
    }

    public TETile[][] see() {
        TETile[][] sliver = new TETile[world.length][world[0].length];
        for (int i = 0; i < world.length; i += 1) {
            for (int j = 0; j < world[0].length; j += 1) {
                if (Math.abs(i - avatarG.getX()) <= 3 & Math.abs(j - avatarG.getY()) <= 3) {
                    sliver[i][j] = world[i][j];
                } else if (Math.abs(i - avatarB.getX()) <= 3 & Math.abs(j - avatarB.getY()) <= 3) {
                    sliver[i][j] = world[i][j];
                } else {
                    sliver[i][j] = Tileset.NOTHING;
                }
            }
        }
        if (blind) {
            return sliver;
        } else {
            return world;
        }
    }

    public void finishGame() {
        ter.renderFrame(world);
        StdDraw.pause(3000);
        ter.initialize(world.length, world[0].length + 4);
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.text((int) ((double) world.length / 2), (int) (2 * (double) world[0].length / 3),
                "GAME OVER: Player 1 score = " + pointsG + " Player 2 Score = " + pointsB);
        StdDraw.show();
        StdDraw.pause(5000);
        System.exit(0);
    }

    /**
     * method that allows avatarG to respond to StdDraw.nextKeyTyped().
     * This is used ONLY in interactWithKeyboard.
     * responds to "wasd" in simple manner.
     * responds to ":" or "q" by updating currentCommand.
     */
    public void respondToKeys(char c) {
        System.out.println(c);
        // Avatar G movement
        if (c == 'w' || c == 'W') {
            if (upOpen(avatarG)) {
                moveUp(avatarG);
            }
        } else if (c == 'a' || c == 'A') {
            if (leftOpen(avatarG)) {
                moveLeft(avatarG);
            }
        } else if (c == 's' || c == 'S') {
            if (downOpen(avatarG)) {
                moveDown(avatarG);
            }
        } else if (c == 'd' || c == 'D') {
            if (rightOpen(avatarG)) {
                moveRight(avatarG);
            }
        // Avatar B movement
        } else if (c == 'i' || c == 'I') {
            if (upOpen(avatarB)) {
                moveUp(avatarB);
            }
        } else if (c == 'j' || c == 'J') {
            if (leftOpen(avatarB)) {
                moveLeft(avatarB);
            }
        } else if (c == 'k' || c == 'K') {
            if (downOpen(avatarB)) {
                moveDown(avatarB);
            }
        } else if (c == 'l' || c == 'L') {
            if (rightOpen(avatarB)) {
                moveRight(avatarB);
            }
        } else if (c == 'b' || c == 'B') {
            blind = !blind;
        } else if (c == ':') {
            currentCommand += c;
        } else if (c == 'q' || c == 'Q') {
            currentCommand += c;
        }

        if (sameSpot() | pointsAvail == 0) {
            finishGame();
        }
    }

    /**
     * saves the world into the file using RememberLastWorld.
     *
     * Data saved include:
     *  Line 1: seed previously used.
     *  Line 2: x pos of avatarG.
     *  Line 3: y pos of avatarG.
     */
    public void save() {
        RememberLastWorld rlw = new RememberLastWorld(f);
        alreadyGone.add(new Point(pointsG, pointsB));
        rlw.rewriteFile(seed, avatarG.getX(), avatarG.getY(),
                avatarB.getX(), avatarB.getY(), alreadyGone);
    }

    /**
     * determines whether it is time to quit the game.
     * time to quit is when command is ":q".
     * @return Can only return false.  If it is true, the System will quit
     * and the returned boolean becomes practically useless.
     */
    public boolean timeToQuit() {
        if (currentCommand.equals(":q") || currentCommand.equals(":Q")) {
            save();
            System.exit(0);
            return true;
        }
        return false;
    }

    /**
     * initiates the position of the avatarG.
     * if the avatarG's x and y position are both 0
     * (which is impossible in actual gameplay and indicates that
     * the avatarG is being placed in a new world)
     * then the avatarG gets placed in the LEFTMOST and BOTTOMMOST available
     * floor tile.
     *
     * otherwise, if avatarG position is currently nonzero, this means
     * avatarG is using a saved position and initiate avatarG in said position.
     */
    public void initiateAvatarStartPoint() {
        if (avatarG.getY() == 0 && avatarG.getX() == 0) {
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world[0].length; j++) {
                    if (world[i][j].equals(Tileset.FLOOR)) {
                        world[i][j] = Tileset.SAND;
                        avatarG.setX(i);
                        avatarG.setY(j);
                        i = world.length;
                        j = world.length;
                    }
                }
            }

            for (int i = world.length - 1; i >= 0; i -= 1) {
                for (int j = world[0].length - 1; j >= 0; j -= 1) {
                    if (world[i][j].equals(Tileset.FLOOR)) {
                        world[i][j] = Tileset.AVATAR;
                        avatarB.setX(i);
                        avatarB.setY(j);
                        i = -1;
                        j = -1;
                    }
                }
            }

        } else {
            world[avatarG.getX()][avatarG.getY()] = Tileset.SAND;
            world[avatarB.getX()][avatarB.getY()] = Tileset.AVATAR;
        }
    }

    /**
     * moves avatarG up.
     */
    private void moveUp(Point p) {
        int x = p.getX();
        int y = p.getY();
        world[x][y] = Tileset.FLOOR;
        if (p.equals(avatarG)) {
            if (world[x][y + 1] == Tileset.FLOWER) {
                pointsG += 1;
                alreadyGone.add(new Point(x, y + 1));
                pointsAvail -= 1;
            }
            world[x][y + 1] = Tileset.SAND;
        } else {
            if (world[x][y + 1] == Tileset.FLOWER) {
                pointsB += 1;
                alreadyGone.add(new Point(x, y + 1));
                pointsAvail -= 1;
            }
            world[x][y + 1] = Tileset.AVATAR;
        }
        p.setY(y + 1);
    }

    /**
     * moves avatarG down.
     */
    private void moveDown(Point p) {
        int x = p.getX();
        int y = p.getY();
        world[x][y] = Tileset.FLOOR;
        if (p.equals(avatarG)) {
            if (world[x][y - 1] == Tileset.FLOWER) {
                pointsG += 1;
                alreadyGone.add(new Point(x, y - 1));
                pointsAvail -= 1;
            }
            world[x][y - 1] = Tileset.SAND;
        } else {
            if (world[x][y - 1] == Tileset.FLOWER) {
                pointsB += 1;
                alreadyGone.add(new Point(x, y - 1));
                pointsAvail -= 1;
            }
            world[x][y - 1] = Tileset.AVATAR;
        }
        p.setY(y - 1);
    }

    /**
     * moves avatarG left.
     */
    private void moveLeft(Point p) {
        int x = p.getX();
        int y = p.getY();
        world[x][y] = Tileset.FLOOR;
        if (p.equals(avatarG)) {
            if (world[x - 1][y] == Tileset.FLOWER) {
                pointsG += 1;
                alreadyGone.add(new Point(x - 1, y));
                pointsAvail -= 1;
            }
            world[x - 1][y] = Tileset.SAND;
        } else {
            if (world[x - 1][y] == Tileset.FLOWER) {
                pointsB += 1;
                alreadyGone.add(new Point(x - 1, y));
                pointsAvail -= 1;
            }
            world[x - 1][y] = Tileset.AVATAR;
        }
        p.setX(x - 1);
    }

    /**
     * moves avatarG right.
     */
    private void moveRight(Point p) {
        int x = p.getX();
        int y = p.getY();
        world[x][y] = Tileset.FLOOR;
        if (p.equals(avatarG)) {
            if (world[x + 1][y] == Tileset.FLOWER) {
                pointsG += 1;
                alreadyGone.add(new Point(x + 1, y));
                pointsAvail -= 1;
            }
            world[x + 1][y] = Tileset.SAND;
        } else {
            if (world[x + 1][y] == Tileset.FLOWER) {
                pointsB += 1;
                alreadyGone.add(new Point(x + 1, y));
                pointsAvail -= 1;
            }
            world[x + 1][y] = Tileset.AVATAR;
        }
        p.setX(x + 1);
    }

    /**
     * checks if up is open.
     * @return true if up is open, false if up is blocked by wall.
     */
    private boolean upOpen(Point p) {
        int x = p.getX();
        int y = p.getY();
        return !world[x][y + 1].equals(Tileset.WALL);
    }

    /**
     * checks if down is open.
     * @return true if down is open, false if down is blocked by wall.
     */
    private boolean downOpen(Point p) {
        int x = p.getX();
        int y = p.getY();
        return !world[x][y - 1].equals(Tileset.WALL);
    }

    /**
     * checks if left is open.
     * @return true if left is open, false if left is blocked by wall.
     */
    private boolean leftOpen(Point p) {
        int x = p.getX();
        int y = p.getY();
        return !world[x - 1][y].equals(Tileset.WALL);
    }

    /**
     * checks if right is open.
     * @return true if right is open, false if right is blocked by wall.
     */
    private boolean rightOpen(Point p) {
        int x = p.getX();
        int y = p.getY();
        return !world[x + 1][y].equals(Tileset.WALL);
    }

    public int getPointsG() {
        return pointsG;
    }

    public int getPointsB() {
        return pointsB;
    }
}
