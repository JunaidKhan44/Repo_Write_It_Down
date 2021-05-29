package com.theswiftvision.writeitdown.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.theswiftvision.writeitdown.BroadcastReciever.ReminderReciver;
import com.theswiftvision.writeitdown.BuildConfig;
import com.theswiftvision.writeitdown.R;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AlarmDetails extends AppCompatActivity {

    String mTitle, mNotes, mDate, mTime;
    TextView titleTv, noteTv, dateTv, timeTv;
    private FrameLayout adContainerView;
    private AdView adView;
    int alarmCount;
    Button dismissAlarm;
    ArrayList<com.theswiftvision.writeitdown.ModelClasses.AlarmDetails> alarmDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);
        hooks();

        if (Paper.book().read("Alarms") != null) {
            alarmDetailsList = Paper.book().read("Alarms");
        }
        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });
        if (ReminderReciver.r == null) {
            dismissAlarm.setVisibility(View.INVISIBLE);
        }

        mTitle = getIntent().getStringExtra("title");
        mNotes = getIntent().getStringExtra("mNotes");
        mDate = getIntent().getStringExtra("date");
        mTime = getIntent().getStringExtra("time");
        alarmCount = getIntent().getIntExtra("alarmStatus", 0);
        titleTv.setText(mTitle);
        noteTv.setText(mNotes);
        dateTv.setText(mDate);
        timeTv.setText(mTime);

        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ReminderReciver.r != null) {
                    ReminderReciver.r.stop();
                }


            }
        });
    }

    private void hooks() {
        titleTv = findViewById(R.id.alarmTitle);
        noteTv = findViewById(R.id.alarmNote);
        dateTv = findViewById(R.id.dateTV);
        timeTv = findViewById(R.id.timeTV);
        dismissAlarm = findViewById(R.id.dismiss_alarm);
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ADMOB_BANNER_fake_ID));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = AdSize.BANNER;
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}