package aufgabe4.TelNetApplication;

import aufgabe4.princetonStdLib.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TelNet {

    private Map<TelKnoten, Integer> nummerierung;// = new TreeMap<>();
    private List<TelVerbindung> minSpanTree;
    private int size;
    private int lbg;

    public TelNet(int lbg) {
        this.lbg = lbg;
        this.nummerierung = new HashMap<>();
        this.minSpanTree = new LinkedList<>();
    }

    public boolean addTelKnoten(int x, int y) {
        TelKnoten temp = new TelKnoten(x, y);
        if (!nummerierung.containsKey(temp)) {
            nummerierung.put(temp, size++);
            return true;
        } else {
            return false;
        }
    }

    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(size);
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(size, Comparator.comparing(v -> v.c)); // cost miteinander vergleichen (c)
        minSpanTree.clear();

        for (var a: nummerierung.entrySet()) {
            for (var b : nummerierung.entrySet()) {
                if (a.equals(b)) {
                    continue;
                } else {
                    int kosten = (Math.abs(a.getKey().x - b.getKey().x) + Math.abs(a.getKey().y - b.getKey().y)); //Manhattan Distanz
                    if (kosten <= this.lbg) {
                        edges.add(new TelVerbindung(a.getKey(), b.getKey(), kosten));
                    }
                }
            }
        }

        while (forest.size() != 1 && !edges.isEmpty()) {
            TelVerbindung tempTV = edges.poll();
            int t1 = forest.find(nummerierung.get(tempTV.u));
            int t2 = forest.find(nummerierung.get(tempTV.v));
            if (t1 != t2) {
                forest.union(t1,t2);
                minSpanTree.add(tempTV);
            }
        }

        if (forest.size() != 1) {
            return false;
        } else {
            return true;
        }
    }

    public List<TelVerbindung> getOptTelNet() throws java.lang.IllegalStateException {
        return minSpanTree;
    }

    public int getOptTelNetKosten() throws java.lang.IllegalStateException {
        int tempAllCost = 0;
        for (var s : minSpanTree) {
            tempAllCost += s.c;
        }
        return tempAllCost;
    }

    public void drawOptTelNetBIG(int xMax, int yMax) throws java.lang.IllegalStateException {
        System.out.println("Start Drawing!");
        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xMax + 1);
        StdDraw.setYscale(0, yMax + 1);
        for(var v : minSpanTree) {
            double x = v.u.x;
            double y = v.v.y;
            StdDraw.line(v.u.x, v.u.y, x, y);
            StdDraw.line(x, y, v.v.x, v.v.y);
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(v.u.x, v.u.y, 2.5);
            StdDraw.filledSquare(v.v.x, v.v.y, 2.5);
            StdDraw.setPenColor(Color.BLACK);
        }
        StdDraw.show(0);
        System.out.println("Done Drawing!");
    }

    public void drawOptTelNetSmall(int xMax, int yMax) throws java.lang.IllegalStateException {
        System.out.println("Start Drawing!");
        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xMax + 1);
        StdDraw.setYscale(0, yMax + 1);

        for(var v : minSpanTree) {
            double x = v.u.x;
            double y = v.v.y;
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(v.u.x, v.u.y, 0.5);
            StdDraw.filledSquare(v.v.x, v.v.y, 0.5);
        }

        StdDraw.setPenColor(Color.BLACK);
        for (int i = 0; i < yMax; i++) {
            StdDraw.line(0.5, i + 0.5, yMax + 0.5, i + 0.5);
        }
        for (int i = 0; i < xMax; i++) {
            StdDraw.line(i + 0.5, 0.5, i + 0.5, xMax + 0.5);
        }
        StdDraw.line(0.5, yMax + 0.5, xMax + 0.5, yMax + 0.5);
        StdDraw.line(xMax + 0.5, 0.5, xMax + 0.5, yMax + 0.5);

        for(var v : minSpanTree) {
            double x = v.u.x;
            double y = v.v.y;
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(v.u.x, v.u.y, x, y);
            StdDraw.line(x, y, v.v.x, v.v.y);
            StdDraw.filledCircle(v.u.x, v.u.y, 0.15);
            StdDraw.filledCircle(v.v.x, v.v.y, 0.15);
        }
        StdDraw.show(0);
        System.out.println("Done Drawing!");
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        for (int i = 0; i < n; i++) {
            int x = (int)(Math.random() * xMax);
            int y = (int)(Math.random() * yMax);
            if (!this.addTelKnoten(x,y)) {
                i--;
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        for (var s : minSpanTree)
            sb.append(s.toString() + "\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Baum 1");
        TelNet newTN = new TelNet(7);
        newTN.addTelKnoten(1,1);
        newTN.addTelKnoten(3,1);
        newTN.addTelKnoten(4,2);
        newTN.addTelKnoten(3,4);
        newTN.addTelKnoten(7,5);
        newTN.addTelKnoten(2,6);
        newTN.addTelKnoten(4,7);


        //newTN.generateRandomTelNet(200,200,200);

        System.out.println("Gibt es einen einzelnen Baum nach dem computen: " + newTN.computeOptTelNet());
        System.out.println("Gesamte Kosten: " + newTN.getOptTelNetKosten());
        System.out.println("TelNet Size is: " + newTN.size());

        //newTN.drawOptTelNetSmall(7,7);

        System.out.println("Baum 2");
        int max = 1000;
        TelNet newTN2 = new TelNet(100);

        newTN2.generateRandomTelNet(max, max, max);

        System.out.println("Gibt es einen einzelnen Baum nach dem computen: " + newTN2.computeOptTelNet());
        System.out.println("Gesamte Kosten: " + newTN2.getOptTelNetKosten());
        System.out.println("TelNet Size is: " + newTN2.size());
        newTN2.drawOptTelNetBIG(max, max);

    }
}
