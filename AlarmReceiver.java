package com.atefmatar.medireminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.repository.MedicationRepository;

/**
 * مستقبل البث للتعامل مع التذكيرات
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String medicationId = intent.getStringExtra("MEDICATION_ID");
        String medicationName = intent.getStringExtra("MEDICATION_NAME");
        String medicationDosage = intent.getStringExtra("MEDICATION_DOSAGE");
        
        // إظهار الإشعار
        ReminderManager reminderManager = new ReminderManager(context);
        reminderManager.showNotification(medicationId, medicationName, medicationDosage);
        
        // إنشاء جرعة جديدة في قاعدة البيانات
        createDoseRecord(medicationId);
        
        // بدء خدمة التذكير (لضمان استمرار التذكير حتى يتم التفاعل معه)
        Intent serviceIntent = new Intent(context, ReminderService.class);
        serviceIntent.putExtra("MEDICATION_ID", medicationId);
        serviceIntent.putExtra("MEDICATION_NAME", medicationName);
        serviceIntent.putExtra("MEDICATION_DOSAGE", medicationDosage);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
    
    private void createDoseRecord(String medicationId) {
        // سيتم تنفيذ منطق إنشاء سجل جرعة جديدة هنا
        // هذا سيتضمن الحصول على مستودع البيانات وإضافة جرعة جديدة بحالة "معلق"
    }
}
