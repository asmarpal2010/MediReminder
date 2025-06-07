package com.atefmatar.medireminder.util;

import android.content.Context;

import com.atefmatar.medireminder.data.model.Dose;
import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.repository.MedicationRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * أداة مساعدة لإنشاء التقارير والإحصائيات
 */
public class ReportGenerator {
    private Context context;
    private MedicationRepository repository;

    public ReportGenerator(Context context) {
        this.context = context;
        this.repository = MedicationRepository.getInstance();
    }

    /**
     * إنشاء تقرير الالتزام الأسبوعي
     */
    public Map<String, Float> generateWeeklyAdherenceReport() {
        Map<String, Float> weeklyReport = new TreeMap<>();
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        // الحصول على بداية الأسبوع (الأحد)
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        
        // حساب معدل الالتزام لكل يوم في الأسبوع
        for (int i = 0; i < 7; i++) {
            Date currentDate = calendar.getTime();
            String dateString = dateFormat.format(currentDate);
            
            List<Dose> dailyDoses = repository.getDosesForDate(currentDate);
            float adherenceRate = calculateDailyAdherenceRate(dailyDoses);
            
            weeklyReport.put(dateString, adherenceRate);
            
            // الانتقال لليوم التالي
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        return weeklyReport;
    }

    /**
     * إنشاء تقرير الالتزام الشهري
     */
    public Map<String, Float> generateMonthlyAdherenceReport() {
        Map<String, Float> monthlyReport = new TreeMap<>();
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        // الحصول على بداية الشهر
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // حساب معدل الالتزام لكل يوم في الشهر
        for (int i = 0; i < daysInMonth; i++) {
            Date currentDate = calendar.getTime();
            String dateString = dateFormat.format(currentDate);
            
            List<Dose> dailyDoses = repository.getDosesForDate(currentDate);
            float adherenceRate = calculateDailyAdherenceRate(dailyDoses);
            
            monthlyReport.put(dateString, adherenceRate);
            
            // الانتقال لليوم التالي
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        return monthlyReport;
    }

    /**
     * إنشاء تقرير الالتزام حسب الدواء
     */
    public Map<String, Float> generateMedicationAdherenceReport() {
        Map<String, Float> medicationReport = new TreeMap<>();
        
        List<Medication> medications = repository.getAllMedications();
        
        for (Medication medication : medications) {
            float adherenceRate = repository.getAdherenceRate(medication.getId());
            medicationReport.put(medication.getName(), adherenceRate);
        }
        
        return medicationReport;
    }

    /**
     * حساب معدل الالتزام اليومي
     */
    private float calculateDailyAdherenceRate(List<Dose> doses) {
        if (doses.isEmpty()) {
            return 0.0f;
        }
        
        int takenCount = 0;
        
        for (Dose dose : doses) {
            if (dose.getStatus().equals("تم التناول")) {
                takenCount++;
            }
        }
        
        return (float) takenCount / doses.size();
    }

    /**
     * إنشاء ملف PDF للتقرير
     */
    public String generatePdfReport() {
        // سيتم تنفيذ منطق إنشاء ملف PDF للتقرير هنا
        return null;
    }
}
