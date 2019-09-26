/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statemachines;

import GraphViz.GraphViz;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author parham
 */
public class StateMachine {

    private final int graphMatrix[][];
    private final ArrayList<Integer>[][] stateTable;
    private final int numberOfStates;
    public final static int NUMBER_OF_POSSIBLE_INPUTS = 26;
    public final static int NO_STATE = -1;
    public String name;

    public StateMachine(int numberOfStates, String[][] machineArray, String name) {
        this.numberOfStates = numberOfStates;
        this.name = name;
        stateTable = new ArrayList[numberOfStates][NUMBER_OF_POSSIBLE_INPUTS];
        graphMatrix = new int[numberOfStates][numberOfStates];
        for (ArrayList[] a : stateTable) {
            for (int i = 0; i < a.length; i++) {
                a[i] = new ArrayList();
            }
        }
        statemaker(machineArray);
    }

    private void statemaker(String[][] machineArray) {
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                if (machineArray[i][j].equals("")) {
                    continue;
                }
                graphMatrix[i][j] = 1;
                char[] c = machineArray[i][j].toCharArray();
                for (int k = 0; k < c.length; k++) {
                    stateTable[i][c[k] - 'a'].add(j);
                }
            }
        }
    }

//    public String stateShower() {
//        String s = "";
//        for (int[] a : stateTable) {
//            s += "state ";
//            for (int i = 0; i < a.length; i++) {
//                s += a[i] + "  ";
//            }
//            s += "\n";
//        }
//        return s;
//    }
//    public boolean validator(String a) {
//        char arr[] = a.toCharArray();
//        int counter = 0;
//        int cs = 0;
//        while (counter < arr.length) {
//            cs = stateTable[cs][arr[counter++] - 'a'];
//            if (cs == -1) {
//                return false;
//            }
//        }
//        if (cs == 0) {
//            return true;
//        }
//        return false;
//    }
    public boolean validator(String a, int counter, int cs,int endS) {
        int checker = 0;
        if (a.length() <= counter) {
            if(cs==endS){
                return true;
            }
            return false;
        }
        ArrayList<Integer> al = stateTable[cs][a.charAt(counter) - 'a'];
        if (al.isEmpty()) {
            return false;
        }
        for (int t : al) {
            if (validator(a, counter+1, t,endS)) {
                checker = 1;
            }
        }
        if (checker==1) {
            return true;
        }
        else return false;
    }

    public void table() {
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                System.out.print(graphMatrix[i][j] + " ");
            }
            System.out.println("\n");
        }

    }

    boolean isCycled() {
        boolean[] visited = new boolean[numberOfStates];
        boolean[] visitedStack = new boolean[numberOfStates];
        for (int i = 0; i < numberOfStates; i++) {
            visited[i] = false;
            visitedStack[i] = false;
        }
        for (int i = 0; i < numberOfStates; i++) {
            if (isCycledRec(0, i, visited, visitedStack)) {
                return true;
            }
        }
        return false;
    }

    void deleteAllCycles() {
        boolean[] visited = new boolean[numberOfStates];
        boolean[] visitedStack = new boolean[numberOfStates];
        for (int i = 0; i < numberOfStates; i++) {
            isCycledRec(1, i, visited, visitedStack);
        }
    }

    private boolean isCycledRec(int action, int cs, boolean[] visited, boolean[] visitedStack) {

        if (visited[cs] == false) {
            visited[cs] = true;
            visitedStack[cs] = true;

            for (int i = 0; i < numberOfStates; i++) {
                if (graphMatrix[cs][i] == 0) {
                    continue;
                }
                if (!visited[i] && isCycledRec(action, i, visited, visitedStack)) {
                    if (action == 0) {

                        return true;
                    }
                } else if (visitedStack[i]) {
                    if (action == 1) {
                        graphMatrix[cs][i] = 0;
                        updateCycle(cs, i);
                    } else if (action == 0) {
                        return true;
                    }
                }
            }
        }
        visitedStack[cs] = false;
        return false;
    }

    private void updateCycle(int cs, int i) {
        for (int j = 0; j < NUMBER_OF_POSSIBLE_INPUTS; j++) {
            for(int k=0;k<stateTable[cs][j].size();k++)
            if (stateTable[cs][j].get(k) == i) {
                stateTable[cs][j].remove(k);
            }
        }
    }

    public void draw(String filename) {
        StringBuilder s = new StringBuilder();
        for (int cs = 0; cs < numberOfStates; cs++) {
            for (int p = 0; p < NUMBER_OF_POSSIBLE_INPUTS; p++) {
                if (!stateTable[cs][p].isEmpty()) {
                    for(int i : stateTable[cs][p])
                    s.append(cs + "->" + i + "[label=" + ((char) (p + 'a')) + "];");
                }
            }
        }/*
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < numberOfStates; j++) {
                if(graphMatrix[i][j]==1){
                    
                }
            }
        }*/
        GraphViz gv = new GraphViz();

        gv.addln(gv.start_graph());
        gv.add(s.toString());
        gv.addln(gv.end_graph());
        File out = new File(filename + "." + "jpg");
        File file = new File(filename + "." + "jpg");
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), "jpg"), out);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int cs = 0; cs < numberOfStates; cs++) {
            s.append("State " + cs + " : ");
            for (int p = 0; p < NUMBER_OF_POSSIBLE_INPUTS; p++) {
                if (!stateTable[cs][p].isEmpty()) {
                    for(int t:stateTable[cs][p])
                    s.append("(" + (t) + "," + ((char) (p + 'a')) + ")");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
