/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcastNoun = nouns[0];
        int outcastDistance = 0;
        int newDistance = 0;

        for (String noun : nouns) {
            newDistance = 0;
            for (int i = 0; i < nouns.length; i++) {
                newDistance += wordNet.distance(noun, nouns[i]);
            }

            if (newDistance > outcastDistance) {
                outcastNoun = noun;
                outcastDistance = newDistance;
            }
        }

        return outcastNoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
