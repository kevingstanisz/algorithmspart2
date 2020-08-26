/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

// create a baseball division from given filename in format specified below

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {

    private final HashMap<String, Integer> teamToId;
    private int[] teamWins;
    private int[] teamLosses;
    private int[] teamRemaining;
    private int[][] teamGames;
    private String[] teamNames;

    private int numberTeams;

    private FordFulkerson fordF;
    private FlowNetwork flowNetwork;

    public BaseballElimination(String filename) {
        In teamString = new In(filename);

        numberTeams = teamString.readInt();

        teamToId = new HashMap<String, Integer>();

        teamWins = new int[numberTeams];
        teamLosses = new int[numberTeams];
        teamRemaining = new int[numberTeams];
        teamGames = new int[numberTeams][numberTeams];
        teamNames = new String[numberTeams];

        for (int i = 0; i < numberTeams; i++) {

            String teamName = teamString.readString();
            teamToId.put(teamName, i);

            teamWins[i] = teamString.readInt();
            teamLosses[i] = teamString.readInt();
            teamRemaining[i] = teamString.readInt();
            teamNames[i] = teamName;

            for (int j = 0; j < numberTeams; j++) {
                teamGames[i][j] = teamString.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamToId.keySet();
    }

    private void checkTeam(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }

        if (teamToId.get(team) == null) {
            throw new IllegalArgumentException();
        }
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);

        return teamWins[teamToId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);

        return teamLosses[teamToId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);

        return teamRemaining[teamToId.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);

        return teamGames[teamToId.get(team1)][teamToId.get(team2)];
    }

    private int constructGraph(String team) {
        checkTeam(team);

        int selectedTeam = teamToId.get(team);
        int negativeWinsFound = -1;
        int checkWinsDifference = 0;

        FlowEdge flowEdge;

        int combinations = (numberTeams - 1) * (numberTeams - 2) / 2;

        flowNetwork = new FlowNetwork(combinations + numberTeams + 1);
        int edgeNumber = 1;
        int adjustVertex = 0;

        for (int i = 0; i < numberTeams; i++) {
            if (i == selectedTeam) {
                continue;
            }

            for (int j = i + 1; j < numberTeams; j++) {
                if (j == selectedTeam) {
                    continue;
                }

                flowEdge = new FlowEdge(0, edgeNumber, teamGames[i][j]);
                flowNetwork.addEdge(flowEdge);

                adjustVertex = i;
                if (i > selectedTeam) {
                    adjustVertex--;
                }
                flowEdge = new FlowEdge(edgeNumber, combinations + adjustVertex + 1,
                                        Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);

                adjustVertex = j;
                if (j > selectedTeam) {
                    adjustVertex--;
                }
                flowEdge = new FlowEdge(edgeNumber, combinations + adjustVertex + 1,
                                        Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);

                edgeNumber++;
            }

            adjustVertex = i;
            if (i > selectedTeam) {
                adjustVertex--;
            }

            checkWinsDifference = teamWins[selectedTeam] + teamRemaining[selectedTeam]
                    - teamWins[i];

            if (checkWinsDifference < 0) {
                negativeWinsFound = i;
                checkWinsDifference = 0;
            }

            flowEdge = new FlowEdge(combinations + adjustVertex + 1, combinations + numberTeams,
                                    checkWinsDifference);

            flowNetwork.addEdge(flowEdge);
        }

        fordF = new FordFulkerson(flowNetwork, 0, combinations + numberTeams);

        return negativeWinsFound;
    }


    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);

        if (constructGraph(team) > -1) {
            return true;
        }

        for (FlowEdge flowEdge : flowNetwork.adj(0)) {
            if (flowEdge.flow() != flowEdge.capacity()) {
                return true;
            }
        }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        ArrayList<String> eliminatingTeams = new ArrayList<>();

        if (constructGraph(team) > -1) {
            eliminatingTeams.add(teamNames[constructGraph(team)]);
            return eliminatingTeams;
        }

        int selectedTeam = teamToId.get(team);
        int combinations = (numberTeams - 1) * (numberTeams - 2) / 2;


        for (int i = 0; i < numberTeams - 1; i++) {
            if (fordF.inCut(i + combinations + 1)) {
                if (i >= selectedTeam) {
                    eliminatingTeams.add(teamNames[i + 1]);
                }
                else {
                    eliminatingTeams.add(teamNames[i]);
                }
            }
        }

        if (eliminatingTeams.isEmpty()) {
            return null;
        }

        return eliminatingTeams;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
