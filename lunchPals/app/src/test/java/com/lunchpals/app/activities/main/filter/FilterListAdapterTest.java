package com.lunchpals.app.activities.main.filter;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/14/17.
 */
public class FilterListAdapterTest {

    FilterListAdapter listAdapter;
    ArrayList<FilterEntry> filterEntries;

    @Before
    public void setUp(){
        filterEntries = new ArrayList<>();
        filterEntries.add(FilterEntry.getFilter(FilterEntry.FILTER.CATEGORIES, new FilterFragment()));
        filterEntries.add(FilterEntry.getFilter(FilterEntry.FILTER.RADIUS, new FilterFragment()));

        listAdapter = new FilterListAdapter(new Activity(), filterEntries);
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(2, listAdapter.getCount());
    }

    @Test
    public void getItem() throws Exception {
        assertEquals(FilterEntry.getFilter(FilterEntry.FILTER.CATEGORIES, new FilterFragment()).getTitle(),
                ((FilterEntry)listAdapter.getItem(0)).getTitle());
    }

    @Test
    public void getItemId() throws Exception {
        assertEquals(0, listAdapter.getItemId(0));
    }

}