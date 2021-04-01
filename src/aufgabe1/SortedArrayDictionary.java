package aufgabe1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K> , V> implements Dictionary<K, V> {

    /**
     * Variablen
     */
    private Comparator<? super K> cmp;
    private static final int DEF_CAPACITY = 32;
    private int size;
    private Entry<K,V>[] data;

    /**
     * Konstruktor 1
     */
    public SortedArrayDictionary() {
        cmp = (x,y) -> ((Comparable<? super K>) x).compareTo(y);
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    /**
     * Konstruktor 2
     */
    public SortedArrayDictionary(Comparator<? super K> c) {
        if (c == null) {
            cmp = (x, y) -> ((Comparable<? super K>) x).compareTo(y);
        } else {
            cmp = c;
        }
        this.size = 0;
        this.data = new Entry[DEF_CAPACITY];
    }

    /**
     * Insert Methode
     */
    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        // Vorhandener Eintrag wird überschrieben:
        if (i != -1) {
            V r = this.data[i].getValue();
            this.data[i].setValue(value);
            return r;
        }

        // Neueintrag:
        if (this.data.length == size) {
            this.data = Arrays.copyOf(data, 2*size);
        }
        int j = this.size-1;
        while (j >= 0 && cmp.compare(key, this.data[j].getKey()) < 0) {
            this.data[j+1] = this.data[j];
            j--;
        }
        this.data[j+1] = new Entry<K,V>(key,value);
        this.size++;
        return null;
    }

    /**
     * Search Methode
     */
    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            return this.data[i].getValue();
        }
        else {
            return null;
        }
    }

    private int searchKey(K key) {
        int li = 0;
        int re = this.size - 1;
        while (re >= li) {
            int m = (li + re)/2;
            if (cmp.compare(key, this.data[m].getKey()) < 0) {
                re = m - 1;
            }
            else if (cmp.compare(key, this.data[m].getKey()) > 0) {
                li = m + 1;
            }
            else {
                return m; // key gefunden
            }
        }
        return -1; // key nicht gefunden
    }


    /**
     * Remove Methode
     */
    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1) {
            return null;
        }

        // Datensatz loeschen und Lücke schließen
        V r = this.data[i].getValue();
        for (int j = i; j < this.size-1; j++) {
            this.data[j] = this.data[j + 1];
        }
        this.data[--this.size] = null;
        return r;
    }


    /**
     * size Methode
     */
    @Override
    public int size() {
        return this.size;
    }


    /**
     * toString Methode
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Iterator Methode
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new SortedArrayDictionaryIterator();
    }

    private class SortedArrayDictionaryIterator implements Iterator<Entry<K, V>> {
        private int cursor;

        public SortedArrayDictionaryIterator() {
            this.cursor = 0;
        }

        public boolean hasNext() {
            return this.cursor < SortedArrayDictionary.this.size;
        }

        public Entry<K, V> next() {
            if(this.hasNext()) {
                int current = this.cursor;
                this.cursor ++;
                return data[current];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
