package com.lunchpals.app.cache;

import com.lunchpals.app.activities.main.filter.FilterEntry;
import com.lunchpals.app.activities.main.filter.FilterFragment;

import java.util.ArrayList;

/**
 * Created by moritz on 10/9/17.
 */

public class FilterCache {

    private static ArrayList<FilterEntry> filters;

    public static ArrayList<FilterEntry> getFilters() {
        return filters;
    }

    public static void setFilters(ArrayList<FilterEntry> filter) {
        filters = filter;
    }

    public static void updateFragment(FilterFragment filterFragment) {
        for (FilterEntry filter : filters){
            filter.setFragment(filterFragment);
        }
    }
}
