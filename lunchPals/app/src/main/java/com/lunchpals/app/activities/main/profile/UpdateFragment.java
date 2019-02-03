package com.lunchpals.app.activities.main.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainFragment;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

//import com.google.firebase.

public class UpdateFragment extends MainFragment {
    private static final int PICK_IMAGE_REQUEST = 234;
    private ImageView imagev;
    private Button c_pic, u_pic;
    private Uri filepath;
    public TextView username;
    private StorageReference storageReference;
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email;
    private DatabaseReference mDatabaseRef;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public ProgressBar progressBar;
    public int a;
    public Handler handler = new Handler();


    public void onStart() {
        //FacebookSdk.sdkInitialize(getApplicationContext());


        progressBar = (ProgressBar) getActivity().findViewById(R.id.indeterminateBar);
        storageReference = FirebaseStorage.getInstance().getReference();
        imagev = (ImageView) getActivity().findViewById(R.id.imageView);
        c_pic = (Button) getActivity().findViewById(R.id.choose);
        u_pic = (Button) getActivity().findViewById(R.id.upload);
        //accessing user pictures in device
        c_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), PICK_IMAGE_REQUEST);


            }

        });
        email = user.getEmail();
        u_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadfile();

            }
        });
        username = (TextView) getActivity().findViewById(R.id.username);
        super.onStart();
    }

    //uploads the image to firebase server storage
    public void uploadfile() {
        if (filepath != null) {

            StorageReference riversRef = storageReference.child("images/" + email);//accessing storage reference

            riversRef.putFile(filepath)//uploading the image to server
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(), "Upload Complete", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ContactsContract.Profile.class));
                            //uploading the image to fireabase storage


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            a = (int) (progress);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(a);


                                        }
                                    });
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "hurrah", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).start();


                        }
                    });
            ;
        } else {


        }


    }
    //@Override
    protected void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&& resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            filepath=data.getData();//stores the images you loaded
            try {
                //putting into an image View
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),filepath);
                imagev.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public String getImageExt(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimTypemap=MimeTypeMap.getSingleton();
        return mimTypemap.getExtensionFromMimeType(contentResolver.getType(uri));


    }


}














