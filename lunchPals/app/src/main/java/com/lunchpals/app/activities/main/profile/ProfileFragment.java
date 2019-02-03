package com.lunchpals.app.activities.main.profile;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.user.User;
import com.lunchpals.app.utils.ImageUpload;

import java.io.File;


public class ProfileFragment extends MainFragment {

    public EditText editTextemail;
    //public EditText username;
    public EditText name;
    private FirebaseAuth firebaseAuth;
    public Button updateProfile;
    public Button updateInfo;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public EditText username1;

    public TextView text;
    private static final int PICK_IMAGE_REQUEST = 234;
    public Uri filepath;
    public ImageView profile_pic;
    public Button logout;
    public Uri photoUrl;
    public TextView username;
    public File localFile;
    public String uri;
    public String generatedFilePath;
    public DatabaseReference mDatabaseRef;
    public ImageUpload img;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public String userId;
    public String TAG;
    public String displayName;
    public Button message_button;
    public UserProfileChangeRequest profileUpdates;
/*mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();*/

    //public FileDownloadTask.TaskSnapshot taskSnapshot;

    @Override
    public void onStart() {
        firebaseAuth = FirebaseAuth.getInstance();
        userId=firebaseAuth.getInstance().getCurrentUser().getUid();
        //AppEventsLogger.activateApp(getApplicationContext());

        text = getActivity().findViewById(R.id.email);
        profile_pic = getActivity().findViewById(R.id.profilepicture);

        if(User.isOnline(this.getContext())) {

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("USER");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //fetches image from firebase database

                    //showData(dataSnapshot);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            profile_pic = getActivity().findViewById(R.id.profilepicture);
            String name = user.getEmail();
            photoUrl = user.getPhotoUrl();
            text.setText(name);

            updateProfile = (Button) getActivity().findViewById(R.id.update);
            updateProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FacebookSdk.sdkInitialize();


                    ((MainActivity)getActivity()).setContent(MainFragment.PROFILE_UPDATE);

                }
            });
            //Glide.get(getApplicationContext()).clearMemory();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + user.getUid());
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(profile_pic);
            username = getActivity().findViewById(R.id.username);
            username.setText(user.getDisplayName());

            updateInfo = getActivity().findViewById(R.id.updateInfo);
            updateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    username1 = getActivity().findViewById(R.id.displayname);
                    displayName = username1.getText().toString();

                    profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
                    user.updateProfile(profileUpdates);
                    username.setText(displayName);


                }
            });
        }

        super.onStart();

    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
