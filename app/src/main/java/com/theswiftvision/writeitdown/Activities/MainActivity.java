package com.theswiftvision.writeitdown.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.theswiftvision.writeitdown.Adapters.PermanentListAdapter;
import com.theswiftvision.writeitdown.ModelClasses.PermanentList;
import com.theswiftvision.writeitdown.R;
import com.theswiftvision.writeitdown.StaticClass.Global;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainListRC;
    ArrayList<PermanentList> permanentListArrayList = new ArrayList<>();
    ArrayList<PermanentList> tempList = new ArrayList<>();
    RelativeLayout addListBtn;
    static String category, categoryTaskList;
    PermanentListAdapter adapter;
    private FrameLayout adContainerView;
    private AdView adView;
    SharedPreferences mSharedPref;
    String isFirstTime = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*-----Hooks------*/
        addListBtn = findViewById(R.id.add_btn);
        mainListRC = findViewById(R.id.hardcode_rc_list);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().build());
        adContainerView = findViewById(R.id.ad_view_container);
        Global.loadInterstitialAdMob(this);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        Paper.init(getApplicationContext());
        inflateData(permanentListArrayList);


        mSharedPref = getSharedPreferences("MySHaredPreference", Context.MODE_PRIVATE);
        categoryTaskList = mSharedPref.getString("taskList", null);

        isFirstTime = mSharedPref.getString("isFirst", "No");

        if ("Yes".equals(isFirstTime)) {
            permanentListArrayList = Paper.book().read("categories");
        }

        mainListRecycler(permanentListArrayList);

        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

    }

    public void mainListRecycler(ArrayList<PermanentList> permanentLists) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mainListRC.setLayoutManager(layoutManager);
        adapter = new PermanentListAdapter(permanentLists, MainActivity.this);
        mainListRC.setAdapter(adapter);
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

    public void dialog() {
        if (Global.mInterstitialAd != null) {
            Global.mInterstitialAd.show(MainActivity.this);
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        //this is what I did to added the layout to the alert dialog
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        final EditText editText = (EditText) layout.findViewById(R.id.category_ET);
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.add_category_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList.clear();

                if (editText.getText().toString().equals("")) {
                    editText.setError("Add Category Name");
                    return;
                } else {
                    int[] colors = new int[]{R.color.category_blue,
                            R.color.category_note,
                            R.color.category_cooking,
                            R.color.category_school,
                            R.color.category_shopping,
                            R.color.category_work};
                    int randomColor = colors[new Random().nextInt(colors.length)];
                    category = editText.getText().toString().trim();
                    tempList.add(new PermanentList(category, 0, getResources().getColor(randomColor), R.drawable.add, R.drawable.delete));
                    permanentListArrayList.addAll(tempList);
                    Paper.book().write("categories", permanentListArrayList);
                    SharedPreferences.Editor edit = mSharedPref.edit();
                    edit.putString("isFirst", "Yes");
                    edit.commit();
                    edit.apply();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }


    public void inflateData(ArrayList<PermanentList> permanentLists) {
        permanentLists.add(new PermanentList("Work", R.drawable.work, getResources().getColor(R.color.category_work), R.drawable.add));
        permanentLists.add(new PermanentList("School", R.drawable.school, getResources().getColor(R.color.category_school), R.drawable.add));
        permanentLists.add(new PermanentList("Shopping", R.drawable.supermarket, getResources().getColor(R.color.category_shopping), R.drawable.add));
        permanentLists.add(new PermanentList("Cooking", R.drawable.chef, getResources().getColor(R.color.category_cooking), R.drawable.add));
        permanentLists.add(new PermanentList("Note", R.drawable.reminder, getResources().getColor(R.color.category_note), R.drawable.add));
        permanentLists.add(new PermanentList("Book", R.drawable.book, getResources().getColor(R.color.category_blue), R.drawable.add));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainListRecycler(permanentListArrayList);
        Global.loadInterstitialAdMob(this);
    }
}