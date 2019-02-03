package com.lunchpals.app.activities.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.login.LoginActivity;
import com.lunchpals.app.activities.main.location.LocationMap;
import com.lunchpals.app.activities.main.matching.YelpApiSetupThread;
import com.lunchpals.app.activities.settings.SettingsActivity;
import com.lunchpals.app.cache.FragmentCache;
import com.lunchpals.app.user.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LocationMap locmap = new LocationMap();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private static final String MAIN_ACTIVITY = "mainActivityKey";

    private int currentFragment = 0;
    private boolean loggingOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupDrawerContent((NavigationView) findViewById(R.id.main_menu_view));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set up Yelp API
        YelpApiSetupThread t = new YelpApiSetupThread(this);
        t.start();

    }

    @Override
    protected void onStart() {
        setContent(FragmentCache.getFragment(MAIN_ACTIVITY));
        loggingOut = false;
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        if (id == R.id.action_chat){
            setContent(R.layout.messenger_fragment);
        }

        return true;
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem){
                        Menu menu = navigationView.getMenu();
                        for (int i = 0; i < menu.size(); i++){
                            menu.getItem(i).setChecked(false);
                        }
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()){
                            case R.id.action_pals:
                                setContent(R.layout.main_fragment);
                                break;
                            case R.id.action_profile:
                                setContent(R.layout.profile_fragment);
                                break;
                            case R.id.action_chat:
                                setContent(R.layout.filter_fragment);
                                break;
                            case R.id.action_settings:
                                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                                break;
                            case R.id.action_logout:
                                //TODO CLEAR CACHE
                                FirebaseAuth.getInstance().signOut();
                                FragmentCache.clearFragmentCache();
                                User.resetUserInformation();
                                loggingOut = true;
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                break;
                            default:
                                return false;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }

                });
    }

    public void setContent(int id) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, MainFragment.newInstance(id), "" + id).commit();
        currentFragment = id;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().findFragmentById(MainFragment.DEFAULT_LAYOUT) != null){

            super.onBackPressed();

        }else{
            setContent(MainFragment.DEFAULT_LAYOUT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(!loggingOut) {
            // Save the current content fragment
            FragmentCache.setFragment(MAIN_ACTIVITY, currentFragment);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    // Look for a request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        locmap.setmLocationPermissionsGranted(false);

        switch(requestCode) {
            case LocationMap.LOCATION_PERMISSION_REQUEST_CODE: {

                if(grantResults.length > 0) {
                    // Check that permissions were granted or not
                    for(int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locmap.setmLocationPermissionsGranted(false);
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    // If all permissions were granted, make mLocationPermissionsGranted true
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    locmap.setmLocationPermissionsGranted(true);
                    //If permission granted reload current fragment to find location
                    setContent(currentFragment);
                }
            }
        }
    }
}
