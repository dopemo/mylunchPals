package com.lunchpals.app.activities.main.filter;

import com.lunchpals.app.R;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by moritz on 12/13/17.
 */
public class FilterEntryTest {

    FilterEntry categoryEntry = FilterEntry.getFilter(FilterEntry.FILTER.CATEGORIES, new FilterFragment());
    FilterEntry priceEntry = FilterEntry.getFilter(FilterEntry.FILTER.PRICE, new FilterFragment());
    FilterEntry radiusEntry = FilterEntry.getFilter(FilterEntry.FILTER.RADIUS, new FilterFragment());
    FilterEntry groupsizeEntry = FilterEntry.getFilter(FilterEntry.FILTER.GROUPSIZE, new FilterFragment());


    @Test
    public void getTitle() throws Exception {
        assertNotNull(categoryEntry.getTitle());
        assertNotNull(priceEntry.getTitle());
        assertNotNull(radiusEntry.getTitle());
        assertNotNull(groupsizeEntry.getTitle());


        assertEquals(categoryEntry.getTitle(), R.string.cuisine);
        assertEquals(priceEntry.getTitle(), R.string.price);
        assertEquals(radiusEntry.getTitle(), R.string.radius);
        assertEquals(groupsizeEntry.getTitle(), R.string.group_size);
    }

}