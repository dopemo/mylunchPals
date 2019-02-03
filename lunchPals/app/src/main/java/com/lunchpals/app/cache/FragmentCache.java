package com.lunchpals.app.cache;

import com.lunchpals.app.activities.main.MainFragment;

import java.util.HashMap;

/**
 * Created by moritz on 10/8/17.
 */

public class FragmentCache {

    private static HashMap<String, Integer> fragments = new HashMap<>();

    public static int getFragment(String key) {
        if(fragments.containsKey(key))
            return fragments.get(key);
        else
            return MainFragment.DEFAULT_LAYOUT;
    }

    public static void setFragment(String key, Integer fragment) {
        fragments.put(key, fragment);
    }

    public static void clearFragmentCache(){
        fragments.clear();
    }
}
