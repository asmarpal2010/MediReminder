package com.atefmatar.medireminder.data.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * نموذج بيانات الدواء
 */
public class Medication {
    private String id;
    private String name;
    private String dosage;
    private String form; // حبوب، شراب، حقن، إلخ
    private String instructions;
    private String imageUri;
    private String notes;
    private String color;
    private Date startDate;
    private Date endDate;
    private List<Reminder> reminders;
    private boolean active;

    public Medication() {
        this.id = UUID.randomUUID().toString();
        this.active = true;
    }

    // الدوال الخاصة بالحصول على البيانات وتعديلها

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
