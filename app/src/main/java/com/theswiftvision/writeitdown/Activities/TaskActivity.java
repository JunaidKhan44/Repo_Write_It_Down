package com.theswiftvision.writeitdown.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theswiftvision.writeitdown.Adapters.TaskAdapter;
import com.theswiftvision.writeitdown.ModelClasses.TaskList;
import com.theswiftvision.writeitdown.R;
import com.theswiftvision.writeitdown.StaticClass.Global;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TaskActivity extends AppCompatActivity {

    TextView category;
    public static TextView listCount;
    EditText taskET;
    ImageView add;
    RelativeLayout addReminderBtn;
    RecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;
    static ArrayList<TaskList> taskLists = new ArrayList<>();
    private FrameLayout adContainerView;
    SharedPreferences mSharedPref;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        hooks();
        taskLists.clear();
        MobileAds.initialize(this, initializationStatus -> {
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().build());
        adContainerView = findViewById(R.id.ad_view_container);
        mSharedPref = getSharedPreferences("MySHaredPreference", Context.MODE_PRIVATE);

        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });
        Global.loadInterstitialAdMob(this);

        Global.categoryTitle = getIntent().getStringExtra("categoryTitle");
        category.setText(Global.categoryTitle);

        if (Paper.book().read(Global.categoryTitle) != null) {
            taskLists.clear();
            taskLists = Paper.book().read(Global.categoryTitle);
            taskListRecycler(taskLists);
            listCount.setText(String.valueOf(taskLists.size()));
        }

        taskET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Global.clicked++;
                    String task = taskET.getText().toString().trim();

                    if (Global.mInterstitialAd != null) {
                        if (Global.clicked % 3 == 0) {
                            Global.mInterstitialAd.show(TaskActivity.this);
                            Global.clicked = 0;
                        }
                    }
                    if (taskET.getText().toString().trim().equals("")) {
                        taskET.setError("Add Task!");
                        return false;
                    } else {
                        taskLists.add(new TaskList(task, 0));
                        Paper.book().write(Global.categoryTitle, taskLists);
                        SharedPreferences.Editor edit = mSharedPref.edit();
                        edit.putString("taskList", Global.categoryTitle);
                        edit.commit();
                        edit.apply();
                        taskListRecycler(taskLists);
                        updateList(taskLists);
                        taskET.getText().clear();
                    }
                    return true;
                }
                return false;
            }
        });

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.clicked++;
                String task = taskET.getText().toString().trim();

                if (Global.mInterstitialAd != null) {
                    if (Global.clicked % 3 == 0) {
                        Global.mInterstitialAd.show(TaskActivity.this);
                        Global.clicked = 0;
                    }
                }
                if (taskET.getText().toString().trim().equals("")) {
                    taskET.setError("Add Task!");
                    return;
                } else {
                    taskLists.add(new TaskList(task, 0));
                    Paper.book().write(Global.categoryTitle, taskLists);
                    SharedPreferences.Editor edit = mSharedPref.edit();
                    edit.putString("taskList", Global.categoryTitle);
                    edit.commit();
                    edit.apply();
                    taskListRecycler(taskLists);
                    updateList(taskLists);
                    taskET.getText().clear();
                }
            }
        });

    }

    public static void updateList(ArrayList<TaskList> taskLists) {
        listCount.setText(String.valueOf(taskLists.size()));
    }

    public static ArrayList<TaskList> getTaskLists() {
        return taskLists;
    }

    public void taskListRecycler(ArrayList<TaskList> taskList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TaskActivity.this, RecyclerView.VERTICAL, false);
        taskRecyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(taskList, TaskActivity.this);
        taskRecyclerView.setAdapter(taskAdapter);
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

    public void hooks() {
        addReminderBtn = findViewById(R.id.add_reminder_btn);
        category = findViewById(R.id.category_text);
        listCount = findViewById(R.id.task_item_count);
        taskET = findViewById(R.id.tasksET);
        add = findViewById(R.id.add_task);
        taskRecyclerView = findViewById(R.id.task_RC_list);

    }
}