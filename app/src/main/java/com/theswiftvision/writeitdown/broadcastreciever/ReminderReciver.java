package com.theswiftvision.writeitdown.broadcastreciever;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.theswiftvision.writeitdown.activities.AlarmDetails;
import com.theswiftvision.writeitdown.R;

public class ReminderReciver extends BroadcastReceiver {

    private String title, note,date,time;
    public final int MY_NOTIFICATION_ID = 1;
    private String CHANNEL_ID = "1234";
    public static Ringtone r;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.alarm_reminder);

        r = RingtoneManager.getRingtone(context, soundUri);
        r.setLooping(true);
        r.play();

        Intent activityIntent = new Intent(context.getApplicationContext(), AlarmDetails.class);
        activityIntent.putExtra("taskDone", 1);
        activityIntent.putExtra("title",title);
        activityIntent.putExtra("mNotes",note);
        activityIntent.putExtra("date",date);
        activityIntent.putExtra("time",time);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(activityIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "NotifyLemubit")
                .setSmallIcon(R.drawable.time_icon)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(note)
                .addAction(R.drawable.time_icon, "Dismiss", resultPendingIntent)
                .setNotificationSilent()
                .setAutoCancel(true);
        builder.setFullScreenIntent(resultPendingIntent, true);
        Notification notification = builder.build();
        // notification.flags |= Notification.FLAG_INSISTENT;
        //notification.sound = soundUri;
        //notification.defaults = 0;
        notificationManagerCompat.notify(MY_NOTIFICATION_ID, notification);
       /* NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "NotifyLemubit")
                .setContentTitle(title)
                .setContentText(note)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.time_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(soundUri)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Creating an Audio Attribute
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("NotifyLemubit", title, importance);
            mChannel.setSound(soundUri, audioAttributes);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);

        }

        assert notificationManager != null;
        notificationManager.notify( 0, notificationBuilder.build());
*/

    }
}
