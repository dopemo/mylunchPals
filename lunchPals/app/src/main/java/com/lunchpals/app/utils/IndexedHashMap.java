package com.lunchpals.app.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by moritz on 11/13/17.
 */

public class IndexedHashMap<K, V> extends HashMap<K, V> {

    private ArrayList<K> keys = new ArrayList<>();

    @Override
    public V put(K key, V value) {
        keys.add(key);
        return super.put(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        keys.remove(key);
        return super.remove(key, value);
    }

    public Object getItemAtPosition(int position){
        return super.get(keys.get(position));
    }

    public int getId(K key) {
        return keys.indexOf(key);
    }

    public K getKeyAtPosition(int position) {
        return keys.get(position);
    }
}
