package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public static void addHexagon(TETile[][] tiles, int sideLength, int x, int y) {
        if ((y < 0) | (y + sideLength > tiles.length)) {
            throw new IllegalArgumentException();
        }
        if ((x < 0) | (x + sideLength > tiles[0].length)) {
            throw new IllegalArgumentException();
        }
        if ((sideLength <= 0) | (2*sideLength > Math.min(tiles.length, tiles[0].length))) {
            throw new IllegalArgumentException();
        }

        int dim = sideLength * 2;
        int counter = 0;
        // outer is for i = y, i < y + dim; i++
        // inner is for j = x, j <
    }

    private static void topCreator(int sideLength, TETile[][] tiles, int x, int y) {
        for (int i = 0; i < sideLength; i+= 1) {
            for (int j = x - i; j < sideLength + i; j += 1) {
                tiles[y + i][j] = Tileset.FLOWER;
            }
        }

        y = y + 2 * sideLength - 1;
        for (int i = 0; i < sideLength; i+= 1) {
            for (int j = x - i; j < sideLength + i; j += 1) {
                tiles[y - i][j] = Tileset.FLOWER;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = 1;
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];

        ter.renderFrame(randomTiles);
    }
}

