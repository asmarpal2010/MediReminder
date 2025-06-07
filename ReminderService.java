package com.atefmatar.medireminder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.atefmatar.medireminder.ui.reminder.ReminderActivity;

/**
 * خدمة التذكير المستمر
 */
public class ReminderService extends Service {
    private static final String CHANNEL_ID = "medication_reminder_service_channel";
    private static final String CHANNEL_NAME = "خدمة تذكير الدواء";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String medicationId = intent.getStringExtra("MEDICATION_ID");
            String medicationName = intent.getStringExtra("MEDICATION_NAME");
            String medicationDosage = intent.getStringExtra("MEDICATION_DOSAGE");
            
            // إنشاء إشعار الخدمة الأمامية
            Notification notification = createForegroundNotification(medicationId, medicationName, medicationDosage);
            startForeground(NOTIFICATION_ID, notification);
        }
        
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("قناة إشعارات خدمة تذكير الدواء");
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createForegroundNotification(String medicationId, String medicationName, String medicationDosage) {
        // إنشاء نية لفتح شاشة التذكير
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.putExtra("MEDICATION_ID", medicationId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // بناء الإشعار
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("تذكير بالدواء")
                .setContentText("حان وقت تناول " + medicationName + " - " + medicationDosage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }
}
