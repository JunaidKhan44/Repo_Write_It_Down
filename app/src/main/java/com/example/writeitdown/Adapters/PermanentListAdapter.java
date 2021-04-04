package com.example.writeitdown.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writeitdown.Activities.TaskActivity;
import com.example.writeitdown.ModelClasses.PermanentList;
import com.example.writeitdown.R;

import java.util.ArrayList;

public class PermanentListAdapter extends RecyclerView.Adapter<PermanentListAdapter.ViewHolder> {

    ArrayList<PermanentList> categoryArrayList = new ArrayList<>();
    ArrayList<PermanentList> tempList;
    Context context;

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
      holder.constraintLayout.setBackgroundColor(permanentList.getmBgColors());

      if(categoryArrayList.size()== 6){
          holder.deleteBtn.setVisibility(View.INVISIBLE);
      }
      if(categoryArrayList.size()>6){
          holder.images.setVisibility(View.INVISIBLE);
      }

      holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(context.getApplicationContext(), TaskActivity.class);
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
        TextView title;
        ImageView images, deleteBtn;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainList = itemView.findViewById(R.id.main_list);
            title = itemView.findViewById(R.id.category_type_name_tv);
            images = itemView.findViewById(R.id.icon_Image);
            constraintLayout =itemView.findViewById(R.id.parent_layout);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }
    }
}
