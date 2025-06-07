package com.atefmatar.medireminder.data.model;

import java.util.Date;
import java.util.UUID;

/**
 * نموذج بيانات الجرعة
 */
public class Dose {
    private String id;
    private String medicationId;
    private Date scheduledTime;
    private Date takenTime;
    private String status; // تم التناول، تم التخطي، متأخر، معلق
    private String notes;

    public Dose() {
        this.id = UUID.randomUUID().toString();
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

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Date getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(Date takenTime) {
        this.takenTime = takenTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // دالة مساعدة لمعرفة ما إذا كانت الجرعة متأخرة
    public boolean isLate() {
        if (status.equals("تم التناول") || status.equals("تم التخطي")) {
            return false;
        }
        return new Date().after(scheduledTime);
    }
}
