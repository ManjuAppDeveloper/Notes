package com.example.notes;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_SCHEDULE_ALARM = 100;
    private static final int ALARM_INTERVAL_MINUTES = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request the SCHEDULE_EXACT_ALARM permission at runtime (for API level 30 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_CODE_SCHEDULE_ALARM);
            } else {
                // Permission already granted, proceed with setting exact alarms
                setupExactAlarms();
            }
        } else {
            // For devices below API level 30, no need to request the permission.
            // Proceed with setting exact alarms directly.
            setupExactAlarms();
        }

        // Dismiss button click listener (optional if you want to provide a dismiss button)
        Button dismissButton = findViewById(R.id.dismiss_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the notification or stop the alarm (if applicable)
                // Add your code to dismiss the notification or stop the alarm here
            }
        });
    }

    private void setupExactAlarms() {
        try {
            // Set up the alarm to trigger the broadcast receiver every ALARM_INTERVAL_MINUTES
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, DrinkWaterReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Calculate the trigger time for the first alarm
            long intervalMillis = ALARM_INTERVAL_MINUTES * 60 * 1000; // Convert minutes to milliseconds
            long triggerTimeMillis = System.currentTimeMillis() + intervalMillis;
            // Set the exact alarm (considering different SDK versions)
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
                }
            }
        } catch (Exception e) {
            // Catch any exceptions and log them for debugging purposes
            Log.e(TAG, "Error setting up exact alarms: " + e.getMessage());
        }
    }

    // Handle the permission request result (if applicable)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SCHEDULE_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with setting exact alarms
                setupExactAlarms();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable the feature)
            }
        }
    }
}
