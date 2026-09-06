package byow.Core;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

/**
 * class entirely in charge of the opening menu and submenus.
 * If any submenus need to be added THIS IS THE SPOT!!
 * @author Sean Lin
 */
public class Menu {
    /**
     * Width of menu.
     */
    private int w;
    /**
     * Height of menu.
     */
    private int h;

    /**
     * size of menu font, adjust as needed.
     */
    private static final int FONTSIZE = 30;

    /**
     * constructor for menu.
     * @param width **width of menu**
     * @param height **heigh to menu**
     */
    public Menu(int width, int height) {
        w = width;
        h = height;
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.text(width / 2, height * 3 / 4, "61B game by DT and SL");
        StdDraw.text(width / 2, height * 11 / 16, "Start moving!");
        StdDraw.text(width / 2, height * 5 / 16, "New World (N)");
        StdDraw.text(width / 2, height * 4 / 16, "Load World (L)");
        StdDraw.text(width / 2, height * 3 / 16, "Quit (Q)");
        StdDraw.show();
    }

    /**
     * method in charge of the submenu prompts for a user-seed-input.
     * @return ** seed that the user input **
     */
    public long promptForSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(w / 2, 2 * h / 3, "Enter a Seed and Press 's' to finish");
        StdDraw.show();
        boolean finishedSeedInput = false;
        String seed = "";
        while (!finishedSeedInput) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 's' || c == 'S') {
                    finishedSeedInput = true;
                } else {
                    seed += c;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(w / 2, 2 * h / 3,
                            "Enter a Seed and Press 's' to finish");
                    StdDraw.text(w / 2, h / 3, seed);
                    StdDraw.show();
                }
            }
        }
        return Long.parseLong(seed);
    }
}
