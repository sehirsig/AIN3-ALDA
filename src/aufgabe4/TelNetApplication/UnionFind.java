package aufgabe4.TelNetApplication;

public class UnionFind {

    private int size;
    private int[] p;

    public UnionFind(int n) { //Erstelle ein int Array in der Größe n, und setze jeden Wert auf -1 (Alles sind Bäume)
        p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = -1;
        }
        size = n;
    }

    public int find(int e) {
        while (p[e] >= 0) { // e ist keine Wurzel
            e = p[e];
        }
        return e;
    }

    public void union(int s1, int s2) { //Union-By-Height
        if (p[s1] >= 0 || p[s2] >= 0) {
            return;
        }
        if (s1 == s2) {
            return;
        }

        if (-p[s1] < -p[s2]) {  // Höhe von s1 < Höhe von s2
            p[s1] = s2;
        } else {
            if (-p[s1] == -p[s2]) {
                p[s1]--; // Höhe von s1 erhöht sich um 1
            }
            p[s2] = s1;
        }
        size--;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(12);
        System.out.println("Neuer Wald erstellt mit -> " + uf.size + " <- Bäumen");
        uf.union(0,1);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(5,8);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(4,6);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(2,10);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(3,11);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(7,0);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(0,4);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);
        uf.union(9,2);
        System.out.println("Partitioniert ... Neue Size: " + uf.size);


        System.out.println("Die Zahl 0 ist in: " + uf.find(0));
        System.out.println("Die Zahl 1 ist in: " + uf.find(1));
        System.out.println("Die Zahl 2 ist in: " + uf.find(2));
        System.out.println("Die Zahl 3 ist in: " + uf.find(3));
        System.out.println("Die Zahl 4 ist in: " + uf.find(4));
        System.out.println("Die Zahl 5 ist in: " + uf.find(5));
        System.out.println("Die Zahl 6 ist in: " + uf.find(6));
        System.out.println("Die Zahl 7 ist in: " + uf.find(7));
        System.out.println("Die Zahl 8 ist in: " + uf.find(8));
        System.out.println("Die Zahl 9 ist in: " + uf.find(9));
        System.out.println("Die Zahl 10 ist in: " + uf.find(10));
        System.out.println("Die Zahl 11 ist in: " + uf.find(11));

        for (int i = 0; i < 12; i++) {
            if (uf.p[i] < 0) {
                System.out.println("The Height of [" + i + "] is = " + uf.p[i]);
            }
        }
        uf.union(3,2);
        System.out.println("Partitioniere 3 mit 2");
        for (int i = 0; i < 12; i++) {
            if (uf.p[i] < 0) {
                System.out.println("The Height of [" + i + "] is = " + uf.p[i]);
            }
        }
        uf.union(3,0);
        System.out.println("Partitioniere 3 mit 0");
        for (int i = 0; i < 12; i++) {
            if (uf.p[i] < 0) {
                System.out.println("The Height of [" + i + "] is = " + uf.p[i]);
            }
        }
        uf.union(3,5);
        System.out.println("Partitioniere 3 mit 5");
        for (int i = 0; i < 12; i++) {
            if (uf.p[i] < 0) {
                System.out.println("The Height of [" + i + "] is = " + uf.p[i]);
            }
        }
        System.out.println("Die Endgröße beträgt: " + uf.size());
        System.out.println("Tabellenform:");
        for (int i = 0; i < 12; i++) {
            System.out.println("|" + i + "| => |" + uf.p[i] + "|");
        }
    }
}
