package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;

/**
 * Class entirely in charge of what is displayed in the heads up display.
 * @author Dayne Tran, Sean Lin
 *
 */
public class HeadsUpDisplay {
    /**
     * world we are using.
     */
    private final TETile[][] world;
    /**
     * String that keeps track of the current tile the mouse is hovering over.
     */
    private static String currentTile;
    /**
     * String that keeps track of the current user command.
     * At the moment, only possible command is ":q" or ":Q"
     */
    private static String commandLine;

    private static String score;

    /**
     * keeps track of width of world for font size scaling purposes.
     */
    private final int width;
    /**
     * keeps track of height of world for font size scaling purpose.
     */
    private final int height;

    /**
     * constructor for HUD.
     * initiates both current tile string and command string to ""
     * @param ourWorld **world being used**
     * @param w **width**
     * @param h **height**
     */
    public HeadsUpDisplay(TETile[][] ourWorld, int w, int h) {
        world = ourWorld;
        width = w;
        height = h;
        commandLine = "";
        currentTile = "";
        score = "SOMETHING";
    }

    /**
     * executes the displaying of ALL HUD parameters.
     * this is called continuously in a while loop in Engine class.
     * @param game **GameOn class collaborator**
     *          the GameOn class must collaborate with HUD class in order to
     *          accurately register keystrokes.
     *
     *          IMPORTANT!!!!
     *          Tricky part is HUD is in charge of using StdDraw.hasNextKeyTyped
     *          to gage whether a command like ":q" has been typed.
     *          GameOn is in charge of using the same to gage whether "wasd"
     *          has been typed.
     *
     *          Because both cannot simultaneously execute, sometimes "wasd"
     *          is pressed at instant where HUD is checking for ":" or "q" and
     *          sometimes vice versa.  So, the user will often experience the
     *          game not being able to register random key presses.
     *
     *          TO FIX THIS, HUD AND GAMEON MUST COLLABORATE AND COMMUNICATE!
     *          GAMEON CONSTANTLY CHECKS IF A KEY SHOULD BE SENT TO HUD INSTEAD
     *          AND HUD DOES THE SAME
     */
    public void displayAll(GameOn game, char c) {
        updateCurrentTile();
        updateCommand(game, c);
        updateScore(game.getPointsG(), game.getPointsB());
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.textRight(width - 2,  height - 2, commandLine);
        StdDraw.textLeft(1, height - 2, currentTile);
        StdDraw.textRight(width - 1, height - 1, score);
        StdDraw.show();
    }

    /**
     * updates currentTile String to reflect current tile mouse is over.
     */
    private void updateCurrentTile() {
        int x = (int) Math.floor(StdDraw.mouseX());
        int y = (int) Math.floor(StdDraw.mouseY());
        if (y < world[0].length) {
            TETile t = world[x][y];
            currentTile = t.description();
        } else {
            currentTile = "mouse is not on world";
        }
    }

    /**
     * updates the command line display.
     * @param g ** GameOn collaborator**
     *          ^^ refer to displayAll comment to understand why necessary.
     */
    private void updateCommand(GameOn g, char c) {
        if (c == ':') {
            commandLine += c;
        } else if (c == 'q') {
            commandLine += c;
        }
    }

    private void updateScore(int s1, int s2) {
        score = "Player A's points: " + s1 + "   Player B's points: " + s2;
    }

    /**
     * method used to update command externally using input char.
     * USED SOLELY by interactWithInputString.
     * @param c ** char to add to String **
     */
    public static void updateCommandExternally(char c) {
        commandLine += c;
    }
}
