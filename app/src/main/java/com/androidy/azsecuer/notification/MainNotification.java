package com.androidy.azsecuer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.HomeActivity;

/**
 * Created by ljh on 2016/8/8.
 */
public class MainNotification {
    private static NotificationManager notificationManager = null;
    private static final int NOTIFICATIONID=1;
    public static void openNotification(Context context){
        if(notificationManager==null){
            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_setting);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(PendingIntent.getActivity(context,0,new Intent(context,HomeActivity.class),PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        notificationManager.notify(NOTIFICATIONID,notification);
    }
    public static void closeNotification(Context context){
        if (notificationManager==null){
            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }
        notificationManager.cancel(NOTIFICATIONID);
    }

}
