package com.lunchpals.app.activities.main.matching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lunchpals.app.R;
import com.lunchpals.app.user.UserInformation;
import com.lunchpals.app.utils.IndexedHashMap;


public class MatchedUsersListAdapter extends BaseAdapter {

    private Context context;
    private IndexedHashMap<String, UserInformation> users;
    private static LayoutInflater inflater = null;

    public MatchedUsersListAdapter(Context context, IndexedHashMap<String, UserInformation> users) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.users = users;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return users.getItemAtPosition(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (vi == null)
            vi = inflater.inflate(R.layout.matched_user_list_item, null);

        TextView name = vi.findViewById(R.id.matched_user_name);

        name.setText(users.getItemAtPosition(position).toString());

        ImageView profile_pic = vi.findViewById(R.id.matched_user_image);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + users.getKeyAtPosition(position));
        Glide.with(vi.getContext() /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .placeholder(R.drawable.ic_action_profile_pic)
                .error(R.drawable.ic_action_profile_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profile_pic);

        return vi;
    }
}
