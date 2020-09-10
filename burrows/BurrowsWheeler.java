/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;

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
        int originalString = BinaryStdIn.readInt();
        String lastColumn = BinaryStdIn.readString();

        HashMap<Character, Queue<Integer>> sortedTable = new HashMap<Character, Queue<Integer>>();

        for (int i = 0; i < lastColumn.length(); i++) {
            if (!sortedTable.containsKey(lastColumn.charAt(i))) {
                sortedTable.put(lastColumn.charAt(i), new Queue<Integer>());
            }
            sortedTable.get(lastColumn.charAt(i)).enqueue(i);
        }

        char sortedWord[] = lastColumn.toCharArray();
        Arrays.sort(sortedWord);
        int next[] = new int[sortedWord.length];

        for (int i = 0; i < sortedWord.length; i++) {
            next[i] = sortedTable.get(sortedWord[i]).dequeue();
        }

        for (int i = 0; i < sortedWord.length; i++) {
            BinaryStdOut.write(sortedWord[originalString], 8);
            originalString = next[originalString];
        }

        BinaryStdIn.close();
        BinaryStdOut.close();
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
