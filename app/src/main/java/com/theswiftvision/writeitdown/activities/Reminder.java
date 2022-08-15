package com.theswiftvision.writeitdown.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.theswiftvision.writeitdown.adapters.ReminderAdapter;
import com.theswiftvision.writeitdown.broadcastreciever.ReminderReciver;
import com.theswiftvision.writeitdown.modelclasses.AlarmDetails;
import com.theswiftvision.writeitdown.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.paperdb.Paper;

public class Reminder extends AppCompatActivity {


    private RecyclerView alarmRecycler;
    public static ArrayList<AlarmDetails> alarmDetailsList = new ArrayList<>();
    private ArrayList<AlarmDetails> todayArrayList = new ArrayList<>();
    private ArrayList<AlarmDetails> tomorrowArrayList = new ArrayList<>();
    public static String mTitle = "", mNotes = "", mDate = "", mTime = "", datePick = "";
    private String mSleepTitle = "Sleep Time ", mWakeupAlarm = "Wakeup Alarm";
    private ReminderAdapter reminderAdapter;
    public int mAlarmStatus;
    private FrameLayout adContainerView;
    private AdView adView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, tomorrow;
    private TextView noReminder, todayList, tomorrowList;
    private ImageView mainList;
    private int year, month, day,alarmStatus;
    SharedPreferences mSharedPref;
    public int alarmRequest = new Random().nextInt();
    private RelativeLayout mSleepTime, mWakeupTime;
    public static int SLEEP_COUNT = 0, WAKEUP_COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        hooks();

        noReminder.setVisibility(View.VISIBLE);
        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(() -> loadBanner());
        Paper.init(getApplicationContext());

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        tomorrow = dateFormat.format(dt);

        mTitle = getIntent().getStringExtra("Title");
        mNotes = getIntent().getStringExtra("mNotes");
        mDate = getIntent().getStringExtra("mDate");
        mTime = getIntent().getStringExtra("mTime");
        alarmStatus  = getIntent().getIntExtra("AlarmStatus",0);

        SLEEP_COUNT = mSharedPref.getInt("sleep", 0);
        WAKEUP_COUNT = mSharedPref.getInt("wakeup", 0);
        if (SLEEP_COUNT == 0) {
            mSleepTime.setBackgroundResource(R.color.category_cooking);
        } else {
            mSleepTime.setBackgroundResource(R.color.colorDarkGrey);
        }

        if (WAKEUP_COUNT == 0) {
            mWakeupTime.setBackgroundResource(R.color.category_school);
        } else {
            mWakeupTime.setBackgroundResource(R.color.colorDarkGrey);
        }
        if (mTime != null && mTitle != null) {
            alarmDetailsList.add(new AlarmDetails(mTitle, mNotes, mTime, mDate, alarmStatus));
            Paper.book().write("Alarms", alarmDetailsList);
            Log.i("alarmDetailsList", "onCreate: " + mTitle + " " + mNotes + " " + mTime + " " + mDate);
        }

        if (Paper.book().read("Alarms") != null) {
            alarmDetailsList = Paper.book().read("Alarms");
            noReminder.setVisibility(View.GONE);
        }

        if (alarmDetailsList != null) {
            taskListRecycler(alarmDetailsList);
        }

        todayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomorrowList.setText("Tomorrow");
                todayList.setTypeface(null, Typeface.BOLD);
                tomorrowList.setTypeface(null, Typeface.NORMAL);
                if (alarmDetailsList != null) {
                    todayArrayList.clear();
                    for (int i = 0; i <= alarmDetailsList.size() - 1; i++) {
                        if (alarmDetailsList.get(i).getmDate().equals(date)) {
                            todayArrayList.add(new AlarmDetails(alarmDetailsList.get(i).getmTitle(), alarmDetailsList.get(i).getmNotes(), alarmDetailsList.get(i).getmTime(),
                                    alarmDetailsList.get(i).getmDate(), alarmDetailsList.get(i).getmAlarmStatus()));
                        }
                    }
                    taskListRecycler(todayArrayList);
                }else{
                    noReminder.setVisibility(View.VISIBLE);
                }
            }
        });

        tomorrowList.setOnClickListener(v -> {
            todayList.setTypeface(null, Typeface.NORMAL);
            tomorrowList.setTypeface(null, Typeface.BOLD);

            if (alarmDetailsList != null) {
                tomorrowArrayList.clear();
                for (int i = 0; i <= alarmDetailsList.size() - 1; i++) {
                    if (alarmDetailsList.get(i).getmDate().equals(tomorrow)) {
                        tomorrowArrayList.add(new AlarmDetails(alarmDetailsList.get(i).getmTitle(), alarmDetailsList.get(i).getmNotes(), alarmDetailsList.get(i).getmTime(),
                                alarmDetailsList.get(i).getmDate(), alarmDetailsList.get(i).getmAlarmStatus()));
                    }
                }
                taskListRecycler(tomorrowArrayList);
            }else{
                noReminder.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.add_reminder_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewReminder.class));
            }
        });
        mSleepTime.setOnClickListener(v -> {
            if (SLEEP_COUNT == 0) {
                SLEEP_COUNT = 1;
                SharedPreferences.Editor edit = mSharedPref.edit();
                edit.putInt("sleep", 1);
                edit.commit();
                edit.apply();
                sleepTime();
                mSleepTime.setBackgroundResource(R.color.colorDarkGrey);
                Toast.makeText(Reminder.this, getString(R.string.msgAlarmNote), Toast.LENGTH_SHORT).show();
            } else if (SLEEP_COUNT == 1){
                SLEEP_COUNT = 0;
                SharedPreferences.Editor edit = mSharedPref.edit();
                edit.putInt("sleep", 0);
                edit.commit();
                edit.apply();
                sleepTime();
                mSleepTime.setBackgroundResource(R.color.category_cooking);
                Toast.makeText(Reminder.this, getString(R.string.msgAlarmDisable), Toast.LENGTH_SHORT).show();
            }
        });
        mainList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        mWakeupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WAKEUP_COUNT == 0) {
                    WAKEUP_COUNT = 1;
                    SharedPreferences.Editor edit = mSharedPref.edit();
                    edit.putInt("wakeup", 1);
                    edit.commit();
                    edit.apply();
                    wakeUpTime();
                    mWakeupTime.setBackgroundResource(R.color.colorDarkGrey);
                    Toast.makeText(Reminder.this, getString(R.string.msgAlarmNote2), Toast.LENGTH_SHORT).show();
                } else if (WAKEUP_COUNT == 1) {
                    WAKEUP_COUNT = 0;
                    SharedPreferences.Editor edit = mSharedPref.edit();
                    edit.putInt("wakeup", 0);
                    edit.commit();
                    edit.apply();
                    mWakeupTime.setBackgroundResource(R.color.category_school);
                    Toast.makeText(Reminder.this, getString(R.string.msgAlarmDisable), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hooks() {
        mSharedPref = getSharedPreferences("MySHaredPreference", Context.MODE_PRIVATE);
        alarmRecycler = findViewById(R.id.alarmRecycler);
        mWakeupTime = findViewById(R.id.wakeUp);
        mSleepTime = findViewById(R.id.sleep_time);
        noReminder = findViewById(R.id.no_reminder);
        mainList = findViewById(R.id.mainList);
        todayList = findViewById(R.id.todayList);
        tomorrowList = findViewById(R.id.tomorrowList);
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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            if (selectedMonth < 10) {
                datePick = selectedDay + "-" + "0" + (selectedMonth + 1) + "-" + selectedYear;
            } else {
                datePick = selectedDay + "-" + (selectedMonth + 1) + "-"
                        + selectedYear;
            }

            tomorrowList.setText(datePick);
            if (alarmDetailsList != null) {
                tomorrowArrayList.clear();
                for (int i = 0; i <= alarmDetailsList.size() - 1; i++) {
                    if (alarmDetailsList.get(i).getmDate().equals(datePick)) {
                        tomorrowArrayList.add(new AlarmDetails(alarmDetailsList.get(i).getmTitle(), alarmDetailsList.get(i).getmNotes(), alarmDetailsList.get(i).getmTime(),
                                alarmDetailsList.get(i).getmDate(), alarmDetailsList.get(i).getmAlarmStatus()));
                    }
                }
                taskListRecycler(tomorrowArrayList);
            }else{
                noReminder.setVisibility(View.VISIBLE);
            }
            Log.i("onDateSet", "onDateSet: " + datePick);
        }
    };

    public void wakeUpTime() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 8);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        createNotificationChannel();
        Intent alarmIntent = new Intent(getApplicationContext(), ReminderReciver.class);
        alarmIntent.putExtra("title", mWakeupAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmRequest, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = mCalendar.getTimeInMillis() - currentTime;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime, pendingIntent);
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

    public void sleepTime() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 22);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        createNotificationChannel();
        Intent alarmIntent = new Intent(getApplicationContext(), ReminderReciver.class);
        alarmIntent.putExtra("title", mSleepTitle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmRequest, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = mCalendar.getTimeInMillis() - currentTime;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime, pendingIntent);
    }

    public void taskListRecycler(ArrayList<AlarmDetails> alarmDetails) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Reminder.this, RecyclerView.VERTICAL, false);
        alarmRecycler.setLayoutManager(layoutManager);
        reminderAdapter = new ReminderAdapter(alarmDetails, Reminder.this);
        alarmRecycler.setAdapter(reminderAdapter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("NotifyLemubit", "Daily Reminder", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}