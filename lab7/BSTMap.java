import java.util.Set;
import java.util.Iterator;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTMap<K, V> left;
    private BSTMap<K, V> right;
    K thisKey;
    V thisValue;

    /* Creates an empty key node*/
    public BSTMap() {
    }


    @Override
    public void clear() {
        left = null;
        right = null;
        thisKey = null;
        thisValue = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (thisKey == null) {
            return false;
        } else if (thisKey.equals(key)) {
            return true;
        } else if (key.compareTo(thisKey) < 0) {
            if (left == null) {
                return false;
            }
            return left.containsKey(key);
        } else {
            if (right == null) {
                return false;
            }
            return right.containsKey(key);
        }
    }

    @Override
    public V get(K key) {
        if (thisKey == null) {
            return null;
        }
        if (thisKey.equals(key)) {
            return thisValue;
        } else if (key.compareTo(thisKey) < 0) {
            if (left == null) {
                return null;
            }
            return left.get(key);
        } else {
            if (right == null) {
                return null;
            }
            return right.get(key);
        }
    }

    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        if (thisKey == null) {
            return 0;
        } else if (left == null) {
            if (right == null) {
                return 1;
            } else {
                return 1 + right.size();
            }
        } else {
            if (right == null) {
                return left.size() + 1;
            } else {
                return 1 + left.size() + right.size();
            }
        }
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if (thisKey == null) {
            thisValue = value;
            thisKey = key;
        } else if (thisKey.equals(key)) {
            thisValue = value;
        } else if (key.compareTo(thisKey) < 0){
            if (left == null) {
                left = new BSTMap<>();
            }
            left.put(key, value);
        } else {
            if (right == null) {
                right = new BSTMap<>();
            }
            right.put(key, value);
        }
    }

    @Override
    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return null;
    }


    private void printInOrder() {

    }

}
