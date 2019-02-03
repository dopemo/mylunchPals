package com.lunchpals.app.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class IndexedHashMapTest {

    IndexedHashMap<String, String> map = new IndexedHashMap<>();
    String key = "key";
    String val = "val";

    @Test
    public void put() throws Exception {
        map.put(key, val);
        assertTrue(map.containsKey(key));
    }

    @Test
    public void remove() throws Exception {
        map.put(key, val);
        assertTrue(map.containsKey(key));
        map.remove(key);
        assertFalse(map.containsKey(key));
    }

    @Test
    public void getItemAtPosition() throws Exception {
        map.put(key, val);
        map.put(val, key);
        assertEquals(map.getItemAtPosition(1), key);
    }

    @Test
    public void getId() throws Exception {
        map.put(key, val);
        assertNotNull(map.getId(key));
    }

    @Test
    public void getKeyAtPosition() throws Exception {
        map.put(key, val);
        assertEquals(map.getKeyAtPosition(0), key);
    }

}