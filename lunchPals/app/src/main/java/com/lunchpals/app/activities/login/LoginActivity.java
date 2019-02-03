package com.lunchpals.app.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;
import com.lunchpals.app.user.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextemail;
    private EditText editTextpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if(User.getFirebaseUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextemail = (EditText) findViewById(R.id.emailtext);
        editTextpassword = (EditText) findViewById(R.id.passText);
    }


    public void registerUser(View view){

        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"PLEASE ENTER EMAIL",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"PLEASE ENTER Password",Toast.LENGTH_LONG).show();
            return;
        }

        User.getFirebaseAuth().createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"REGISTRATION Sucessfull",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this,"REGISTRATION Failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void userLogin(View view) {
        String email= editTextemail.getText().toString();
        String password=editTextpassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"PLEASE ENTER EMAIL",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"PLEASE ENTER Password",Toast.LENGTH_LONG).show();
            return;
        }

        User.getFirebaseAuth().signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this,"Sorry you were not found in our database",Toast.LENGTH_LONG).show();
                }
            }
       });


    }


}

