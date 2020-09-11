/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> charactersASCII = new LinkedList<Character>();

        for (int i = 0; i <= 255; i++) {
            charactersASCII.addLast((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char character = BinaryStdIn.readChar();
            int index = charactersASCII.indexOf(character);
            charactersASCII.remove(index);
            charactersASCII.addFirst(character);
            BinaryStdOut.write(index, 8);
        }

        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> charactersASCII = new LinkedList<Character>();

        for (int i = 0; i <= 255; i++) {
            charactersASCII.addLast((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char character = charactersASCII.get(index);
            charactersASCII.remove(index);
            charactersASCII.addFirst(character);
            BinaryStdOut.write(character, 8);
        }

        BinaryStdOut.close();
        BinaryStdIn.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }

        if (args[0].equals("+")) {
            decode();
        }
    }
}
