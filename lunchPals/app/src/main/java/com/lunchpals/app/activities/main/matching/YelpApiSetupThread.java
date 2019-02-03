package com.lunchpals.app.activities.main.matching;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import java.io.IOException;

/**
 * Created by moritz on 11/13/17.
 */

public class YelpApiSetupThread extends Thread{

    private static final String TAG = "YelpApiSetup";

    private PackageManager packageManager;
    private String packageName;

    public YelpApiSetupThread(Activity activity){
        packageManager = activity.getPackageManager();
        packageName = activity.getPackageName();
    }

    public void run() {
        try {
            Log.d(TAG, "Yelp API is being set up...");
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            Bundle bundle = ai.metaData;

            String yelpApiKey = bundle.getString("yelpClientId");
            String yelpApiSecret = bundle.getString("yelpClientSecret");

            Log.d(TAG, "ApiKey: " + yelpApiKey);

            YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();

            YelpFusionApi yelpFusionApi = apiFactory.createAPI(yelpApiKey, yelpApiSecret);
            YelpApi.setYelpFusionApi(yelpFusionApi);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(YelpApi.hasYelpFusionApi())
                Log.d(TAG, "YelpFusionApi ready to use!");
            else
                Log.d(TAG, "YelpFusionApi could not be set up correctly.");
        }
    }

}
