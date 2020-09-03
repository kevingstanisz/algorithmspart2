/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashSet;

public class BoggleSolver {

    private final HashSet<String> boggleDictionary;
    private final HashSet<String> bogglePrefixDictionary;
    private HashSet<String> boggleWords;

    private boolean[][] dieVisited;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException();
        }

        boggleDictionary = new HashSet<String>();
        bogglePrefixDictionary = new HashSet<String>();

        Collections.addAll(boggleDictionary, dictionary);

        for (String word : dictionary) {
            for (int i = word.length(); i >= 0; i--) {
                if (!bogglePrefixDictionary.add(word.substring(0, i))) {
                    break;
                }
            }
        }
    }

    private void dieDFS(BoggleBoard board, int i, int j, String currentPath) {
        StdOut.println(board.getLetter(i, j));
        currentPath += board.getLetter(i, j);
        // StdOut.println(currentPath);

        if (board.getLetter(i, j) == 'Q') {
            currentPath += 'U';
        }

        if (!bogglePrefixDictionary.contains(currentPath)) {
            // StdOut.println("nothing matches " + currentPath);
            return;
        }

        if (boggleDictionary.contains(currentPath) && currentPath.length() > 2) {
            // StdOut.println("before null " + currentPath);
            boggleWords.add(currentPath);
        }

        dieVisited[i][j] = true;

        for (int iMove = -1; iMove <= 1; iMove++) {
            for (int jMove = -1; jMove <= 1; jMove++) {
                if (!(iMove == 0 && jMove == 0)) {
                    int nextRow = i + iMove;
                    int nextCol = j + jMove;

                    if ((nextRow >= 0) && (nextCol >= 0) && (nextRow
                            < board.rows()) && (nextCol < board
                            .cols())) {
                        // StdOut.println("true or nah " + dieVisited[nextRow][nextCol]);

                        if (!dieVisited[nextRow][nextCol]) {
                            // StdOut.println(
                            //         "i move " + iMove + " j move " + jMove + " and i " + i + " j " + j);
                            dieDFS(board, nextRow, nextCol, currentPath);
                        }
                    }
                }
            }
        }

        dieVisited[i][j] = false;
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        dieVisited = new boolean[board.rows()][board.cols()];
        boggleWords = new HashSet<String>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dieVisited = new boolean[board.rows()][board.cols()];
                dieDFS(board, i, j, "");
            }
        }

        return boggleWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (boggleDictionary.contains(word)) {
            int wordLength = word.length();

            if (wordLength <= 2) {
                return 0;
            }
            else if (wordLength <= 4) {
                return 1;
            }
            else if (wordLength == 5) {
                return 2;
            }
            else if (wordLength == 6) {
                return 3;
            }
            else if (wordLength == 7) {
                return 5;
            }
            else {
                return 11;
            }
        }
        else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
