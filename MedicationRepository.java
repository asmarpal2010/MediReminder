package com.atefmatar.medireminder.data.repository;

import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.model.Reminder;
import com.atefmatar.medireminder.data.model.Dose;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * مستودع بيانات الأدوية والتذكيرات
 */
public class MedicationRepository {
    private static MedicationRepository instance;
    private List<Medication> medications;
    private List<Reminder> reminders;
    private List<Dose> doses;

    private MedicationRepository() {
        medications = new ArrayList<>();
        reminders = new ArrayList<>();
        doses = new ArrayList<>();
    }

    public static synchronized MedicationRepository getInstance() {
        if (instance == null) {
            instance = new MedicationRepository();
        }
        return instance;
    }

    // دوال إدارة الأدوية
    public List<Medication> getAllMedications() {
        return medications;
    }

    public Medication getMedicationById(String id) {
        for (Medication medication : medications) {
            if (medication.getId().equals(id)) {
                return medication;
            }
        }
        return null;
    }

    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public void updateMedication(Medication medication) {
        for (int i = 0; i < medications.size(); i++) {
            if (medications.get(i).getId().equals(medication.getId())) {
                medications.set(i, medication);
                break;
            }
        }
    }

    public void deleteMedication(String id) {
        medications.removeIf(medication -> medication.getId().equals(id));
        reminders.removeIf(reminder -> reminder.getMedicationId().equals(id));
        doses.removeIf(dose -> dose.getMedicationId().equals(id));
    }

    // دوال إدارة التذكيرات
    public List<Reminder> getRemindersForMedication(String medicationId) {
        List<Reminder> medicationReminders = new ArrayList<>();
        for (Reminder reminder : reminders) {
            if (reminder.getMedicationId().equals(medicationId)) {
                medicationReminders.add(reminder);
            }
        }
        return medicationReminders;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    public void updateReminder(Reminder reminder) {
        for (int i = 0; i < reminders.size(); i++) {
            if (reminders.get(i).getId().equals(reminder.getId())) {
                reminders.set(i, reminder);
                break;
            }
        }
    }

    public void deleteReminder(String id) {
        reminders.removeIf(reminder -> reminder.getId().equals(id));
    }

    // دوال إدارة الجرعات
    public List<Dose> getDosesForMedication(String medicationId) {
        List<Dose> medicationDoses = new ArrayList<>();
        for (Dose dose : doses) {
            if (dose.getMedicationId().equals(medicationId)) {
                medicationDoses.add(dose);
            }
        }
        return medicationDoses;
    }

    public List<Dose> getDosesForDate(Date date) {
        // سيتم تنفيذ المنطق لاسترجاع الجرعات ليوم معين
        return new ArrayList<>();
    }

    public void addDose(Dose dose) {
        doses.add(dose);
    }

    public void updateDose(Dose dose) {
        for (int i = 0; i < doses.size(); i++) {
            if (doses.get(i).getId().equals(dose.getId())) {
                doses.set(i, dose);
                break;
            }
        }
    }

    // دوال التقارير والإحصائيات
    public float getAdherenceRate(String medicationId) {
        // سيتم تنفيذ المنطق لحساب معدل الالتزام بالدواء
        return 0.0f;
    }

    public float getOverallAdherenceRate() {
        // سيتم تنفيذ المنطق لحساب معدل الالتزام الإجمالي
        return 0.0f;
    }
}
