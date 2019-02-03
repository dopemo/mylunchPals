package com.lunchpals.app.activities.main.filter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lunchpals.app.R;

import java.util.ArrayList;

public class FilterListAdapter extends BaseAdapter {

    Context context;
    ArrayList<FilterEntry> filters = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private String TAG = "FilterListAdapter";

    public FilterListAdapter(Context context, ArrayList<FilterEntry> filters) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.filters = filters;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filters.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return filters.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        FilterEntry currentFilter = filters.get(position);

        if (vi == null)
            vi = inflater.inflate(R.layout.filter_entry, null);

        TextView title = vi.findViewById(R.id.filter_title);
        TextView desc = vi.findViewById(R.id.filter_desc);
        currentFilter.setDescView(desc);
        ImageView icon = vi.findViewById(R.id.filter_icon);

        title.setText(currentFilter.getTitle());
        desc.setText(currentFilter.getDescription());
        icon.setImageResource(currentFilter.getIcon());
        
        if(currentFilter.getArgs() == null){
            currentFilter.setArgs(new Bundle());
        }

        //Log.d(TAG, "Filter " + currentFilter.toString() + " isEnabled=" + currentFilter.isEnabled());


        Switch cBox = vi.findViewById(R.id.filter_check);
        cBox.setOnClickListener(currentFilter.getCheckBoxListener());

        if (currentFilter.isEnabled()) {
            cBox.setChecked(true);
        }

        vi.findViewById(R.id.filter_entry).setOnClickListener(currentFilter.getOnClickListener());

        return vi;
    }
}
