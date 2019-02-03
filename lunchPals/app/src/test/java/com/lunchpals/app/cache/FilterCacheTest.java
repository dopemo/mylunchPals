package com.lunchpals.app.cache;

import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.filter.FilterEntry;
import com.lunchpals.app.activities.main.filter.FilterFragment;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class FilterCacheTest {

    @Test
    public void getFilters() throws Exception {
        FilterCache.setFilters(new ArrayList<FilterEntry>());
        assertNotNull(FilterCache.getFilters());
        assertTrue(FilterCache.getFilters().isEmpty());
    }

    @Test
    public void updateFragment() throws Exception {
        FilterEntry entry = FilterEntry.getFilter(FilterEntry.FILTER.CATEGORIES, new FilterFragment());
        ArrayList<FilterEntry> list = new ArrayList<>();

        list.add(entry);

        FilterCache.setFilters(list);
        FilterFragment fragment = new FilterFragment();
        FilterCache.updateFragment(fragment);
        assertTrue(FilterCache.getFilters().get(0).getFragment() == fragment);
    }

}