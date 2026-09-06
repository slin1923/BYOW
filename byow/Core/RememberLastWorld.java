package byow.Core;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class that is in charge of executing saves.
 * Reads to, writes from, and checks if load file exists.
 * @author Sean Lin
 */
public class RememberLastWorld {
    /**
     * File to work with.
     * as long as file exists, it should NOT be empty
     */
    private File relevantFile;

    /**
     * constructor for RememberLastWorld.
     * @param f ** file to read and write to **
     */
    public RememberLastWorld(File f) {
        relevantFile = f;
    }

    /**
     * Checks if file f already exists in directory.
     * Effectively this checks whether a world-to-load exists.
     * @return true if file exists, false otherwise.
     */
    public boolean fileExists() {
        return relevantFile.exists();
    }

    /**
     * creates a file.
     *
     * This method is dangerous and should not be used often as it creates
     * an empty load file which should never exist.
     * if a load file, exists, it must have content.
     * otherwise game will believe a load exists since file exists,
     * then will try to read from an empty file which
     * produces ERROR!
     *
     * Note: try catch format is a MUST with many of the File class methods.
     *
     * @return true if file is created, or false if
     *          1. file already exists.
     *          2. there was an error in file creation.
     */
    public boolean createFile() {
        try {
            return relevantFile.createNewFile();
        } catch (IOException e) {
            System.out.println("createFile is not working");
            return false;
        }
    }

    /**
     * rewrites the file by deleting it and recreating it in the same name.
     * (effectively this deletes all contents within old file)
     * then writes data into the new blank file.
     *
     * Current saved data is only seed, and previous avatar position.
     * This method SHOULD evolve as more game mechanics are added.
     *
     * @param seed ** seed to use **
     * @param avatarGX ** x pos of avatar **
     * @param avatarGY ** y pos of avatar **
     * @return true if successfully rewritten, false if otherwise.
     */
    public boolean rewriteFile(long seed, int avatarGX, int avatarGY,
                               int avatarBX, int avatarBY, ArrayList<Point> alreadyGone) {
        try {
            relevantFile.delete();
            relevantFile.createNewFile();
            FileWriter fw = new FileWriter(relevantFile);
            fw.write(Long.toString(seed) + "\n");
            fw.write(avatarGX + "\n");
            fw.write(avatarGY + "\n");
            fw.write(avatarBX + "\n");
            fw.write(avatarBY + "\n");
            for (Point point : alreadyGone) {
                fw.write(point.getX() + "\n");
                fw.write(point.getY() + "\n");
            }
            fw.flush();
            fw.close();
            return true;
        } catch (IOException e) {
            System.out.println("rewriteFile is not working");
            return false;
        }
    }

    /**
     * Simply reads data that already exists within file.
     *
     * @return ** ArrayList that contains each line in each index **
     */
    public ArrayList<String> readFromFile() {
        ArrayList<String> arr = new ArrayList<String>();
        try {
            Scanner scan = new Scanner(relevantFile);
            while (scan.hasNextLine()) {
                arr.add(scan.nextLine());
            }
        } catch (FileNotFoundException e) {
            return arr;
        }
        return arr;
    }
}
