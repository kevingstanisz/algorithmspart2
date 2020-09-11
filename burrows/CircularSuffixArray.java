/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private String suffixString;
    private Integer[] indexArr;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        suffixString = s;
        indexArr = new Integer[suffixString.length()];

        for (int i = 0; i < suffixString.length(); i++) {
            indexArr[i] = i;
        }

        Arrays.sort(indexArr, sortSuffix());
    }

    private Comparator<Integer> sortSuffix() {
        return new SortSuffix();
    }

    private class SortSuffix implements Comparator<Integer> {
        public int compare(Integer index1, Integer index2) {
            int firstIndex, secondIndex, result = 0;

            for (int i = 0; i < suffixString.length(); i++) {
                firstIndex = (index1 + i) % suffixString.length();
                secondIndex = (index2 + i) % suffixString.length();
                result = suffixString.charAt(firstIndex) - suffixString.charAt(secondIndex);
                // StdOut.println("same");
                if (result != 0) {
                    return result;
                }
            }

            return 0;
        }
    }

    // length of s
    public int length() {
        return suffixString.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if ((i < 0) || (i >= suffixString.length())) {
            throw new IllegalArgumentException();
        }

        return indexArr[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray abracadabraTest = new CircularSuffixArray("ABRACADABRA!");

        for (int i = 0; i < abracadabraTest.length(); i++) {
            StdOut.println(abracadabraTest.index(i));
        }
    }
}
