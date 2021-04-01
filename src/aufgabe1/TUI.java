package aufgabe1;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TUI {

    private static Dictionary<String, String> dict;

    private static long start;
    private static long end;
    private static final long THOUSAND = 1000;

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
            case "benchmark":
            case "b":
                benchmarks(args);
                break;
            default:
                System.err.println(errorMessage);
        }

    }

    private static void count() {
        if (!checkDirExists()) { return; }
        System.out.printf("Number of Words: %d\n", dict.size());
    }

    private static void create(String[] args) {
        if (args.length >= 2) {
            String arg = args[1].toLowerCase();
            if (arg.equals("hashdictionary") || arg.equals("hash") || arg.equals("h")){
                dict = new HashDictionary<>(3);
                System.out.println("HashDictionary created!");
                return;
            } else if (arg.equals("binarytreedictionary") || arg.equals("binary") || arg.equals("b")) {
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


        int number = 1;
        int counter = 0;
        File sFile = null;
        String line;

        try {
            if (args.length >= 2) {
                number = Integer.parseInt(args[1]);
            }
        } catch (IllegalArgumentException e){
            System.err.println(errorMessage);
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

            System.out.printf("Gesteste Dictionary: %s\n-> Messung in Mikro Sekunden µs <-\n\n", dict.getClass().toString());

            if (args.length >= 2) {
                start = System.nanoTime();
                while((line = br.readLine()) != null && counter < number ) {
                    String[] currentLine = line.split(" ");
                    dict.insert(currentLine[0], currentLine[1]);
                    counter++;
                }
                end = System.nanoTime();
                System.out.printf("Dauer für %d Einträge : %d µs\n", dict.size(), (end-start)/THOUSAND);
            } else {
                start = System.nanoTime();
                while((line = br.readLine()) != null) {
                    String[] currentLine = line.split(" ");
                    dict.insert(currentLine[0], currentLine[1]);
                }
                end = System.nanoTime();
                System.out.printf("Dauer für %d Einträge : %d µs\n", dict.size(), (end-start)/THOUSAND);
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
        if (args.length < 2) { System.err.println(errorMessage); return;}
        start = System.nanoTime();
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
        if (args.length < 3) { System.err.println(errorMessage); return;}

        try {
            dict.insert(args[1], args[2]);
            System.out.printf("Added: %s : %s\n", args[1], args[2]);
        } catch (Exception e) {
            System.err.println(errorMessage);
        }

    }

    private static void remove(String[] args) {
        if (!checkDirExists()) { return; }
        if (args.length < 2) { System.err.println(errorMessage); return;}
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
                |   create { binary, hash, \"\" }     |
                |   read { \"\", n (any number) }     |
                |   p (prints the dictionary)       |
                |   s { Wort (in german) }  (search)|
                |   i { Wort word }         (insert)|
                |   r { Wort }              (remove)|
                |   count / c   (size of Dictionary)|
                |   help               (Display help|
                |   exit             (Close process)|
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


    private static void benchmarks(String[] args) {
        if (!checkDirExists()) { return; }

        int number = 8000;
        int counter = 0;
        File sFile = null;
        String line;
        long zeitDeutsch = 0;
        long zeitEnglisch = 0;

        try {
            if (args.length >= 2) {
                number = Integer.parseInt(args[1]);
            }
        } catch (IllegalArgumentException e){
            System.err.println(errorMessage);
            return;
        }

        List<String> deutscheWoerter = new LinkedList<>();
        List<String> englischeWoerter = new LinkedList<>();

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

            while((line = br.readLine()) != null && counter < number ) {
                String[] currentLine = line.split(" ");
                dict.insert(currentLine[0], currentLine[1]);
                deutscheWoerter.add(currentLine[0]);
                englischeWoerter.add(currentLine[1]);

                counter++;
            }


            br.close();

        } catch (Exception e) {
            System.err.println("Error while reading file!");
        }
        System.out.printf("Gesteste Dictionary: %s\n-> Messung in Mikro Sekunden µs <-\n\n", dict.getClass().toString());

        ListIterator<String> itD = deutscheWoerter.listIterator();
        int c = 0;
        for (var words : deutscheWoerter) {
            start = System.nanoTime();
            dict.search(itD.next());
            end = System.nanoTime();
            zeitDeutsch += (end-start)/THOUSAND;
            c++;
        }
        System.out.printf("Dauer für %d erfolgreiche Einträge : %d µs\n", c, zeitDeutsch);

        ListIterator<String> itE = englischeWoerter.listIterator();
        c = 0;
        for (var words : englischeWoerter) {
            start = System.nanoTime();
            dict.search(itE.next());
            end = System.nanoTime();
            zeitEnglisch += (end-start)/THOUSAND;
            c++;
        }
        System.out.printf("Dauer für %d nicht erfolgreiche Einträge : %d µs\n", c, zeitEnglisch);
    }
}
