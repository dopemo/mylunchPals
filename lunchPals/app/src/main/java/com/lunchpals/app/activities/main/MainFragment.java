package com.lunchpals.app.activities.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.chat.MessengerFragment;
import com.lunchpals.app.activities.main.filter.FilterFragment;
import com.lunchpals.app.activities.main.matching.FindMatch;
import com.lunchpals.app.activities.main.matching.SearchingMatch;
import com.lunchpals.app.activities.main.matching.ShowMatch;
import com.lunchpals.app.activities.main.profile.ProfileFragment;
import com.lunchpals.app.activities.main.profile.UpdateFragment;

public class  MainFragment extends Fragment {



    public static final String LAYOUT = "layoutId";
    public static final int CHAT_LAYOUT = R.layout.messenger_fragment;
    public static final int PROFILE_LAYOUT = R.layout.profile_fragment;
    public static final int DEFAULT_LAYOUT = R.layout.main_fragment;
    public static final int FILTER_LAYOUT = R.layout.filter_fragment;
    public static final int FIND_MATCH = R.layout.main_fragment;
    public static final int PROFILE_UPDATE = R.layout.activity_profile_update;
    public static final int SEARCH_MATCH = R.layout.searching_for_match_fragment;
    public static final int SHOW_MATCH = R.layout.match_fragment;

    public static MainFragment newInstance(int layout){
        MainFragment f;
        Bundle args = new Bundle();

        switch (layout){
            case FILTER_LAYOUT:
                f = new FilterFragment();
                break;
            case SEARCH_MATCH:
                f = new SearchingMatch();
                break;
            case SHOW_MATCH:
                f = new ShowMatch();
                break;
            case CHAT_LAYOUT:
                f = new MessengerFragment();
                break;
            case PROFILE_LAYOUT:
                f = new ProfileFragment();
                break;
            case PROFILE_UPDATE:
                f=new UpdateFragment();
                break;
            case DEFAULT_LAYOUT:
            default:
                f = new FindMatch();
                layout = DEFAULT_LAYOUT;
        }

        args.putInt(LAYOUT, layout);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getArguments().getInt(LAYOUT), container, false);
    }
}