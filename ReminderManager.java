package com.atefmatar.medireminder.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.model.Reminder;
import com.atefmatar.medireminder.ui.reminder.ReminderActivity;

import java.util.Calendar;

/**
 * خدمة إدارة التذكيرات والإشعارات
 */
public class ReminderManager {
    private static final String CHANNEL_ID = "medication_reminder_channel";
    private static final String CHANNEL_NAME = "تذكيرات الدواء";
    private static final String CHANNEL_DESCRIPTION = "إشعارات تذكير بمواعيد الدواء";

    private Context context;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    public ReminderManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 250, 500});
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleReminder(Medication medication, Reminder reminder) {
        // إنشاء نية للتذكير
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("MEDICATION_ID", medication.getId());
        intent.putExtra("REMINDER_ID", reminder.getId());
        intent.putExtra("MEDICATION_NAME", medication.getName());
        intent.putExtra("MEDICATION_DOSAGE", medication.getDosage());
        
        // إنشاء PendingIntent فريد لكل تذكير
        int requestCode = (medication.getId() + reminder.getId()).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // حساب وقت التذكير القادم
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calendar.set(Calendar.MINUTE, reminder.getMinute());
        calendar.set(Calendar.SECOND, 0);
        
        // إذا كان الوقت قد مر اليوم، جدول للغد
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        // جدولة التذكير
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    public void cancelReminder(Medication medication, Reminder reminder) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = (medication.getId() + reminder.getId()).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        alarmManager.cancel(pendingIntent);
    }

    public void showNotification(String medicationId, String medicationName, String dosage) {
        // إنشاء نية لفتح شاشة التذكير
        Intent intent = new Intent(context, ReminderActivity.class);
        intent.putExtra("MEDICATION_ID", medicationId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // إنشاء نية لتأكيد تناول الدواء
        Intent takenIntent = new Intent(context, NotificationActionReceiver.class);
        takenIntent.setAction("ACTION_TAKEN");
        takenIntent.putExtra("MEDICATION_ID", medicationId);
        PendingIntent takenPendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                takenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // إنشاء نية لتأجيل التذكير
        Intent snoozeIntent = new Intent(context, NotificationActionReceiver.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("MEDICATION_ID", medicationId);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // إعداد نغمة الإشعار
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        // بناء الإشعار
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("تذكير بالدواء")
                .setContentText("حان وقت تناول " + medicationName + " - " + dosage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 500, 250, 500})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_send, "تم التناول", takenPendingIntent)
                .addAction(android.R.drawable.ic_popup_reminder, "تأجيل", snoozePendingIntent);
        
        // عرض الإشعار
        notificationManager.notify(medicationId.hashCode(), notificationBuilder.build());
    }
}
