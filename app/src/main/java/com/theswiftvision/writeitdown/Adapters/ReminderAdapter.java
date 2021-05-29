package com.theswiftvision.writeitdown.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.theswiftvision.writeitdown.ModelClasses.AlarmDetails;
import com.theswiftvision.writeitdown.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    ArrayList<AlarmDetails> alarmDetailsList = new ArrayList<>();
    Context context;
    Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, tomorrow;
    private String time = String.valueOf(System.currentTimeMillis()-1);

    public ReminderAdapter(ArrayList<AlarmDetails> alarmDetailsList, Context context) {
        this.alarmDetailsList = alarmDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmDetails alarmDetails = alarmDetailsList.get(position);
        holder.alarmTitle.setText(alarmDetails.getmTitle());
        holder.alarmTime.setText(alarmDetails.getmTime());

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());

        if (alarmDetails.getmDate().equals(date)&&alarmDetails.getmTime().equals(time)) {
            holder.alarmStatus.setImageResource(R.drawable.alarm_done);
            holder.clock.setVisibility(View.INVISIBLE);
            holder.alarmDone.setVisibility(View.VISIBLE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.theswiftvision.writeitdown.Activities.AlarmDetails.class);
                intent.putExtra("title", alarmDetails.getmTitle());
                intent.putExtra("mNotes", alarmDetails.getmNotes());
                intent.putExtra("time", alarmDetails.getmTime());
                intent.putExtra("date", alarmDetails.getmDate());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarmDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView alarmStatus, clock;
        TextView alarmTitle, alarmTime, alarmDone;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmStatus = itemView.findViewById(R.id.alarm_status);
            alarmTitle = itemView.findViewById(R.id.alarm_title);
            alarmTime = itemView.findViewById(R.id.alarm_time);
            cardView = itemView.findViewById(R.id.cardView);
            clock = itemView.findViewById(R.id.clock);
            alarmDone = itemView.findViewById(R.id.done);
        }
    }
}
