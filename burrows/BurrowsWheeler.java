/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String stringTransform = BinaryStdIn.readString();
        CircularSuffixArray csaTransform = new CircularSuffixArray(stringTransform);

        for (int i = 0; i < csaTransform.length(); i++) {
            if (csaTransform.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        int selectChar = 0;

        for (int i = 0; i < csaTransform.length(); i++) {
            selectChar = csaTransform.index(i) - 1;

            if (selectChar < 0) {
                selectChar = csaTransform.length() - 1;
            }

            BinaryStdOut.write(stringTransform.charAt(selectChar));
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }

        if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
