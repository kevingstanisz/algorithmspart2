/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture scPicture;

    private enum Direction {
        XDIR, YDIR
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        scPicture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return scPicture;
    }

    // width of current picture
    public int width() {
        return scPicture.width();
    }

    // height of current picture
    public int height() {
        return scPicture.height();
    }

    private double calculateGradient(int x, int y, Direction direction) {
        if ((direction == Direction.XDIR && x == 0 || x == scPicture.width() - 1) || (
                direction == Direction.YDIR && y == 0 || y == scPicture.height() - 1)) {
            return -1;
        }
        else {
            int forwardSide;
            int behindSide;

            if (direction == Direction.XDIR) {
                forwardSide = scPicture.getRGB(x + 1, y);
                behindSide = scPicture.getRGB(x - 1, y);
            }
            else {
                forwardSide = scPicture.getRGB(x, y + 1);
                behindSide = scPicture.getRGB(x, y - 1);
            }

            double squaredTotal =
                    Math.pow((((forwardSide >> 16) & 0xFF) - ((behindSide >> 16) & 0xFF)), 2)
                            + Math
                            .pow((((forwardSide >> 8) & 0xFF) - ((behindSide >> 8) & 0xFF)), 2)
                            + Math.pow((((forwardSide) & 0xFF) - ((behindSide) & 0xFF)), 2);

            return squaredTotal;
        }
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= scPicture.width() || y < 0 || y >= scPicture.height()) {
            throw new IllegalArgumentException();
        }
        
        double xGradient = calculateGradient(x, y, Direction.XDIR);
        double yGradient = calculateGradient(x, y, Direction.YDIR);

        if (xGradient == -1 || yGradient == -1) {
            return 1000;
        }
        else {
            return Math.sqrt(xGradient + yGradient);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new SeamCarver(picture);
        // sc.picture().show();
        // StdOut.printf("image is %d columns by %d rows\n", sc.width(), sc.height());
    }

}
