package aufgabe1;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedArrayDictionary<K extends Comparable<? super K> , V> implements Dictionary<K, V> {
    private Comparator<? super K> cmp;



    public SortedArrayDictionary() {
        cmp = (x,y) -> ((Comparable<? super K>) x).compareTo(y);
    }

    public SortedArrayDictionary(Comparator<? super K> c) {
        if (c == null) {
            cmp = (x, y) -> ((Comparable<? super K>) x).compareTo(y);
        } else {
            cmp = c;
        }
    }


    @Override
    public V insert(K key, V value) {
        return null;
    }

    @Override
    public V search(K key) {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
