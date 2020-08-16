/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SAP {

    private Digraph sapDigraph;

    private enum FindShortest {
        PATH_LENGTH, COMMON_ANCESTOR
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        sapDigraph = new Digraph(G);
    }

    private int vertexBFS(int v, int w, FindShortest lookingFor) {
        // check that v and w are valid
        if (v >= sapDigraph.V() || w >= sapDigraph.V() || v < 0 || w < 0) {
            throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(sapDigraph, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(sapDigraph, w);

        // might want to add another parameter that determines length of ancestor
        return useBFS(vBFS, wBFS, lookingFor);
    }

    private int verticesBFS(Iterable<Integer> v, Iterable<Integer> w, FindShortest lookingFor) {
        // check that v and w are valid
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        boolean[] notEmptyArray = new boolean[2];

        for (Integer vValue : v) {
            notEmptyArray[0] = true;
            if (vValue == null || vValue < 0 || vValue >= sapDigraph.V()) {
                throw new IllegalArgumentException();
            }
        }

        for (Integer wValue : w) {
            notEmptyArray[1] = true;
            if (wValue == null || wValue < 0 || wValue >= sapDigraph.V()) {
                throw new IllegalArgumentException();
            }
        }

        if (!notEmptyArray[0] || !notEmptyArray[1]) {
            return -1;
        }

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(sapDigraph, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(sapDigraph, w);

        return useBFS(vBFS, wBFS, lookingFor);
    }

    private int useBFS(BreadthFirstDirectedPaths vBFS, BreadthFirstDirectedPaths wBFS,
                       FindShortest lookingFor) {
        int shortestPath = Integer.MAX_VALUE;
        int shortestAncestor = -1;
        int newPath = -1;

        for (int v = 0; v < sapDigraph.V(); v++) {
            if (vBFS.hasPathTo(v) && wBFS.hasPathTo(v)) {
                newPath = vBFS.distTo(v) + wBFS.distTo(v);

                if (newPath < shortestPath) {
                    shortestPath = newPath;
                    shortestAncestor = v;
                }
            }
        }

        if (shortestPath == Integer.MAX_VALUE) {
            shortestPath = -1;
        }

        if (lookingFor == FindShortest.PATH_LENGTH) {
            return shortestPath;
        }
        else {
            return shortestAncestor;
        }
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return vertexBFS(v, w, FindShortest.PATH_LENGTH);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return vertexBFS(v, w, FindShortest.COMMON_ANCESTOR);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return verticesBFS(v, w, FindShortest.PATH_LENGTH);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return verticesBFS(v, w, FindShortest.COMMON_ANCESTOR);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        int[] vArr = new int[] { 13, 23, 24 };
        List<Integer> vList = Arrays.stream(vArr)        // IntStream
                                    .boxed()        // Stream<Integer>
                                    .collect(Collectors.toList());
        int[] wArr = new int[] { 6, 16, 17 };
        List<Integer> wList = Arrays.stream(wArr)        // IntStream
                                    .boxed()        // Stream<Integer>
                                    .collect(Collectors.toList());

        int verticesLength = sap.length(vList, wList);
        int verticesAncestor = sap.ancestor(vList, wList);
        StdOut.printf("length = %d, ancestor = %d\n", verticesLength, verticesAncestor);


        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
