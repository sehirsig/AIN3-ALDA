package aufgabe1;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TUI {

    private static Dictionary<String, String> dict;

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
                benchmarks();
                break;
            case "d":
                deleteAll();
                break;
            default:
                System.err.println(errorMessage);
        }

    }

    private static void count() {
        if (!checkDirExists()) { return; }
        System.out.printf("Number of Words: %d\n", dict.size());
    }

    private static void deleteAll() {
        if (!checkDirExists()) { return; }
        List<String> keys = new LinkedList<>();

        for (Dictionary.Entry<String, String> words : dict) {
            keys.add(words.getKey());
        }

        ListIterator<String> itK = keys.listIterator();
        for (var word : keys) {
            dict.remove(word);
        }
    }

    private static void create(String[] args) {
        benchNumber = 0;
        averageDeutsch = 0;
        averageEnglisch = 0;
        averageRead = 0;
        benchNumberRead = 0;
        previousamount = 0;
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

    private static long averageRead = 0;
    private static long benchNumberRead = 0;
    private static int previousamount = 0;

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

        long gemessteZeit = 0;
        long start;
        long end;

        try {
            in = new FileReader(sFile);

            BufferedReader br = new BufferedReader(in);

            System.out.printf("Gesteste Dictionary: %s\n-> Messung in Mikro Sekunden µs <-\n\n", dict.getClass().toString());

            if (args.length >= 2) {
                while((line = br.readLine()) != null && counter < number ) {
                    String[] currentLine = line.split(" ");
                    start = System.nanoTime();
                    dict.insert(currentLine[0], currentLine[1]);
                    end = System.nanoTime();
                    gemessteZeit += (end - start) / THOUSAND;
                    counter++;
                }
            } else {
                while((line = br.readLine()) != null) {
                    String[] currentLine = line.split(" ");
                    start = System.nanoTime();
                    dict.insert(currentLine[0], currentLine[1]);
                    end = System.nanoTime();
                    gemessteZeit += (end - start) / THOUSAND;
                    counter++;
                }
            }
            number = counter;
            System.out.printf("Dauer für %d Einträge : %d µs\n", counter, gemessteZeit);

            averageRead += gemessteZeit;
            benchNumberRead += 1;
            if (previousamount != number) {
                averageRead = gemessteZeit;
                benchNumberRead = 1;
                previousamount = number;
            }
            System.out.printf("\nAverage from %d Runs with %d words: %d µs\n", benchNumberRead, previousamount, (averageRead/benchNumberRead));
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
                |   d           (delete All Entries)|
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

    private static long averageDeutsch = 0;
    private static long averageEnglisch = 0;
    private static long benchNumber = 0;



    private static void benchmarks() {
        if (!checkDirExists()) { return; }

        long zeitDeutsch = 0;
        long zeitEnglisch = 0;

        List<String> deutscheWoerter = new LinkedList<>();
        List<String> englischeWoerter = new LinkedList<>();

        for (Dictionary.Entry<String, String> words : dict) {
            deutscheWoerter.add(words.getKey());
            englischeWoerter.add(words.getValue());
        }

        long startD = 0;
        long endD = 0;
        long startE = 0;
        long endE = 0;


        System.out.printf("Gesteste Dictionary: %s\n-> Messung in Mikro Sekunden µs <-\n\n", dict.getClass().toString());

        ListIterator<String> itD = deutscheWoerter.listIterator();
        int c = 0;
        for (var words : deutscheWoerter) {
            startD = System.nanoTime();
            dict.search(itD.next());
            endD = System.nanoTime();
            zeitDeutsch += (endD-startD)/THOUSAND;
            c++;
        }
        System.out.printf("Dauer für %d erfolgreiche Einträge : %d µs\n", c, zeitDeutsch);

        ListIterator<String> itE = englischeWoerter.listIterator();
        c = 0;
        for (var words : englischeWoerter) {
            startE = System.nanoTime();
            dict.search(itE.next());
            endE = System.nanoTime();
            zeitEnglisch += (endE-startE)/THOUSAND;
            c++;
        }
        System.out.printf("Dauer für %d nicht erfolgreiche Einträge : %d µs\n", c, zeitEnglisch);

        averageDeutsch += zeitDeutsch;
        averageEnglisch += zeitEnglisch;
        benchNumber += 1;
        System.out.printf("\nAverage from %d Runs:\nDeutsch: %d µs\nEnglisch: %d µs\n", benchNumber, (averageDeutsch/benchNumber), (averageEnglisch/benchNumber));
    }
}
