package com.lunchpals.app.activities.settings;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lunchpals.app.R;
import com.lunchpals.app.activities.main.MainActivity;


public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public void back(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

}