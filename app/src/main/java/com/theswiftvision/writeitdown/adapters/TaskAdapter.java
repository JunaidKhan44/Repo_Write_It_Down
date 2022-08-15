package com.theswiftvision.writeitdown.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theswiftvision.writeitdown.activities.TaskActivity;
import com.theswiftvision.writeitdown.modelclasses.TaskList;
import com.theswiftvision.writeitdown.R;
import com.theswiftvision.writeitdown.staticclass.Global;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

   public static ArrayList<TaskList> taskLists = new ArrayList<>();
   private Context context;


    public TaskAdapter(ArrayList<TaskList> taskLists, Context context) {
        this.taskLists = taskLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskList taskModel = taskLists.get(position);
        holder.task.setText(taskModel.getmTask());
        if(taskLists.get(position).getCount() ==1){
            holder.addTask.setImageResource(R.drawable.task_done);
            holder.task.setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        holder.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskModel.setCount(1);
                taskLists.set(position,taskModel);
                Paper.book().write(Global.categoryTitle,taskLists);
                notifyDataSetChanged();
                Log.i("getCount", "onClick: "  +taskModel.getCount() + " " +Global.categoryTitle );
            }
        });


        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to delete?"  );
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, Html.fromHtml("<font color='#607cc8'>Yes</font>"),
                        (dialog, which) -> {
                            try{
                                taskLists.remove(position);
                                notifyItemRemoved(position);
                                Paper.book().write(Global.categoryTitle,taskLists);
                                TaskActivity.updateList(taskLists);
                                notifyDataSetChanged();

                            }catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, Html.fromHtml("<font color='#607cc8'>Cancel</font>"),
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        });
    }

    public static ArrayList<TaskList> getTaskLists(){
        return taskLists;
    }
    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addTask,deleteTask;
        TextView task;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addTask = itemView.findViewById(R.id.task_icon);
            deleteTask = itemView.findViewById(R.id.delete_task_icon);
            task = itemView.findViewById(R.id.tasksTextView);
        }
    }
}
