package aufgabe1;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TUI {

    private static Dictionary<String, String> dict;

    public static void main(String[] args) throws Exception {

        System.out.println(welcomeMessage);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("-> ");
            String in = sc.nextLine();
            commands(in);
        }
    }

    private static void commands(String command) throws Exception {

        String args[] = command.split(" ");

        switch (args[0]) {
            case "create":
                create(args);
                break;
            case "read":
                read(args);
                break;
            case "p":
                print();
                break;
            case "s":
                search(args);
                break;
            case "i":
                insert(args);
                break;
            case "r":
                remove(args);
                break;
            case "exit":
                exit();
                break;
            case "help":
                System.out.println(helpString);
                break;
            case "count":
            case "c":
                count();
                break;
            default:
                System.out.println(errorMessage);
        }

    }

    private static void count() {
        if (!checkDirExists()) { return; }
        System.out.printf("Number of Words: %d\n", dict.size());
    }

    private static void create(String[] args) {
        if (args.length >= 2) {
            String arg = args[1].toLowerCase();
            if (arg.equals("hashdictionary")){
                dict = new HashDictionary<>(3);
                System.out.println("HashDictionary created!");
                return;
            } else if (arg.equals("binarytreedictionary")) {
                dict = new BinaryTreeDictionary<>();
                System.out.println("BinaryTreeDictionary created!");
                return;
            }
        }
        dict = new SortedArrayDictionary<>();
        System.out.println("SortedArrayDictionary created!");
    }

    private static void read(String[] args) throws Exception {
        if (!checkDirExists()) { return; }
        //if (args.length < 1) { System.out.println(errorMessage); return;}


        int number = 1;
        int counter = 0;
        File sFile = null;
        String line;

        try {
            if (args.length >= 2) {
                number = Integer.parseInt(args[1]);
            }
        } catch (IllegalArgumentException e){
            System.out.println(errorMessage);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("../AIN3-ALDA/src/aufgabe1"));

        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            sFile = fc.getSelectedFile();
        } else {
            System.err.println("No file selected!");
            return;
        }

        FileReader in;

        try {
            in = new FileReader(sFile);

            BufferedReader br = new BufferedReader(in);

            if (args.length >= 2) {
                while((line = br.readLine()) != null && counter < number ) {
                    String[] currentLine = line.split(" ");
                    dict.insert(currentLine[0], currentLine[1]);
                    counter++;
                }
            } else {
                while((line = br.readLine()) != null) {
                    String[] currentLine = line.split(" ");
                    dict.insert(currentLine[0], currentLine[1]);
                }
            }
            br.close();

        } catch (Exception e) {
            System.err.println("Error while reading file!");
        }

    }

    private static void print() {
        if (!checkDirExists()) { return; }
        System.out.println(dict.toString());
    }

    private static void search(String[] args) {
        if (!checkDirExists()) { return; }
        if (args.length < 2) { System.out.println(errorMessage); return;}
        try {
            String ergebnis = dict.search(args[1]);
            if (ergebnis.equals("null")) { System.out.println("Word doesnt exist!"); }
            System.out.println(ergebnis);
        } catch (NullPointerException e) {
            System.err.println("Word not found!");
        }

    }

    private static void insert(String[] args) {
        if (!checkDirExists()) { return; }
        if (args.length < 3) { System.out.println(errorMessage); return;}
        try {
            dict.insert(args[1], args[2]);
            System.out.printf("Added: %s : %s\n", args[1], args[2]);
        } catch (Exception e) {
            System.err.println(errorMessage);
        }

    }

    private static void remove(String[] args) {
        if (!checkDirExists()) { return; }
        if (args.length < 2) { System.out.println(errorMessage); return;}
        try {
            dict.remove(args[1]);
            System.out.printf("Removed: %s\n", args[1]);
        } catch (Exception e) {
            System.err.println(errorMessage);
        }
    }

    private static void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private static boolean checkDirExists() {
        if (dict == null) {
            System.out.println("Create a Dictionary first! Use: \"help\" !");
            return false;
        }
        return true;
    }


    private static String helpString = """
                ------------------------------------
                |            HELP TABLE             |
                |-----------------------------------|
                |   create                          |
                |   read                            |
                |   p                               |
                |   s                               |
                |   i                               |
                |   r                               |
                |   count                           |
                |   help                            |
                |   exit                            |
                -------------------------------------
                
                """;

    private static String welcomeMessage = """
                ------------------------------------
                |   Deutsch - Englisch Dictionary   |
                |-----------------------------------|
                |       Textbased User Interface    |
                |           HTWG Konstanz 2021      |
                |               v0.0.1              |
                -------------------------------------
                
                Enter Input:
                """;

    private static String errorMessage = "ERROR! Wrong usage! Try \"help\" !";


}
