package com.atefmatar.medireminder.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.repository.MedicationRepository;
import com.atefmatar.medireminder.ui.medication.AddEditMedicationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * الشاشة الرئيسية للتطبيق
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView tvDate;
    private TextView tvUpcomingMedications;
    private FloatingActionButton fabAddMedication;
    private BottomNavigationView bottomNavigationView;
    
    private MedicationRepository medicationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);
        
        // تهيئة مستودع البيانات
        medicationRepository = MedicationRepository.getInstance();
        
        // تهيئة عناصر واجهة المستخدم
        initializeViews();
        
        // إعداد المستمعين للأحداث
        setupListeners();
        
        // عرض التاريخ الحالي
        displayCurrentDate();
        
        // عرض الأدوية القادمة
        displayUpcomingMedications();
    }
    
    private void initializeViews() {
        // هذه مجرد عناصر وهمية للتوضيح، في التطبيق الفعلي سيتم استخدام تخطيط مخصص
        tvDate = new TextView(this);
        tvUpcomingMedications = new TextView(this);
        fabAddMedication = new FloatingActionButton(this);
        bottomNavigationView = new BottomNavigationView(this);
        
        // إعداد شريط التنقل السفلي
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
    
    private void setupListeners() {
        // إعداد مستمع لزر إضافة دواء جديد
        fabAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال إلى شاشة إضافة دواء جديد
                Intent intent = new Intent(MainActivity.this, AddEditMedicationActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void displayCurrentDate() {
        // عرض التاريخ الحالي بتنسيق مناسب
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("ar"));
        String currentDate = dateFormat.format(new Date());
        tvDate.setText(currentDate);
    }
    
    private void displayUpcomingMedications() {
        // الحصول على قائمة الأدوية
        List<Medication> medications = medicationRepository.getAllMedications();
        
        // عرض الأدوية القادمة (هذا مجرد مثال مبسط)
        StringBuilder medicationsText = new StringBuilder();
        
        if (medications.isEmpty()) {
            medicationsText.append("لا توجد أدوية مجدولة لليوم");
        } else {
            for (Medication medication : medications) {
                medicationsText.append(medication.getName())
                        .append(" - ")
                        .append(medication.getDosage())
                        .append("\n");
            }
        }
        
        tvUpcomingMedications.setText(medicationsText.toString());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // التعامل مع اختيارات شريط التنقل السفلي
        // هذا مجرد مثال مبسط، في التطبيق الفعلي سيتم التعامل مع العناصر الفعلية
        int itemId = item.getItemId();
        
        // تحميل الشاشة المناسبة بناءً على العنصر المحدد
        Fragment selectedFragment = null;
        
        // تحميل الشاشة المحددة
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, selectedFragment)
                    .commit();
            return true;
        }
        
        return false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // تحديث عرض الأدوية عند العودة إلى الشاشة
        displayUpcomingMedications();
    }
}
