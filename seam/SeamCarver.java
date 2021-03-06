/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture scPicture;
    private double[][] pictureArr;

    private enum Direction {
        XDIR, YDIR
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        scPicture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(scPicture);
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
        } else {
            int forwardSide;
            int behindSide;

            if (direction == Direction.XDIR) {
                forwardSide = scPicture.getRGB(x + 1, y);
                behindSide = scPicture.getRGB(x - 1, y);
            } else {
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
        } else {
            return Math.sqrt(xGradient + yGradient);
        }
    }

    private void fillPictureArr() {
        pictureArr = new double[scPicture.width()][scPicture.height()];

        for (int i = 0; i < scPicture.width(); i++) {
            for (int j = 0; j < scPicture.height(); j++) {
                pictureArr[i][j] = this.energy(i, j);
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        fillPictureArr();
        int[][] edgeTo = new int[scPicture.width()][scPicture.height()];
        double[][] distTo = new double[scPicture.width()][scPicture.height()];

        for (int j = 0; j < scPicture.height(); j++) {
            distTo[0][j] = pictureArr[0][j];
        }

        // topological order
        for (int i = 0; i < scPicture.width() - 1; i++) {
            for (int j = 0; j < scPicture.height(); j++) {
                // look to the right and the diagonals
                if (j != 0) {
                    if ((distTo[i + 1][j - 1] > distTo[i][j] + pictureArr[i + 1][j - 1])
                            || distTo[i + 1][j - 1] == 0) {
                        distTo[i + 1][j - 1] = distTo[i][j] + pictureArr[i + 1][j - 1];
                        edgeTo[i + 1][j - 1] = j;
                    }
                }

                if (j != (scPicture.height() - 1)) {
                    if ((distTo[i + 1][j + 1] > distTo[i][j] + pictureArr[i + 1][j + 1])
                            || distTo[i + 1][j + 1] == 0) {
                        distTo[i + 1][j + 1] = distTo[i][j] + pictureArr[i + 1][j + 1];
                        edgeTo[i + 1][j + 1] = j;
                    }
                }

                if ((distTo[i + 1][j] > distTo[i][j] + pictureArr[i + 1][j])
                        || distTo[i + 1][j] == 0) {
                    distTo[i + 1][j] = distTo[i][j] + pictureArr[i + 1][j];
                    edgeTo[i + 1][j] = j;
                }
            }
        }

        int[] minVertex = new int[scPicture.width()];
        double currDist = -1;

        for (int j = 0; j < scPicture.height(); j++) {
            if (distTo[scPicture.width() - 1][j] < currDist || currDist == -1) {
                currDist = distTo[scPicture.width() - 1][j];
                minVertex[scPicture.width() - 1] = j;
            }
        }

        for (int i = scPicture.width() - 2; i >= 0; i--) {
            minVertex[i] = edgeTo[i + 1][minVertex[i + 1]];
        }

        return minVertex;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        fillPictureArr();
        int[][] edgeTo = new int[scPicture.width()][scPicture.height()];
        double[][] distTo = new double[scPicture.width()][scPicture.height()];

        for (int i = 0; i < scPicture.width(); i++) {
            distTo[i][0] = pictureArr[i][0];
        }

        // topological order
        for (int j = 0; j < scPicture.height() - 1; j++) {
            for (int i = 0; i < scPicture.width(); i++) {
                // look to the right and the diagonals
                if (i != 0) {
                    if ((distTo[i - 1][j + 1] > distTo[i][j] + pictureArr[i - 1][j + 1])
                            || distTo[i - 1][j + 1] == 0) {
                        distTo[i - 1][j + 1] = distTo[i][j] + pictureArr[i - 1][j + 1];
                        edgeTo[i - 1][j + 1] = i;
                    }
                }

                if (i != (scPicture.width() - 1)) {
                    if ((distTo[i + 1][j + 1] > distTo[i][j] + pictureArr[i + 1][j + 1])
                            || distTo[i + 1][j + 1] == 0) {
                        distTo[i + 1][j + 1] = distTo[i][j] + pictureArr[i + 1][j + 1];
                        edgeTo[i + 1][j + 1] = i;
                    }
                }

                if ((distTo[i][j + 1] > distTo[i][j] + pictureArr[i][j + 1])
                        || distTo[i][j + 1] == 0) {
                    distTo[i][j + 1] = distTo[i][j] + pictureArr[i][j + 1];
                    edgeTo[i][j + 1] = i;
                }
            }
        }

        int[] minVertex = new int[scPicture.height()];
        double currDist = -1;

        for (int i = 0; i < scPicture.width(); i++) {
            if (distTo[i][scPicture.height() - 1] < currDist || currDist == -1) {
                currDist = distTo[i][scPicture.height() - 1];
                minVertex[scPicture.height() - 1] = i;
            }
        }

        for (int j = scPicture.height() - 2; j >= 0; j--) {
            minVertex[j] = edgeTo[minVertex[j + 1]][j + 1];
        }

        return minVertex;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != scPicture.width() || scPicture.height() <= 1) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < scPicture.width(); i++) {
            if (seam[i] < 0 || seam[i] > (scPicture.height() - 1)) {
                throw new IllegalArgumentException();
            } else if (i > 0) {
                if ((seam[i] != (seam[i - 1] - 1)) && (seam[i] != (seam[i - 1] + 1)) && (seam[i] != (seam[i - 1]))) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Picture newPicture = new Picture(scPicture.width(), scPicture.height() - 1);

        boolean seamFound = false;

        for (int i = 0; i < newPicture.width(); i++) {
            seamFound = false;
            for (int j = 0; j < newPicture.height(); j++) {
                if (seam[i] == j) {
                    seamFound = true;
                }

                if (seamFound) {
                    newPicture.set(i, j, scPicture.get(i, j + 1));
                } else {
                    newPicture.set(i, j, scPicture.get(i, j));
                }
            }
        }

        scPicture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != scPicture.height() || scPicture.width() <= 1) {
            throw new IllegalArgumentException();
        }

        for (int j = 0; j < scPicture.height(); j++) {
            if (seam[j] < 0 || seam[j] > (scPicture.width() - 1)) {
                throw new IllegalArgumentException();
            } else if (j > 0) {
                if ((seam[j] != (seam[j - 1] - 1)) && (seam[j] != (seam[j - 1] + 1)) && (seam[j] != (seam[j - 1]))) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Picture newPicture = new Picture(scPicture.width() - 1, scPicture.height());

        boolean seamFound = false;

        for (int j = 0; j < newPicture.height(); j++) {
            seamFound = false;
            for (int i = 0; i < newPicture.width(); i++) {
                if (seam[j] == i) {
                    seamFound = true;
                }

                if (seamFound) {
                    newPicture.set(i, j, scPicture.get(i + 1, j));
                } else {
                    newPicture.set(i, j, scPicture.get(i, j));
                }
            }
        }

        scPicture = newPicture;
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
