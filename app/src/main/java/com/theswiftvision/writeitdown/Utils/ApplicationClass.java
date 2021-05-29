package com.theswiftvision.writeitdown.Utils;

import android.app.Application;

import com.theswiftvision.writeitdown.StaticClass.Global;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try{
            Global.loadInterstitialAdMob(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
