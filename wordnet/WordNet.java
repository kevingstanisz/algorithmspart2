/* *****************************************************************************
 *  Name: Kevin Staniszewski
 *  Date: 8/13/2020
 *  Description: WordNet - Week 1
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WordNet {

    private final Map<Integer, String> idToNoun;
    private final Map<String, ArrayList<Integer>> nounToId;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In inSynsets = new In(synsets);
        idToNoun = new HashMap<Integer, String>();
        nounToId = new HashMap<String, ArrayList<Integer>>();

        String lineString = inSynsets.readLine();
        while (lineString != null) {
            String[] synsetsInfo = lineString.split(",");

            int idHash = Integer.parseInt(synsetsInfo[0]);
            String synsetNouns = synsetsInfo[1];
            idToNoun.put(idHash, synsetNouns);

            String[] spacedWords = synsetNouns.split(" ");

            for (String spacedWord : spacedWords) {
                ArrayList<Integer> existingIds = nounToId.get(spacedWord);
                if (existingIds == null) {
                    ArrayList<Integer> addFirstId = new ArrayList<Integer>();
                    addFirstId.add(idHash);
                    nounToId.put(spacedWord, addFirstId);
                }
                else {
                    existingIds.add(idHash);
                }
            }

            lineString = inSynsets.readLine();
        }


        In inHypernyms = new In(hypernyms);
        Digraph wordDigraph = new Digraph(idToNoun.size());

        lineString = inHypernyms.readLine();

        while (lineString != null) {
            String[] hypernymsInfo = lineString.split(",");

            int edgeId = Integer.parseInt(hypernymsInfo[0]);

            for (int i = 1; i < hypernymsInfo.length; i++) {
                int secondEdgeId = Integer.parseInt(hypernymsInfo[i]);
                wordDigraph.addEdge(edgeId, secondEdgeId);
            }

            lineString = inHypernyms.readLine();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return true;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return " ";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);

        int j = 0;

        for (String noun : wordNet.nouns()) {
            // StdOut.println(noun);
            j++;
        }

        StdOut.println(j);
    }
}
