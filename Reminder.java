package com.atefmatar.medireminder.data.model;

import java.util.Date;
import java.util.UUID;

/**
 * نموذج بيانات التذكير
 */
public class Reminder {
    private String id;
    private String medicationId;
    private int hour;
    private int minute;
    private boolean[] daysOfWeek; // [الأحد، الإثنين، الثلاثاء، الأربعاء، الخميس، الجمعة، السبت]
    private String notificationType; // صوت، اهتزاز، صامت
    private boolean enabled;

    public Reminder() {
        this.id = UUID.randomUUID().toString();
        this.daysOfWeek = new boolean[7];
        this.enabled = true;
    }

    // الدوال الخاصة بالحصول على البيانات وتعديلها

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // دالة مساعدة للحصول على وقت التذكير القادم
    public Date getNextReminderTime() {
        // سيتم تنفيذ المنطق لحساب وقت التذكير القادم هنا
        return null;
    }
}
