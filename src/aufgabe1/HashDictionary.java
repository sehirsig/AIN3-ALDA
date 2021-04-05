package aufgabe1;

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K, V> implements Dictionary<K, V> {
    /**
     * Variablen
     */
    private int size;
    private LinkedList<Entry<K, V>>[] tab;
    private final int DEF_CAPACITY = 31;
    private final int load_factor = 2;
    private int load;

    /**
     * Konstruktor 1
     */
    public HashDictionary() {
        this.tab = new LinkedList[DEF_CAPACITY];
        this.load = this.DEF_CAPACITY;
        this.size = 0;
    }


    /**
     * Konstruktor 2
     */
    public HashDictionary(int n) {
        if (n < 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.tab = new LinkedList[n];
        this.load = n;
        this.size = 0;
    }


    /**
     * Hilfsmethoden
     */
    private boolean check_load() { // True if load is good, False if load is too much
        return (this.load * this.load_factor < size) ? false : true;
    }

    private void add_capacity() {
        int newload = 2 * this.tab.length;

        while (!isPrime(newload)) {
            ++newload;
        }
        HashDictionary<K, V> newtab = new HashDictionary<>(newload);

        for (var list : this.tab) {
            if (list == null) {
                continue;
            } else {
                for (var element : list) {
                    newtab.insert(element.getKey(), element.getValue());
                }
            }
        }

        this.tab = newtab.tab;
        this.load = newload;

    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        } else {
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private int hash(K key) {
        int adr = 0;
        adr = key.hashCode();
        if (adr < 0) {
            adr = -(adr);
        }
        return (adr % (tab.length - 1));
    }



    /**
     * Insert Methode
     */

    @Override
    public V insert(K key, V value) {
        int adr = hash(key);

        if (search(key) != null) {
            for (var element : tab[adr]) {
                if (element.getKey().equals(key)) {
                    V oldValue = element.getValue();
                    element.setValue(value);
                    return oldValue;
                }
            }
        }

        if (!check_load()) {
            add_capacity();
        }

        if (tab[adr] != null) {
            tab[adr].add(new Entry<K, V>(key, value));
        } else {
            tab[adr] = new LinkedList<Entry<K,V>>();
            tab[adr].add(new Entry<K, V>(key, value));
        }
        size++;
        return null;
    }


    /**
     * Search Methode
     */
    @Override
    public V search(K key) {
        int adr = hash(key);
        if (tab[adr] != null) {
            for (var element : tab[adr]) {
                if (element.getKey().equals(key)) {
                    return element.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Remove Methode
     */
    @Override
    public V remove(K key) {
        int adr = hash(key);

        for (var element : tab[adr]) {
            if (element.getKey().equals(key)) {
                V oldValue = element.getValue();
                tab[adr].remove(element);
                size--;
                return oldValue;
            }
        }
        return null;
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
        StringBuilder sb = new StringBuilder();
        for (Dictionary.Entry<K, V> e : this) {
            sb.append(e.getKey() + " : " + e.getValue() + "\n");
        }
        return sb.toString();
    }

    /**
     * Iterator Methode
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionary.HashDictionaryIterator();
    }

    private class HashDictionaryIterator implements Iterator<Entry<K, V>> {
        private int index;
        Iterator<Entry<K,V>> it;

        public HashDictionaryIterator() {
            this.index = -1;
        }

        public boolean hasNext() {
            if (it != null && it.hasNext()) {
                return true;
            } else {
                while (++this.index < tab.length) {
                    if (tab[index]!= null) {
                        it = tab[index].iterator();
                        return it.hasNext();
                    }
                }
                return false;
            }
        }

        public Entry<K, V> next() {
            return it.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
