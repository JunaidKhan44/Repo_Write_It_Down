package com.example.writeitdown.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writeitdown.Adapters.PermanentListAdapter;
import com.example.writeitdown.Adapters.TaskAdapter;
import com.example.writeitdown.ModelClasses.PermanentList;
import com.example.writeitdown.ModelClasses.TaskList;
import com.example.writeitdown.R;
import com.example.writeitdown.StaticClass.Global;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TaskActivity extends AppCompatActivity {

    TextView category;
    public static TextView listCount;
    EditText taskET;
    ImageView add;
    RecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;
    ArrayList<TaskList> taskLists = new ArrayList<>();
    private FrameLayout adContainerView;
    SharedPreferences mSharedPref;
    private AdView adView;
    boolean isFirstTime  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        hooks();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
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
        category.setText( Global.categoryTitle);

        isFirstTime = mSharedPref.getBoolean(Global.categoryTitle,  false);
        Log.i("SharedPrefKey", "onCreate: " + isFirstTime);

        if(Paper.book().read(Global.categoryTitle)!=null){
            Log.i("SharedPrefKey", "onCreate: " + Global.categoryTitle);
            taskLists.clear();
            taskLists = Paper.book().read( Global.categoryTitle);
            taskListRecycler(taskLists);
            listCount.setText(String.valueOf(taskLists.size()));

        }else {
            Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
        }
      /*  if (isFirstTime) {


            Log.i("SharedPrefKey", "onCreate: if" +isFirstTime);
        } else {
            Log.i("SharedPrefKey", "onCreate: else" +isFirstTime);
        }*/




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.clicked++;
                String task = taskET.getText().toString().trim();

                if (Global.mInterstitialAd != null) {
                    if (Global.clicked % 3 == 0) {
                        Global.mInterstitialAd.show(TaskActivity.this);
                        Toast.makeText(TaskActivity.this, "Add Task! " + Global.clicked, Toast.LENGTH_SHORT).show();
                        Global.clicked = 0;
                    }
                } else {
                    Global.mInterstitialAd.show(TaskActivity.this);
                }
                if (taskET.getText().toString().trim().equals("")) {
                    Toast.makeText(TaskActivity.this, "Add Task! " + Global.clicked, Toast.LENGTH_SHORT).show();
                    taskET.setError("Add Task!");
                    return;
                } else {

                    taskLists.add(new TaskList(task,0));
                    Paper.book().write(Global.categoryTitle, taskLists);
                    Log.i("addList", "onClick: " + Global.categoryTitle);
                   /* SharedPreferences.Editor edit = mSharedPref.edit();
                    edit.putBoolean(Global.categoryTitle, true);
                    edit.commit();
                    edit.apply();*/
                    taskListRecycler(taskLists);
                    updateList(taskLists);
                   //taskAdapter.notifyDataSetChanged();
                    taskET.getText().clear();
                }
            }
        });

    }

    public static void updateList(ArrayList<TaskList> taskLists){
        listCount.setText(String.valueOf(taskLists.size()));
    }

    public  ArrayList<TaskList> getTaskLists(){
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
        adView.setAdUnitId(getString(R.string.ADMOB_BANNER_REAL_ID));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = AdSize.BANNER;
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    public void hooks() {
        category = findViewById(R.id.category_text);
        listCount = findViewById(R.id.task_item_count);
        taskET = findViewById(R.id.tasksET);
        add = findViewById(R.id.add_task);
        taskRecyclerView = findViewById(R.id.task_RC_list);

    }
}