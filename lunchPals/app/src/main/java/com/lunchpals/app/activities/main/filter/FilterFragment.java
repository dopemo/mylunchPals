package com.lunchpals.app.activities.main.filter;


import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.activities.main.matching.FindMatch;
import com.lunchpals.app.cache.FilterCache;

import java.util.ArrayList;


public class FilterFragment extends MainFragment {

    private static ArrayList<FilterEntry> filters;

    @Override
    public void onStart() {
        ListView ll = this.getActivity().findViewById(R.id.filter_list);
        ll.setAdapter(new FilterListAdapter(this.getActivity(), getFilters()));

        Button apply = getActivity().findViewById(R.id.filter_apply_btn);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setContent(R.layout.main_fragment);
            }
        });

        super.onStart();
    }

    public ArrayList<FilterEntry> getFilters() {
        if (FilterCache.getFilters() != null){
            FilterCache.updateFragment(this);
            filters = FilterCache.getFilters();
        }else {
            filters = new ArrayList<>();
            filters.add(FilterEntry.getFilter(FilterEntry.FILTER.CATEGORIES, this));
            filters.add(FilterEntry.getFilter(FilterEntry.FILTER.PRICE, this));
            filters.add(FilterEntry.getFilter(FilterEntry.FILTER.RADIUS, this));
            filters.add(FilterEntry.getFilter(FilterEntry.FILTER.GROUPSIZE, this));
            FilterCache.setFilters(filters);
        }

        return filters;
    }

    @Override
    public void onStop() {

        FilterCache.setFilters(filters);
        FindMatch.updateFilterInformtion();

        super.onStop();
    }


}
