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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cn.edu.whu.lmars.unl.entity.SensorsCollection;


public class SensorsLoggerEngine extends Thread implements SensorEventListener, LocationListener {

    private static final String TAG = "SensorsLoggerEngine";

    public static final int SENSOR_TYPE_GNSS = 2020;

    private static final float NS2S = 1.0f / 1000000000.0f;

    private final Activity rMainActivity;

    private final SensorsLoggerEngineOption cSensorsLoggerEngineOption;
    
    private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_FASTEST; // 1000Hz
//    private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_GAME; // 100Hz

    private final SensorManager sensorManager;

    private final LocationManager locationManager;

    private boolean fileLoggerSwitcher = false;

    private int sensorsLoggerEngineStatus = 0;

    private SensorsCollection sensorsCollection;

    private DataCollectorFileEngine vdrDataCollectorFileEngine;

    public SensorsLoggerEngine(Activity mainActivity, SensorsLoggerEngineOption sensorsLoggerEngineOption) {
        rMainActivity= mainActivity;
        cSensorsLoggerEngineOption = sensorsLoggerEngineOption;
        fileLoggerSwitcher = cSensorsLoggerEngineOption.getFileLoggerSwitcher();

        sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        sensorsCollection = new SensorsCollection();
    }

    public void startLog() {
        ArrayList<Integer> logSensorsTypeList = cSensorsLoggerEngineOption.getLogSensorsTypeList();
        int logSensorsTypeListCounts = logSensorsTypeList.size();
        for (int logSensorsTypeListCounter = 0; logSensorsTypeListCounter < logSensorsTypeListCounts; logSensorsTypeListCounter++) {
            int registerLogSensorsType = logSensorsTypeList.get(logSensorsTypeListCounter);
            if (registerLogSensorsType == SENSOR_TYPE_GNSS) {
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                Sensor registerSensor = sensorManager.getDefaultSensor(registerLogSensorsType);
                sensorManager.registerListener(this, registerSensor, SENSOR_DELAY_CONFIG);
            }
        }

        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine = new DataCollectorFileEngine(rMainActivity, cSensorsLoggerEngineOption);
            vdrDataCollectorFileEngine.startLogFile();
        }

        this.start();
    }

    private File getLoggerFile(String loggerFolderPath, String loggerFileName, String extension) {

        File loggerFileDirectory = new File(loggerFolderPath);
        if (!loggerFileDirectory.exists()) {
            boolean directoryCreationStatus = loggerFileDirectory.mkdirs();
            Log.i(TAG, "directoryCreationStatus: " + directoryCreationStatus);
        }

        File recordFile = new File(loggerFileDirectory, loggerFileName + extension);

        if (recordFile.exists()) {
            boolean deletionStatus = recordFile.delete();
            Log.i(TAG, "File already exists, delete it first, deletionStatus: " + deletionStatus);
        }

        if (!recordFile.exists()) {
            try {
                boolean deletionStatus = recordFile.createNewFile();
                Log.i(TAG, "fileCreationStatus: " + deletionStatus);
            } catch (Exception e) {
                Log.i(TAG, "createNewFile failed: " + e.toString());
            }
        }

        return recordFile;
    }

    public void stopLog() {
        sensorsLoggerEngineStatus = 0;
        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.stopLogFile();
        }
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorsCollection.updateSensorsValues(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        sensorsCollection.updateLocationValues(location);
    }

    @Override
    public void run() {
        sensorsLoggerEngineStatus = 1;

        sensorsCollection.updateAlkaidValues();

        while (sensorsLoggerEngineStatus == 1) {
            try {
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (fileLoggerSwitcher) {
                vdrDataCollectorFileEngine.logSensorsCollection(sensorsCollection);
            }



//            Log.d(TAG, "loop update: " + System.currentTimeMillis());
        }
    }
}
