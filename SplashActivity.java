package com.atefmatar.medireminder.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.atefmatar.medireminder.ui.main.MainActivity;
import com.atefmatar.medireminder.ui.onboarding.OnboardingActivity;
import com.atefmatar.medireminder.util.PreferenceManager;

/**
 * شاشة البداية
 */
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 ثوانٍ
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // إعداد واجهة المستخدم
        setContentView(android.R.layout.simple_list_item_1);
        TextView textView = findViewById(android.R.id.text1);
        textView.setText("MediReminder\nبواسطة Atef Ahmed Matar");
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(24);
        
        preferenceManager = new PreferenceManager(this);
        
        // تأخير لعرض شاشة البداية
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNextScreen, SPLASH_DELAY);
    }
    
    private void navigateToNextScreen() {
        // التحقق مما إذا كان المستخدم يستخدم التطبيق لأول مرة
        if (preferenceManager.isFirstLaunch()) {
            // إذا كانت أول مرة، انتقل إلى شاشة الترحيب
            startActivity(new Intent(this, OnboardingActivity.class));
        } else {
            // إذا لم تكن أول مرة، انتقل مباشرة إلى الشاشة الرئيسية
            startActivity(new Intent(this, MainActivity.class));
        }
        
        // إنهاء النشاط الحالي
        finish();
    }
}
