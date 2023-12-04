package dev.mv.engine.utils.collection;

import dev.mv.engine.utils.function.Hasher;
import dev.mv.engine.utils.function.Prober;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FlatHashMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Prober DEFAULT_PROBER = (i, n, c) -> (i + n * n) % c;
    private static final Hasher DEFAULT_HASHER = (o) -> {
        int h;
        return (o == null) ? 0 : (h = o.hashCode() & 0x7FFFFFFF) ^ (h >>> 16);
    };

    int size;
    Object[] keys, values;
    float loadFactor;
    Prober prober = DEFAULT_PROBER;
    Hasher hasher = DEFAULT_HASHER;

    public FlatHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public FlatHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public FlatHashMap(float loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public FlatHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0 || initialCapacity > MAXIMUM_CAPACITY) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor) || loadFactor > 1) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        keys = new Object[initialCapacity];
        values = new Object[initialCapacity];
    }

    public FlatHashMap<K, V> setProber(Prober prober) {
        this.prober = prober;
        return this;
    }

    public FlatHashMap<K, V> setHasher(Hasher hasher) {
        this.hasher = hasher;
        return this;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int indexOf(Object key) {
        int i = hasher.hash(key) % keys.length;
        int probe = 1;
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                return i;
            }
            i = prober.next(i, probe, keys.length);
            probe++;
        }
        return -1;
    }

    private void grow() {
        int oldCapacity = keys.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity < 0 || newCapacity > MAXIMUM_CAPACITY) {
            newCapacity = MAXIMUM_CAPACITY;
        }
        Object[] oldKeys = keys;
        Object[] oldValues = values;
        keys = new Object[newCapacity];
        values = new Object[newCapacity];
        size = 0;
        int j, probe;
        for (int i = 0; i < oldCapacity; i++) {
            if (oldKeys[i] != null) {
                j = hasher.hash(oldKeys[i]) % keys.length;
                probe = 1;
                while (keys[j] != null) {
                    j = prober.next(j, probe, keys.length);
                    probe++;
                }

                keys[j] = oldKeys[i];
                values[j] = oldValues[i];
                size++;
            }
        }
    }

    public boolean containsKey(Object key) {
        if (key == null) return false;
        return indexOf(key) >= 0;
    }

    public boolean containsValue(Object value) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != null) {
                if (value == null && values[i] == null) return true;
                if (value != null && value.equals(values[i])) return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        if (key == null) return null;
        int index = indexOf(key);
        return index >= 0 ? (V) values[index] : null;
    }

    public V remove(Object key) {
        int h = hasher.hash(key);
        int i = h % keys.length;
        int probe = 1;
        int prev = i;
        boolean back = false;
        V val = null;
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                val = (V) values[i];
                back = true;
                prev = i;
                i = prober.next(i, probe, keys.length);
                probe++;
                continue;
            }
            if (back && hasher.hash(keys[i]) == h) {
                keys[prev] = keys[i];
                values[prev] = values[i];
                prev = i;
            }
            i = prober.next(i, probe, keys.length);
            probe++;
        }
        return val;
    }

    public V put(K key, V value) {
        if (size + 1 >= keys.length * loadFactor) {
            grow();
        }
        int i = hasher.hash(key) % keys.length;
        int probe = 1;
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                V old = (V) values[i];
                values[i] = value;
                return old;
            }
            i = prober.next(i, probe, keys.length);
            probe++;
        }

        keys[i] = key;
        values[i] = value;
        size++;

        return null;
    }

    public void clear() {
        size = 0;
        int capacity = keys.length;
        keys = new Object[capacity];
        values = new Object[capacity];
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]!= null) {
                set.add((K) keys[i]);
            }
        }
        return set;
    }

    @NotNull
    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != null) {
                values.add((V) this.values[i]);
            }
        }
        return values;
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]!= null) {
                set.add(new Node<>((K) keys[i], (V) values[i]));
            }
        }
        return set;
    }

    private static class Node<K, V> implements Entry<K, V> {
        K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return value;
        }
    }
}
