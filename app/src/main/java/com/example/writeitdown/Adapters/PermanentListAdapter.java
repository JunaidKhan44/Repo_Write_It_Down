package com.example.writeitdown.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writeitdown.Activities.MainActivity;
import com.example.writeitdown.Activities.TaskActivity;
import com.example.writeitdown.ModelClasses.PermanentList;
import com.example.writeitdown.ModelClasses.TaskList;
import com.example.writeitdown.R;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import io.paperdb.Paper;

public class PermanentListAdapter extends RecyclerView.Adapter<PermanentListAdapter.ViewHolder> {

    ArrayList<PermanentList> categoryArrayList = new ArrayList<>();
    MainActivity mainActivity;
    Context context;
   /* ArrayList<TaskList> lists = TaskActivity.getTaskLists();*/



    public PermanentListAdapter(ArrayList<PermanentList> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      PermanentList permanentList = categoryArrayList.get(position);
      holder.title.setText(permanentList.getmTitles());
      holder.images.setImageResource(permanentList.getmImages());
      holder.addBtn.setImageResource(permanentList.getmAddBtn());
      holder.deleteBtn.setImageResource(permanentList.getmDeleteBtn());
      holder.constraintLayout.setBackgroundColor(permanentList.getmBgColors());
  /*    holder.taskCount.setText(String.valueOf(TaskActivity.getTaskLists().size()));*/
       // Log.i("taskList", "onBindViewHolder: "+lists.size());

      holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              AlertDialog alertDialog = new AlertDialog.Builder(context).create();
              alertDialog.setTitle("Alert");
              alertDialog.setMessage("Are you sure you want to delete?"  );
              alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                      (dialog, which) -> {
                          try{
                              categoryArrayList.remove(position);
                              notifyItemRemoved(position);
                              Paper.book().write("categories",categoryArrayList);
                              notifyDataSetChanged();
                          }catch (IndexOutOfBoundsException e){
                              e.printStackTrace();
                          }
                      });
              alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                      (dialog, which) -> dialog.dismiss());
              alertDialog.show();

          }
      });

      holder.addBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(context.getApplicationContext(), TaskActivity.class);
              intent.putExtra("categoryTitle",permanentList.getmTitles());
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mainList;
        TextView title,taskCount;
        ImageView images, deleteBtn,addBtn;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainList = itemView.findViewById(R.id.main_list);
            title = itemView.findViewById(R.id.category_type_name_tv);
            images = itemView.findViewById(R.id.icon_Image);
            constraintLayout =itemView.findViewById(R.id.parent_layout);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            addBtn = itemView.findViewById(R.id.addBtn);
            taskCount = itemView.findViewById(R.id.total_tasks);
        }
    }
}
