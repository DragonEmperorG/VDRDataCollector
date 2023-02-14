package cn.edu.whu.lmars.unl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.edu.whu.lmars.unl.entity.SensorsCollection;
import cn.edu.whu.lmars.unl.listener.SensorsCollectionListener;

public class SensorsLoggerEngine extends Thread implements SensorEventListener, LocationListener {

    private static final String TAG = "SensorsLoggerEngine";

    public static final int SENSOR_TYPE_GNSS = 2020;

    public static final int SENSOR_TYPE_ALKAID = 2022;

    private final Activity rMainActivity;

    private SensorsLoggerEngineOption cSensorsLoggerEngineOption;
    
    private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_FASTEST; // 1000Hz
//    private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_GAME; // 100Hz

    private SensorManager sensorManager;

    private LocationManager locationManager;

    private boolean fileLoggerSwitcher = false;

    private boolean alkaidSensorsSwitcher = false;

    private int sensorsLoggerEngineStatus = 0;

    private SensorsCollection sensorsCollection;

    private SensorsCollectionListener sensorsCollectionListener = null;

    private DataCollectorFileEngine vdrDataCollectorFileEngine;

    private int alkaidSensorTriggerCounter = 0;

    private int callbackTriggerCounter = 0;

    public SensorsLoggerEngine(Activity mainActivity) {
        rMainActivity= mainActivity;
        sensorsCollection = new SensorsCollection();
    }

    public void registerSensorsCollectionListener(SensorsCollectionListener iSensorsCollectionListener) {
        sensorsCollectionListener = iSensorsCollectionListener;
    }

    public void unregisterSensorsCollectionListener() {
        sensorsCollectionListener = null;
    }

    public void openSensors(SensorsLoggerEngineOption sensorsLoggerEngineOption) {

        cSensorsLoggerEngineOption = sensorsLoggerEngineOption;
        ArrayList<Integer> logSensorsTypeList = cSensorsLoggerEngineOption.getLogSensorsTypeList();

        if (logSensorsTypeList.contains(SENSOR_TYPE_GNSS)) {
            if (ActivityCompat.checkSelfPermission(rMainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rMainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager = (LocationManager) rMainActivity.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER, 0, 0, this);
            logSensorsTypeList.remove(Integer.valueOf(SENSOR_TYPE_GNSS));
        }

        if (logSensorsTypeList.contains(SENSOR_TYPE_ALKAID)) {
            alkaidSensorsSwitcher = true;
            sensorsCollection.configAlkaidValues(cSensorsLoggerEngineOption.getAlkaidSensorHost(), cSensorsLoggerEngineOption.getAlkaidSensorPort());
            logSensorsTypeList.remove(Integer.valueOf(SENSOR_TYPE_ALKAID));
        }

        sensorManager = (SensorManager) rMainActivity.getSystemService(Context.SENSOR_SERVICE);
        int logSensorsTypeListCounts = logSensorsTypeList.size();
        for (int logSensorsTypeListCounter = 0; logSensorsTypeListCounter < logSensorsTypeListCounts; logSensorsTypeListCounter++) {
            int registerLogSensorsType = logSensorsTypeList.get(logSensorsTypeListCounter);
            Sensor registerSensor = sensorManager.getDefaultSensor(registerLogSensorsType);
            sensorManager.registerListener(this, registerSensor, SENSOR_DELAY_CONFIG);
        }

        this.start();
    }

    public void closeSensors() {
        sensorsLoggerEngineStatus = 0;
        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.stopLogFile();
        }
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    public void startLogger(String loggerFolderName) {
        if (!fileLoggerSwitcher) {
            vdrDataCollectorFileEngine = new DataCollectorFileEngine(rMainActivity, loggerFolderName);
            vdrDataCollectorFileEngine.startLogFile();
            fileLoggerSwitcher = true;
        }
    }

    public void stopLogger() {
        if (fileLoggerSwitcher) {
            fileLoggerSwitcher = false;
            vdrDataCollectorFileEngine.stopLogFile();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onSensorChanged(SensorEvent event) {
        // https://stackoverflow.com/questions/5500765/accelerometer-sensorevent-timestamp
        long systemCurrentTimeMillis = System.currentTimeMillis();
        long systemClockElapsedRealtimeMillis = SystemClock.elapsedRealtimeNanos();

        sensorsCollection.updateSensorsValues(event);

        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.logSensorEvent(systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
//        StringBuilder stringBuilder;
//        stringBuilder = new StringBuilder();
//        stringBuilder.append(location.getTime());
//        stringBuilder.append(", ").append(location.getLongitude());
//        stringBuilder.append(", ").append(location.getLatitude());
//        Log.d(TAG, "onLocationChanged: " + stringBuilder);
        long systemCurrentTimeMillis = System.currentTimeMillis();
        long systemClockElapsedRealtimeMillis = SystemClock.elapsedRealtimeNanos();

        sensorsCollection.updateLocationValues(location);

        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.logSensorGNSS(systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, location);
        }
    }

    @Override
    public void run() {
        sensorsLoggerEngineStatus = 1;
        while (sensorsLoggerEngineStatus == 1) {
            try {
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (fileLoggerSwitcher) {
                vdrDataCollectorFileEngine.logSensorsCollection(sensorsCollection);
            }

            if (alkaidSensorTriggerCounter >= 200) {

                if (alkaidSensorsSwitcher) {
                    sensorsCollection.updateAlkaidValues();
                }

                alkaidSensorTriggerCounter = 0;
            }
            alkaidSensorTriggerCounter++;

            if (callbackTriggerCounter >= 20) {

                if (sensorsCollectionListener != null) {
                    sensorsCollectionListener.onSensorsCollectionUpdated(sensorsCollection);
                }

                callbackTriggerCounter = 0;
            }
            callbackTriggerCounter++;

//            Log.d(TAG, "loop update: " + System.currentTimeMillis());
        }
    }
}
