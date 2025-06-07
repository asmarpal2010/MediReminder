package com.atefmatar.medireminder.ui.medication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.atefmatar.medireminder.data.model.Medication;
import com.atefmatar.medireminder.data.model.Reminder;
import com.atefmatar.medireminder.data.repository.MedicationRepository;
import com.atefmatar.medireminder.service.ReminderManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * شاشة إضافة/تعديل دواء
 */
public class AddEditMedicationActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    
    private EditText etMedicationName;
    private EditText etDosage;
    private Spinner spMedicationForm;
    private EditText etInstructions;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnAddReminder;
    private ImageView ivMedicationImage;
    private Button btnSave;
    
    private MedicationRepository medicationRepository;
    private ReminderManager reminderManager;
    private String medicationId;
    private Uri imageUri;
    private List<Reminder> reminders = new ArrayList<>();
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);
        
        // تهيئة مستودع البيانات ومدير التذكيرات
        medicationRepository = MedicationRepository.getInstance();
        reminderManager = new ReminderManager(this);
        
        // تهيئة عناصر واجهة المستخدم
        initializeViews();
        
        // إعداد المستمعين للأحداث
        setupListeners();
        
        // التحقق مما إذا كان هذا تعديل لدواء موجود
        medicationId = getIntent().getStringExtra("MEDICATION_ID");
        if (medicationId != null) {
            // تحميل بيانات الدواء للتعديل
            loadMedicationData();
        }
    }
    
    private void initializeViews() {
        // هذه مجرد عناصر وهمية للتوضيح، في التطبيق الفعلي سيتم استخدام تخطيط مخصص
        etMedicationName = new EditText(this);
        etDosage = new EditText(this);
        spMedicationForm = new Spinner(this);
        etInstructions = new EditText(this);
        etStartDate = new EditText(this);
        etEndDate = new EditText(this);
        btnAddReminder = new Button(this);
        ivMedicationImage = new ImageView(this);
        btnSave = new Button(this);
    }
    
    private void setupListeners() {
        // مستمع لحقل تاريخ البدء
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateCalendar, etStartDate);
            }
        });
        
        // مستمع لحقل تاريخ الانتهاء
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateCalendar, etEndDate);
            }
        });
        
        // مستمع لزر إضافة تذكير
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        
        // مستمع لصورة الدواء
        ivMedicationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });
        
        // مستمع لزر الحفظ
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication();
            }
        });
    }
    
    private void loadMedicationData() {
        Medication medication = medicationRepository.getMedicationById(medicationId);
        if (medication != null) {
            etMedicationName.setText(medication.getName());
            etDosage.setText(medication.getDosage());
            // تعيين نوع الدواء في القائمة المنسدلة
            etInstructions.setText(medication.getInstructions());
            
            // تعيين تاريخ البدء
            if (medication.getStartDate() != null) {
                startDateCalendar.setTime(medication.getStartDate());
                updateDateField(etStartDate, startDateCalendar);
            }
            
            // تعيين تاريخ الانتهاء
            if (medication.getEndDate() != null) {
                endDateCalendar.setTime(medication.getEndDate());
                updateDateField(etEndDate, endDateCalendar);
            }
            
            // تحميل صورة الدواء إذا كانت موجودة
            if (medication.getImageUri() != null && !medication.getImageUri().isEmpty()) {
                imageUri = Uri.parse(medication.getImageUri());
                ivMedicationImage.setImageURI(imageUri);
            }
            
            // تحميل التذكيرات
            reminders = medicationRepository.getRemindersForMedication(medicationId);
        }
    }
    
    private void showDatePickerDialog(final Calendar calendar, final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateField(editText, calendar);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    
    private void updateDateField(EditText editText, Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        editText.setText(dateFormat.format(calendar.getTime()));
    }
    
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // إنشاء تذكير جديد
                        Reminder reminder = new Reminder();
                        reminder.setHour(hourOfDay);
                        reminder.setMinute(minute);
                        
                        // تفعيل التذكير لكل يوم في الأسبوع
                        boolean[] daysOfWeek = new boolean[7];
                        for (int i = 0; i < 7; i++) {
                            daysOfWeek[i] = true;
                        }
                        reminder.setDaysOfWeek(daysOfWeek);
                        
                        // إضافة التذكير إلى القائمة
                        reminders.add(reminder);
                        
                        // عرض رسالة تأكيد
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        Toast.makeText(
                                AddEditMedicationActivity.this,
                                "تمت إضافة تذكير في الساعة " + timeFormat.format(calendar.getTime()),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
    
    private void showImageSourceDialog() {
        // في التطبيق الفعلي، سيتم عرض مربع حوار للاختيار بين الكاميرا والمعرض
        // لتبسيط المثال، سنفتح المعرض مباشرة
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // التقاط صورة من الكاميرا
                ivMedicationImage.setImageURI(imageUri);
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // اختيار صورة من المعرض
                imageUri = data.getData();
                ivMedicationImage.setImageURI(imageUri);
            }
        }
    }
    
    private void saveMedication() {
        // التحقق من صحة البيانات المدخلة
        String name = etMedicationName.getText().toString().trim();
        String dosage = etDosage.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();
        
        if (name.isEmpty()) {
            etMedicationName.setError("يرجى إدخال اسم الدواء");
            return;
        }
        
        if (dosage.isEmpty()) {
            etDosage.setError("يرجى إدخال الجرعة");
            return;
        }
        
        // إنشاء أو تحديث كائن الدواء
        Medication medication;
        if (medicationId != null) {
            medication = medicationRepository.getMedicationById(medicationId);
        } else {
            medication = new Medication();
        }
        
        medication.setName(name);
        medication.setDosage(dosage);
        medication.setForm(spMedicationForm.getSelectedItem().toString());
        medication.setInstructions(instructions);
        medication.setStartDate(startDateCalendar.getTime());
        
        if (!etEndDate.getText().toString().isEmpty()) {
            medication.setEndDate(endDateCalendar.getTime());
        }
        
        if (imageUri != null) {
            medication.setImageUri(imageUri.toString());
        }
        
        // حفظ الدواء في قاعدة البيانات
        if (medicationId != null) {
            medicationRepository.updateMedication(medication);
        } else {
            medicationId = medication.getId();
            medicationRepository.addMedication(medication);
        }
        
        // حفظ التذكيرات وجدولتها
        for (Reminder reminder : reminders) {
            reminder.setMedicationId(medicationId);
            
            if (reminder.getId() == null) {
                medicationRepository.addReminder(reminder);
            } else {
                medicationRepository.updateReminder(reminder);
            }
            
            // جدولة التذكير
            reminderManager.scheduleReminder(medication, reminder);
        }
        
        // العودة إلى الشاشة السابقة
        finish();
    }
}
