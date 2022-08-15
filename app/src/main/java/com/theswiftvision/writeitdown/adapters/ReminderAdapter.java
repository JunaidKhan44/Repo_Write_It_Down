package com.theswiftvision.writeitdown.adapters;

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

import com.theswiftvision.writeitdown.modelclasses.AlarmDetails;
import com.theswiftvision.writeitdown.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private ArrayList<AlarmDetails> alarmDetailsList = new ArrayList<>();
    private Context context;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, tomorrow;
    private String time = String.valueOf(System.currentTimeMillis() - 1);
    private String currentDate = null;

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

        //get date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());


        /**/
        Date alarmDate = null;
        Date currentDate = null;
        try {

            alarmDate = dateFormat.parse(alarmDetails.getmDate());
            currentDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**/

        /**/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String getCurrentTime = sdf.format(c.getTime());  //current time
        String getTestTime = alarmDetails.getmTime(); //old time

//        if (getCurrentTime .compareTo(getTestTime) < 0 &&)
//        {
//            // Do your staff
//            Log.d("Return","getTestTime less than getCurrentTime ");
//
//        }
//        else
//
        if (getCurrentTime.compareTo(getTestTime) > 0 && currentDate.equals(alarmDate)) {
            Log.d("Return", "getTestTime older than getCurrentTime ");
            holder.alarmStatus.setImageResource(R.drawable.alarm_done);
            holder.clock.setVisibility(View.INVISIBLE);
            holder.alarmDone.setVisibility(View.VISIBLE);
            holder.cardView.setEnabled(false);
            
        }else{
            holder.cardView.setEnabled(true);
        }
        /**/


//
//        if (alarmDetails.getmDate().equals(date)&&alarmDetails.getmTime().equals(time)) {
//            holder.alarmStatus.setImageResource(R.drawable.alarm_done);
//            holder.clock.setVisibility(View.INVISIBLE);
//            holder.alarmDone.setVisibility(View.VISIBLE);
//            Log.d("tt","equals call");
//        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, com.theswiftvision.writeitdown.activities.AlarmDetails.class);
                intent.putExtra("title", alarmDetails.getmTitle());
                intent.putExtra("mNotes", alarmDetails.getmNotes());
                intent.putExtra("time", alarmDetails.getmTime());
                intent.putExtra("date", alarmDetails.getmDate());
                context.startActivity(intent);

            }
        });

        Log.d("tag", "today date" + alarmDetails.getmDate());

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
