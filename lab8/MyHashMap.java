import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyHashMap<K,V> implements Map61B<K, V>{
    private double loadF;
    private HashSet keys;
    private List[] hashBrowns;
    private int size;

    public MyHashMap() {
        loadF = 0.75;
        keys = new HashSet(16);
        hashBrowns = new ArrayList[16];
        size = 0;
    }

    public MyHashMap(int initialSize) {
        keys = new HashSet(initialSize);
        hashBrowns = new ArrayList[initialSize];
        loadF = 0.75;
        size = 0;
    }

    public MyHashMap(int initialSize, double loadFactor) {
        keys = new HashSet(initialSize);
        loadF = loadFactor;
        hashBrowns = new ArrayList[initialSize];
        size = 0;
    }

    @Override
    public void clear() {
        keys.clear();
        for (int i = 0; i < hashBrowns.length; i++) {
            hashBrowns[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    @Override
    public V get (K key) {
        int modded = Math.floorMod(key.hashCode(), hashBrowns.length);
        if (containsKey(key)) {
            int index = hashBrowns[modded].indexOf(key);
            return (V) hashBrowns[modded].get(index + 1);
        }
        return null;
    }

    @Override
    public int size () {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int modded = Math.floorMod(key.hashCode(), hashBrowns.length);
        if(!containsKey(key)) {
            if (hashBrowns[modded] == null) {
                hashBrowns[modded] = new ArrayList();
            }
            hashBrowns[modded].add(key);
            hashBrowns[modded].add(value);
            keySet().add(key);
            size += 1;
            if (size / hashBrowns.length >= loadF) {
                resize();
            }
        } else {
            int index = hashBrowns[modded].indexOf(key);
            hashBrowns[modded].set(index + 1, value);
        }
    }

    private void resize() {
        List<K> kMover = new ArrayList<K>();
        List<V> vMover = new ArrayList<V>();
        Iterator<K> it = iterator();
        while (it.hasNext()) {
            K tempKey = it.next();
            V tempValue = get(tempKey);
            kMover.add(tempKey);
            vMover.add(tempValue);
        }
        hashBrowns = new ArrayList[hashBrowns.length * 2];
        keys.clear();
        for (int i = 0; i < kMover.size(); i++) {
            put(kMover.get(i), vMover.get(i));
            size -= 1;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }


    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
