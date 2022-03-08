package cn.edu.whu.lmars.unl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Drawable ENABLE_MATERIAL_BUTTON_BACKGROUND;
    private Drawable DISABLE_MATERIAL_BUTTON_BACKGROUND;

    private MaterialButton collectorModuleButtonStartCollect;
    private MaterialButton collectorModuleButtonStopCollect;

    private SensorsLoggerEngine sensorsLoggerEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isRecordPermissionGranted()) {
            requestRecordPermission();
            return;
        }

        initView();
    }

    private boolean isRecordPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                0);
    }

    private String getDefaultTimeStamp4Folder() {
        SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        Date mDate = new Date();
        return customDateFormat.format(mDate);
    }

    private void initView() {
        collectorModuleButtonStartCollect = findViewById(R.id.collector_module_button_start_collect);
        ENABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonStartCollect.getBackground();
        collectorModuleButtonStartCollect.setOnClickListener(view -> {
            SensorsLoggerEngineOption sensorsLoggerEngineOption = new SensorsLoggerEngineOption();
            ArrayList<Integer> logSensorsTypeList = new ArrayList<>();
            logSensorsTypeList.add(SensorsLoggerEngine.SENSOR_TYPE_GNSS);
            logSensorsTypeList.add(Sensor.TYPE_ACCELEROMETER);
            logSensorsTypeList.add(Sensor.TYPE_GRAVITY);
            logSensorsTypeList.add(Sensor.TYPE_GYROSCOPE);
            logSensorsTypeList.add(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
            logSensorsTypeList.add(Sensor.TYPE_LINEAR_ACCELERATION);
            logSensorsTypeList.add(Sensor.TYPE_ROTATION_VECTOR);
            logSensorsTypeList.add(Sensor.TYPE_GAME_ROTATION_VECTOR);
            logSensorsTypeList.add(Sensor.TYPE_MAGNETIC_FIELD);
            logSensorsTypeList.add(Sensor.TYPE_ORIENTATION);
            logSensorsTypeList.add(Sensor.TYPE_PRESSURE);
            sensorsLoggerEngineOption.setLogSensorsTypeList(logSensorsTypeList);
            sensorsLoggerEngineOption.setFileLoggerSwitcher(true);
            sensorsLoggerEngineOption.setLoggerFolderName(getDefaultTimeStamp4Folder());
            sensorsLoggerEngine = new SensorsLoggerEngine(MainActivity.this, sensorsLoggerEngineOption);
            sensorsLoggerEngine.startLog();
            collectorModuleButtonStartCollect.setEnabled(false);
            collectorModuleButtonStartCollect.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
            collectorModuleButtonStopCollect.setEnabled(true);
            collectorModuleButtonStopCollect.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
        });

        collectorModuleButtonStopCollect = findViewById(R.id.collector_module_button_stop_collect);
        DISABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonStopCollect.getBackground();
        collectorModuleButtonStopCollect.setOnClickListener(view -> {
            sensorsLoggerEngine.stopLog();
            collectorModuleButtonStopCollect.setEnabled(false);
            collectorModuleButtonStopCollect.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
            collectorModuleButtonStartCollect.setEnabled(true);
            collectorModuleButtonStartCollect.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
        });

    }


}