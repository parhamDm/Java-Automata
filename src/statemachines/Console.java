/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statemachines;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author parham
 */
public class Console {

    public Console() {
        ArrayList<StateMachine> al = new ArrayList();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("->");
            String command = in.nextLine();
            command = command.replaceAll("\\s+", "");
            switch (command) {
                case "exit":
                    System.exit(0);
                    break;
                case "add":
                    System.out.println("number of rows:");
                    int t = 0;
                    try {
                        t = (new Scanner(System.in)).nextInt();
                    } catch (Exception e) {
                        System.err.println("invalid input");
                        continue;
                    }
                    String[][] str = new String[t][t];
                    System.out.println("type all rows of table\n type null "
                            + "for leaving it empty");
                    for (int i = 0; i < t; i++) {
                        String validator = "";
                        for (int j = 0; j < t; j++) {
                            System.out.print("a[" + i + "][" + j + "] = ");
                            String value = (new Scanner(System.in)).nextLine().replaceAll("[^a-zA-Z ]", "").toLowerCase();
                            if (value.equals("null") || value.equals("")) {
                                str[i][j] = "";
                            } else {
                                str[i][j] = value;
                                validator += value;
                            }

                        }
                    }
                    System.out.println("write graph name:");
                    al.add(new StateMachine(t, str, (new Scanner(System.in)).nextLine()));
                    System.out.println("graph saved");
                    break;
                case "list":
                    getList(al);
                    break;
                case "removecycle":
                    t = choose(al);
                    if (t == -1) {
                        continue;
                    }
                    al.get(t).deleteAllCycles();
                    System.out.println("cycles removed");
                    break;
                case "iscycled":
                    t = choose(al);
                    if (t == -1) {
                        continue;
                    }
                    System.out.println(al.get(t).isCycled());
                    break;
                case "writetofile":
                    t = choose(al);
                    if (t == -1) {
                        continue;
                    }
                    System.out.println("write name of file");
                    al.get(t).draw(new Scanner(System.in).nextLine());
                    break;
                case "checkstring":
                    t = choose(al);
                    if (t == -1) {
                        continue;
                    }
                    System.out.println("type string,start state,end space with space");   
                    Scanner s= new Scanner(System.in);
                    System.out.println(al.get(t).validator(s.next(),0,s.nextInt(),s.nextInt()));
                    break;
            }
        }
    }

    private int choose(ArrayList<StateMachine> al) {
        int t = 0;
        int i = 0;
        System.out.println("choose graph (int only)");
        for (StateMachine sm : al) {
            System.out.println((i++) + " : " + sm.name);
        }
        try {
            t = (new Scanner(System.in)).nextInt();
        } catch (Exception e) {
            System.err.println("invalid input");
            return -1;
        }
        if (t > al.size()) {
            System.err.println("invalid input");
            return -1;
        }
        return t;
    }

    private void getList(ArrayList<StateMachine> alsm) {
        for (StateMachine sm : alsm) {
            System.out.println("<<" + sm.name + ">>");
            System.out.print(sm);
        }
    }

    private boolean isValid(String str) {
        char s[] = str.toCharArray();
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s.length; j++) {
                if (i != j && s[i] == s[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
