package com.lunchpals.app.activities.main.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.lunchpals.app.R;
import com.lunchpals.app.requests.RestaurantSearchRequest;



public class FilterEntry {

    public enum FILTER{
        CATEGORIES,
        PRICE,
        RADIUS,
        GROUPSIZE
    }

    private String key;
    private String value;
    private int title;
    private int icon;
    private boolean enabled;

    private String description;
    private TextView desc;

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    private Bundle args;

    public void setDescView(TextView desc) {
        this.desc = desc;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded = false;

    public FilterFragment getFragment() {
        return fragment;
    }

    public void setFragment(FilterFragment fragment) {
        this.fragment = fragment;
    }

    private FilterFragment fragment;

    private static final String TAG = "Filters";

    private FilterEntry(String key, int title, int icon, FilterFragment fragment) {
        this.key = key;
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
        setEnabled(false);
    }

    @Nullable
    public static FilterEntry getFilter(FILTER filter, FilterFragment fragment) {
        switch (filter){
            case CATEGORIES:
                return new FilterEntry(RestaurantSearchRequest.TERM, R.string.cuisine, R.drawable.ic_action_chef, fragment);
            case PRICE:
                return new FilterEntry(RestaurantSearchRequest.PRICE, R.string.price, R.drawable.ic_action_price, fragment);
            case RADIUS:
                return new FilterEntry(RestaurantSearchRequest.RADIUS, R.string.radius, R.drawable.ic_action_radius, fragment);
            case GROUPSIZE:
                return new FilterEntry(RestaurantSearchRequest.LIMIT, R.string.group_size, R.drawable.ic_action_group, fragment);
            default:
                return null;
        }
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
        desc.setText(description);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public View.OnClickListener getOnClickListener() {
        return new FilterEntryListener();
    }

    public View.OnClickListener getCheckBoxListener() {
        return new FilterEntryCheckBoxListener();
    }

    protected class FilterEntryListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {

            LinearLayout ll = view.findViewById(R.id.filter_entry_options);
            ListView filterList = fragment.getView().findViewById(R.id.filter_list);

            if (!isExpanded()) {

                String title = ((TextView) view.findViewById(R.id.filter_title)).getText().toString();
                int layout;
                FilterOption filterOption = new FilterOption();

                if (title == fragment.getString(R.string.cuisine)) {
                    layout = R.layout.filter_categories;
                    filterOption.setFilterOption(FILTER.CATEGORIES);
                } else if (title == fragment.getString(R.string.price)) {
                    layout = R.layout.filter_price;
                    filterOption.setFilterOption(FILTER.PRICE);
                } else if( title == fragment.getString(R.string.radius)) {
                    layout = R.layout.filter_radius;
                    filterOption.setFilterOption(FILTER.RADIUS);
                } else /*if(title == fragment.getString(R.string.group_size))*/{
                    layout = R.layout.filter_group_size;
                    filterOption.setFilterOption(FILTER.GROUPSIZE);
                }

                final LayoutInflater factory = fragment.getActivity().getLayoutInflater();
                factory.inflate(layout, ll, true);
                filterOption.notifyFilterOption(ll);
                setExpanded(true);
            }else {
                ll.removeAllViewsInLayout();
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                setExpanded(false);
            }

        }

    }

    private class FilterOption {

        private FILTER filterOption;
        private LinearLayout layout;

        private void setFilterOption(FILTER filterOption) {
            this.filterOption = filterOption;
        }

        private void notifyFilterOption(LinearLayout layout) {
            this.layout = layout;
            switch (filterOption) {
                case PRICE:
                    setUpPriceOption();
                    break;
                case CATEGORIES:
                    setUpCategoriesOption();
                    break;
                case RADIUS:
                    setUpRadiusOption();
                    break;
                case GROUPSIZE:
                    setUpGroupSizeOption();
                    break;
            }
        }

        private void setUpCategoriesOption() {
            // get TextEdit and Button from view
            final EditText textEdit = layout.findViewById(R.id.edit_categories);
            final Button addBtn = layout.findViewById(R.id.add_category_btn);
            final Button clearBtn = layout.findViewById(R.id.clear_categories_btn);

            textEdit.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_SPACE:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_COMMA:
                            addCategory(textEdit.getText().toString(), textEdit);
                            return true;
                        default:
                            return false;
                    }
                }

            });

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addCategory(textEdit.getText().toString(), textEdit);
                }
            });

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textEdit.setText("");
                    value = "";
                    setDescription("");
                }
            });

        }

        private void addCategory(String category, EditText textEdit) {
            if (category.length() > 0){
                category = category.substring(0, 1).toUpperCase() + category.substring(1, category.length());
                value = value == null ? category : value + " " + category;
                if (getDescription().length() > 0) category = ", " + category;
                setDescription(getDescription() + category);
                textEdit.setText("");
                updateSearchRequest();
            }
        }

        private void setUpPriceOption() {

            final String ARG_MIN = "minPrice";
            final String ARG_MAX = "maxPrice";

            // get seekbar from view
            final CrystalRangeSeekbar rangeSeekbar = layout.findViewById(R.id.rangeSeekbar);

            // get min and max text view
            final TextView tvMin = layout.findViewById(R.id.textMin);
            final TextView tvMax = layout.findViewById(R.id.textMax);

            final String dollar = "$";

            // set listener
            rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                @Override
                public void valueChanged(Number minValue, Number maxValue) {
                    int minVal = minValue.intValue();
                    int maxVal = maxValue.intValue();
                    String min = "", max = "";

                    for (int i = 0; i < minVal; i++){
                        min += dollar;
                    }
                    for (int i = 0; i < maxVal; i++){
                        max += dollar;
                    }

                    tvMin.setText(min);
                    tvMax.setText(max);

                    setDescription(min + " - " + max);
                }
            });

            // set final value listener
            rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
                @Override
                public void finalValue(Number minValue, Number maxValue) {
                    int minVal = minValue.intValue();
                    int maxVal = maxValue.intValue();
                    value = "";
                    for (int i = minVal; i <= maxVal; i++){
                        if(i > minVal)
                            value += ",";
                        value += i;
                    }
                    args.putInt(ARG_MIN, minVal);
                    args.putInt(ARG_MAX, maxVal);
                    updateSearchRequest();
                }
            });

            if(getArgs().get(ARG_MIN) != null && getArgs().get(ARG_MAX) != null){
                rangeSeekbar.setMinStartValue(getArgs().getInt(ARG_MIN));
                rangeSeekbar.setMaxStartValue(getArgs().getInt(ARG_MAX));
                rangeSeekbar.apply();
            }

        }

        private void setUpGroupSizeOption() {

            final String ARG_RADIUS = "radius";
            //Define at what steps radius should be changed (example: 500m steps)
            final int radiusSteps = 1;
            //Minimum Radius will be min, default = default + min, max = max + min
            final int radiusMin = 2;
            final int radiusMax = 4;
            int radiusDefault = 1;

            final SeekBar seekbar = layout.findViewById(R.id.filter_radius_seekbar);
            final TextView textview = layout.findViewById(R.id.filter_radius_textView);

            if(getArgs().get(ARG_RADIUS) != null)
                radiusDefault = getArgs().getInt(ARG_RADIUS) - radiusMin;

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress += radiusMin;
                    progress /= radiusSteps;
                    progress *= radiusSteps;
                    value = progress + "";
                    textview.setText(value + " Pals");
                    setDescription("Group Size of " + value);
                    args.putInt(ARG_RADIUS, progress);
                    updateSearchRequest();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });

            seekbar.setMax(radiusMax);
            seekbar.setProgress(radiusDefault);

        }



        private void setUpRadiusOption() {

            final String ARG_RADIUS = "radius";
            //Define at what steps radius should be changed (example: 500m steps)
            final int radiusSteps = 500;
            //Minimum Radius will be min, default = default + min, max = max + min
            final int radiusMin = 2000;
            final int radiusMax = 8000;
            int radiusDefault = 1500;

            final SeekBar seekbar = layout.findViewById(R.id.filter_radius_seekbar);
            final TextView textview = layout.findViewById(R.id.filter_radius_textView);

            if(getArgs().get(ARG_RADIUS) != null)
                radiusDefault = getArgs().getInt(ARG_RADIUS) - radiusMin;

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress += radiusMin;
                    progress /= radiusSteps;
                    progress *= radiusSteps;
                    value = progress + "";
                    textview.setText(value + "m");
                    setDescription(value + " meters");
                    args.putInt(ARG_RADIUS, progress);
                    updateSearchRequest();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

            });

            seekbar.setMax(radiusMax);
            seekbar.setProgress(radiusDefault);

        }


    }

    private class FilterEntryCheckBoxListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isEnabled()){
                setEnabled(false);
            }else{
                setEnabled(true);
            }
            updateSearchRequest();
        }
    }

    private void updateSearchRequest(){
        if (!isEnabled())
            RestaurantSearchRequest.removeFilter(key);
        else if (value != null)
            RestaurantSearchRequest.setFilter(key, value);
    }

}
