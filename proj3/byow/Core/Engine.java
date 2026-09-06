package byow.Core;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdDraw;

/**
 * Engine class generates random worlds based on
 * given input seed and final world dimensions.
 * @author Dayne Tran, Sean Lin
 */
public class Engine {
    /**
     * Tile renderer class used create visual world.
     * Black Boxed: ignore what occurs within TERenderer.
     */
    private final TERenderer ter = new TERenderer();
    /**
     * Width of WORLD, feel free to change.
     */
    public static final int WIDTH = 80;
    /**
     * Height of WORLD, feel free to change.
     */
    public static final int HEIGHT = 30;
    /**
     * List that contains ALL ROOMS to be built within the world.
     */
    private final ArrayList<Room> refRooms = new ArrayList<>();
    /**
     * Seed used for StdRandom generation specified by user input String.
     */
    private long seed;
    /**
     * File is used to store saved data in order to successfully load worlds.
     *
     * The trick is all instance variables of a class become out of scope
     * once java terminates running.
     *
     * Therefore a local file MUST be used to store data.
     */
    private final File f = new File("byow/Core/savedWorld.txt");;

    /**
     * final value for how much space we want to give the HUD.
     */
    private static final int HUD = 4;
    private final ArrayList<Point> alreadyGone = new ArrayList<>();

    /**
     * Method used for exploring a fresh world.
     * This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT + HUD);
        Menu m = new Menu(WIDTH, HEIGHT + HUD);
        RememberLastWorld rlw = new RememberLastWorld(f);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        boolean setupNotComplete = true;
        boolean loadExists = false;
        List<String> loadedData = new ArrayList<String>();
        while (setupNotComplete) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'n' || c == 'N') {
                    seed = m.promptForSeed();
                    setupNotComplete = false;
                } else if (c == 'l' || c == 'L') {
                    if (rlw.fileExists()) {
                        loadedData = rlw.readFromFile();
                        loadExists = true;
                        setupNotComplete = false;
                    } else {
                        System.exit(0);
                    }
                } else if (c == 'q' || c == 'Q') {
                    System.exit(0);
                }
            }
        }

        // Begin creating the world or loading it in
        ter.initialize(WIDTH, HEIGHT + HUD);
        GameOn game;
        if (loadExists) {
            seed = Long.parseLong(loadedData.get(0));
            createWorld(finalWorldFrame);
            if (loadedData.size() > 5) {
                for (int i = 5; i < loadedData.size() - 2; i += 2) {
                    finalWorldFrame[Integer.parseInt(loadedData.get(i))]
                            [Integer.parseInt(loadedData.get(i + 1))] = Tileset.FLOOR;
                }
            }
            int score1 = Integer.parseInt(loadedData.get(loadedData.size() - 2));
            int score2 = Integer.parseInt(loadedData.get(loadedData.size() - 1));
            int[] scores = new int[2];
            scores[0] = score1;
            scores[1] = score2;
            game = new GameOn(finalWorldFrame,
                    f,
                    seed,
                    Integer.parseInt(loadedData.get(1)),
                    Integer.parseInt(loadedData.get(2)),
                    Integer.parseInt(loadedData.get(3)),
                    Integer.parseInt(loadedData.get(4)),
                    scores);
        } else {
            int[] scores = new int[2];
            createWorld(finalWorldFrame);
            rlw.rewriteFile(seed, 0, 0, 0, 0, alreadyGone);
            game = new GameOn(finalWorldFrame, f, seed,
                    0, 0, 0, 0, scores);
        }
        game.see();
        HeadsUpDisplay hud = new HeadsUpDisplay(finalWorldFrame, WIDTH,
                HEIGHT + HUD);
        boolean theGameIsAfoot = true;
        while (theGameIsAfoot) {
            TETile[][] sliverOrWhole = game.see();
            ter.renderFrame(sliverOrWhole);
            hud.displayAll(game, 'n');
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                System.out.println(c);
                hud.displayAll(game, c);
                game.respondToKeys(c);
            }
            StdDraw.show();
            StdDraw.pause(50);
            theGameIsAfoot = !game.timeToQuit();
        }
    }

    /**
     * Method used for autograding and testing your code.
     * The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas",
     * "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters
     * into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game
     * to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect
     * the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the
     * exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        char[] keystrokes;
        GameOn g;
        RememberLastWorld rlw = new RememberLastWorld(f);
        if (isValidInput(input).equals("A Whole New World")) {
            seed = extractSeed(input);
            keystrokes = readKeyStrokesNewWorld(input);
            createWorld(finalWorldFrame);
            int[] scores = new int[2];
            g = new GameOn(finalWorldFrame, f, seed, 0, 0, 0, 0, scores);
        } else if (isValidInput(input).equals("Loaded World")) {
            if (!rlw.fileExists()) {
                seed = 0;
                keystrokes = readKeyStrokesLoadWorld(input);
                createWorld(finalWorldFrame);
                int[] scores = new int[2];
                g = new GameOn(finalWorldFrame, f, seed, 0, 0, 0, 0, scores);
            } else {
                List<String> loadedData = rlw.readFromFile();
                seed = Long.parseLong(loadedData.get(0));
                keystrokes = readKeyStrokesLoadWorld(input);
                createWorld(finalWorldFrame);
                int score1 = Integer.parseInt(loadedData.get(loadedData.size() - 2));
                int score2 = Integer.parseInt(loadedData.get(loadedData.size() - 1));
                int[] scores = new int[2];
                scores[0] = score1;
                scores[1] = score2;
                g = new GameOn(finalWorldFrame, f, seed,
                        Integer.parseInt(loadedData.get(1)),
                        Integer.parseInt(loadedData.get(2)),
                        Integer.parseInt(loadedData.get(3)),
                        Integer.parseInt(loadedData.get(4)),
                        scores);
            }
        } else {
            throw new IllegalArgumentException("bad argument!");
        }
        for (char c : keystrokes) {
            g.respondToKeysExternally(c);
        }
        if (endsWithQuit(input)) {
            g.save();
        }
        finalWorldFrame = g.see();
        return finalWorldFrame;
    }

    /**
     * Reads the keystrokes "wasd" of an input string IF
     * input creates a NEW WORLD.
     * @param input **raw string input**
     * @return **array of keystrokes with order preserved**
     */
    private char[] readKeyStrokesNewWorld(String input) {
        int s1 = input.indexOf("s");
        int s2 = input.indexOf("S");
        int s = s1;
        if (s1 < 0) {
            s = s2;
        }
        String sub = "";
        if (endsWithQuit(input)) {
            sub = input.substring(s, input.indexOf(":"));
        } else {
            sub = input.substring(s, input.length());
        }
        return sub.toCharArray();
    }

    /**
     * Reads the keystrokes "wasd" of an input string IF
     * input creates a LOADED WORLD.
     * @param input **raw string input**
     * @return **array of keystrokes with order preserved**
     */
    private char[] readKeyStrokesLoadWorld(String input) {
        String sub = "";
        if (endsWithQuit(input)) {
            sub = input.substring(1, input.indexOf(":"));
        } else {
            sub = input.substring(1, input.length());
        }
        return sub.toCharArray();
    }

    /**
     * helper method that modularizes the creation of a new world.
     * KEY: the creation uses the current value of instance variable "seed"
     * if "seed" is null, this method will cause a runtime error.
     * @param t **2D array of tiles to create world into**
     */
    private void createWorld(TETile[][] t) {
        StdRandom.setSeed(seed);
        fillWithNothing(t);
        pointFinder(refRooms);
        addrefRooms(t, refRooms);
        Expander manifestD = new Expander(t, refRooms, seed);
        manifestD.expandAll();
        Hallmaker trailBlazer = new Hallmaker(t, refRooms, seed);
        trailBlazer.addHalls();
        t = manifestD.getWorld();
    }

    /**
     * Method checks if the input string is a valid input.
     * Valid is defined as:
     *      1. if String starts with an 'N' or 'n'
     *          and also contains an 's' or 'S'.
     *      2. if String starts with a 'L' or 'l'
     * @param input **String to be evaluated for validity**
     * @return True if Valid, false if not.
     */
    private String isValidInput(String input) {
        if (input.substring(0, 1).equals("n")
                || input.substring(0, 1).equals("N")) {
            if (input.contains("s") || input.contains("S")) {
                return "A Whole New World";
            }
        }
        if (input.substring(0, 1).equals("l")
                || input.substring(0, 1).equals("L")) {
            return "Loaded World";
        }
        return "not valid";
    }

    /**
     * tests whether input string ends with ":Q" or ":q".
     * This means the user wants to save and quit.
     * @param input **raw string input**
     * @return true if quit, false if else.
     */
    private boolean endsWithQuit(String input) {
        return input.contains(":Q") || input.contains(":q");
    }

    /**
     * ASSUMING THE INPUT IS VALID ALREADY.
     * extracts the substring of input between 'N'/'n' and 'S'/'s'.
     * returns said substring as an Integer.
     * @param input **String to extract seed from**
     * @return Extracted Integer Seed.
     */
    private Long extractSeed(String input) {
        int lowerS = input.indexOf("s");
        int upperS = input.indexOf("S");
        int indexOfS = 0;
        if (lowerS < 0) {
            indexOfS = upperS;
        } else if (upperS < 0) {
            indexOfS = lowerS;
        } else {
            indexOfS = Math.min(lowerS, upperS);
        }
        return Long.parseLong(input.substring(1, indexOfS));
    }

    /**
     * Maps the points in a list of points into a 2-D array.
     * Sets each coordinate in 2-D array that is contained
     * within a list of points to a FLOOR tile.
     * @param world **the 2-D array to map onto**
     * @param rfRooms **the List of points to be mapped**
     */
    private void addrefRooms(TETile[][] world, List<Room> rfRooms) {
        for (Room refPoint : rfRooms) {
            int x = refPoint.getPoint().getX();
            int y = refPoint.getPoint().getY();
            world[x][y] = Tileset.FLOOR;
            for (int i = refPoint.getwWall(); i
                    < refPoint.geteWall() + 1; i += 1) {
                for (int j = refPoint.getsWall(); j
                        < refPoint.getnWall() + 1; j += 1) {
                    if (!((x == i) && (y == j))) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    /**
     * And then he rested on the seventh day.
     * fills the 2-D array with nothing tiles.
     * @param world **the world to fill with nothing**
     */
    private void fillWithNothing(TETile[][] world) {
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Determines number of rooms based on a normal
     * distribution as a function of Area.
     * Should only be called ONCE PER WORLD!
     * @return **number of rooms**
     *
     */
    private int numRoomGenerator() {
        return (int) (2 * Math.floor(StdRandom.gaussian
                (WIDTH * HEIGHT / SCALE, WIDTH * HEIGHT / STDEV)));
    }

    /**
     * cannot have magic numbers.
     */
    private static final int SCALE = 100;
    /**
     * cannot have magic numbers.
     */
    private static final int STDEV = 600;

    /**
     * creates room nuclei in 4 quadrants of the world.
     * Each quadrant contains numRooms/4 rooms.
     * Anywhere between 0-3 rooms may remain, which are
     * randomly placed in the world without quadrant bounds
     * quadrant system ensures a very basic degree of space efficiency!
     *
     * Quadrant visual:  I    II
     *                  III   IV
     *
     * Uniformly selects x and y coordinates in each quadrant
     * and creates a Point with said coordinates.
     *
     * IMPORTANT: nucleus can never be generated on the BORDER OF THE WORLD!
     * @param arr **List of rooms to be initialized**
     */
    private void pointFinder(ArrayList<Room> arr) {
        /**
         * The number of rooms to add within the world.
         */
        int numRooms = numRoomGenerator();
        int numPerQuad = numRooms / 4;
        int remaining = numRooms % 4;
        int equator = HEIGHT / 2;
        int meridian = WIDTH / 2;
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < numPerQuad; j++) {
                int x = 0;
                int y = 0;
                if (i == 4) {
                    x = StdRandom.uniform(meridian, WIDTH - 2);
                    y = StdRandom.uniform(1, equator - 1);
                }
                if (i == 3) {
                    x = StdRandom.uniform(1, meridian - 1);
                    y = StdRandom.uniform(1, equator - 1);
                }
                if (i == 2) {
                    x = StdRandom.uniform(meridian, WIDTH - 2);
                    y = StdRandom.uniform(equator, HEIGHT - 2);
                }
                if (i == 1) {
                    x = StdRandom.uniform(1, meridian - 1);
                    y = StdRandom.uniform(equator, HEIGHT - 2);
                }
                Room toBeAdded = new Room(new Point(x, y), seed);
                if (!collisionImminent(toBeAdded, arr)) {
                    arr.add(toBeAdded);
                }
            }
        }
        for (int i = 0; i < remaining; i++) {
            int xR = StdRandom.uniform(1, WIDTH - 2);
            int yR = StdRandom.uniform(1, HEIGHT - 2);
            arr.add(new Room(new Point(xR, yR), seed));
        }
    }

    /**
     * Determines whether or not a generated nucleus
     * for room growth collides with a pre-existing point.
     * Collision is defined as a nucleus that is generated
     * either right on top of an existing nucleus OR
     * a room that is generated in one of the 8 adjacent
     * spaces to the said nucleus.
     * @param r **Nucleus of potential new room**
     * @param rooms ** list of already-existing rooms**
     * @return True if point causes Collision, False if not.
     */
    private boolean collisionImminent(Room r, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            Point point = room.getPoint();
            Point p = r.getPoint();
            if (Math.pow(p.getX() - point.getX(), 2)
                    + Math.pow(p.getY() - point.getY(), 2) < 9) {
                return true;
            }
        }
        return false;
    }

    /**
     * main method for testing interactWithStringInput.
     * @param args **String argument of type N1234..S...**
     *             args is not case sensitive for 'N' or 'S'
     */
    public static void main(String[] args) {
        Engine thomasTheDankEngine = new Engine();
//        thomasTheDankEngine.ter.initialize(WIDTH, HEIGHT);
//        TETile[][] world = thomasTheDankEngine.interactWithInputString(args[0]);
//        thomasTheDankEngine.ter.renderFrame(world);
        thomasTheDankEngine.interactWithKeyboard();
    }
}

