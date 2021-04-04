package com.example.writeitdown.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.writeitdown.Adapters.PermanentListAdapter;
import com.example.writeitdown.ModelClasses.PermanentList;
import com.example.writeitdown.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainListRC;
    ArrayList<PermanentList> permanentListArrayList = new ArrayList<>();
    ArrayList<PermanentList> tempList = new ArrayList<>();
    FloatingActionButton addListBtn;
    TextInputEditText editText;
    static String category;
    PermanentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*-----Hooks------*/
        addListBtn = findViewById(R.id.add_btn);
        mainListRC = findViewById(R.id.hardcode_rc_list);


        inflateData(permanentListArrayList);
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

    public void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.alert_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.add_category_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText  = findViewById(R.id.category_ET);
                category = editText.getText().toString().trim();
                permanentListArrayList.add(new PermanentList(category,getResources().getColor(R.color.category_work)));
                adapter.notifyDataSetChanged();
            }
        });


        dialog.show();
    }

    public void inflateData(ArrayList<PermanentList> permanentLists) {
        permanentLists.add(new PermanentList("Work", R.drawable.work, getResources().getColor(R.color.category_work)));
        permanentLists.add(new PermanentList("School", R.drawable.school, getResources().getColor(R.color.category_school)));
        permanentLists.add(new PermanentList("Shopping", R.drawable.supermarket, getResources().getColor(R.color.category_shopping)));
        permanentLists.add(new PermanentList("Cooking", R.drawable.chef, getResources().getColor(R.color.category_cooking)));
        permanentLists.add(new PermanentList("Note", R.drawable.reminder, getResources().getColor(R.color.category_note)));
        permanentLists.add(new PermanentList("Book", R.drawable.book, getResources().getColor(R.color.category_blue)));

    }

}