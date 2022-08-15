package com.theswiftvision.writeitdown.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.theswiftvision.writeitdown.broadcastreciever.ReminderReciver;
import com.theswiftvision.writeitdown.modelclasses.AlarmDetails;
import com.theswiftvision.writeitdown.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class NewReminder extends AppCompatActivity {

    ImageView alarmStatus;
    //Uri soundUri =Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://"+ getApplicationContext().getPackageName() + "/" + R.raw.alarm_reminder);
    public static int count = 1,ALARM_COUNT=0;
    int year, month, day, mHour, mMinute;
    int requestID = new Random().nextInt();
    private ImageView datePickerButton, timePickerButton;
    private EditText dateEt, timeEt, titleEt, notesEt;
    private RelativeLayout createAlarmBtn;
    private ArrayList<AlarmDetails> alarmDetailsList;
    private TextView cancelBtn;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    public Calendar cal, mCalendar;
    private FrameLayout adContainerView;
    private AdView adView;
    public static String mTime, mDate, mTitle, mNotes;
    private int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        hooks();

        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alarmStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1) {
                    alarmStatus.setImageResource(R.drawable.alarm_done);
                    ALARM_COUNT++;
                    count = 0;
                } else if (count == 0) {
                    alarmStatus.setImageResource(R.drawable.circle_strokes);
                    count = 1;
                }
            }
        });

        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        createAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (titleEt.getText().toString().equals("")) {
                    titleEt.setError(getString(R.string.msgErrortext));
                    return;
                } else if (notesEt.getText().toString().equals("")) {
                    titleEt.setError(getString(R.string.msgErrortext));
                    return;
                } else if (dateEt.getText().toString().equals("")) {
                    titleEt.setError(getString(R.string.msgErrortext));
                    return;
                } else if (timeEt.getText().toString().equals("")) {
                    titleEt.setError(getString(R.string.msgErrortext));
                    return;
                } else if(count== 1){
                    Toast.makeText(NewReminder.this, getString(R.string.msgAlarmText), Toast.LENGTH_SHORT).show();
                }else {
                    mTimeSplit = mTime.split(":");
                    mDateSplit = mDate.split("-");

                    mHour = Integer.parseInt(mTimeSplit[0]);
                    mMinute = Integer.parseInt(mTimeSplit[1]);
                    day = Integer.parseInt(mDateSplit[0]);
                    month = Integer.parseInt(mDateSplit[1]);
                    year = Integer.parseInt(mDateSplit[2]);

                    mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                    mCalendar.set(Calendar.MINUTE, mMinute);
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH + 1, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, day);
                    mCalendar.set(Calendar.SECOND, 0);

                    createNotificationChannel();
                    Intent alarmIntent = new Intent(getApplicationContext(), ReminderReciver.class);
                    alarmIntent.putExtra("title", mTitle);
                    alarmIntent.putExtra("note", mNotes);
                    alarmIntent.putExtra("date", mDate);
                    alarmIntent.putExtra("time", mTime);
                    alarmIntent.putExtra("alarmStatus",ALARM_COUNT);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestID, alarmIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    // Calculate notification time
                    Calendar c = Calendar.getInstance();
                    long currentTime = c.getTimeInMillis();
                    long diffTime = mCalendar.getTimeInMillis() - currentTime;
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + diffTime, pendingIntent);

                    Intent intent = new Intent(getApplicationContext(), Reminder.class);
                    intent.putExtra("Title", titleEt.getText().toString().trim());
                    intent.putExtra("mNotes", notesEt.getText().toString().trim());
                    intent.putExtra("mTime", mTime);
                    intent.putExtra("mDate", mDate);
                    intent.putExtra("AlarmStatus",ALARM_COUNT);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }

    public void hooks() {
        createAlarmBtn = findViewById(R.id.create_alarm_btn);
        alarmStatus = findViewById(R.id.alarm_on);
        datePickerButton = findViewById(R.id.date);
        timePickerButton = findViewById(R.id.time);
        dateEt = findViewById(R.id.et_date);
        timeEt = findViewById(R.id.et_time);
        titleEt = findViewById(R.id.et_Title);
        notesEt = findViewById(R.id.et_Notes);
        cancelBtn = findViewById(R.id.cancel_btn);
    }
    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ADMOB_BANNER_REAL_ID));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = AdSize.BANNER;
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(this, datePickerListener, year, month, day);


    }

    private void pickTime() {
        cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (minute < 10) {
                    mTime = hourOfDay + ":" + "0" + minute;
                } else {
                    mTime = hourOfDay + ":" + minute;
                }

                if (hourOfDay <= 12) {
                    timeEt.setText(mTime);
                } else {
                    timeEt.setText(mTime);
                }

                mTime = timeEt.getText().toString().trim();
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            if(selectedMonth <10){
                mDate = selectedDay + "-" + "0" +(selectedMonth + 1) + "-"+selectedYear;
            }else{
                mDate = selectedDay + "-" + (selectedMonth + 1) + "-"
                        + selectedYear;
            }
            dateEt.setText(mDate);

            mDate = dateEt.getText().toString();
            Log.i("onDateSet", "onDateSet: " + mDate);
        }
    };

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mTitle = titleEt.getText().toString().trim();
            mNotes = notesEt.getText().toString().trim();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("NotifyLemubit", mTitle, importance);
            channel.setDescription(mNotes);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


}