package cn.edu.whu.lmars.unl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssClock;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Vector;

import cn.edu.whu.lmars.unl.entity.SensorsCollection;
import cn.edu.whu.lmars.unl.listener.SensorsCollectionListener;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SensorsLoggerEngine extends Thread implements SensorEventListener, LocationListener {

    private static final String TAG = "SensorsLoggerEngine";

    public static final int SENSOR_TYPE_GNSS = 2020;
    public static final int SENSOR_TYPE_ALKAID = 2022;
    private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_FASTEST; // 1000Hz
    // private final int SENSOR_DELAY_CONFIG = SensorManager.SENSOR_DELAY_GAME; // 100Hz

    /**
     * Sensor Manager
     **/
    private int sensorsLoggerEngineStatus = 0;
    private final Activity rMainActivity;
    private SensorsLoggerEngineOption cSensorsLoggerEngineOption;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    /**
     * Alkaid Sensor
     **/
    private boolean alkaidSensorsSwitcher = false;
    private int alkaidSensorTriggerCounter = 0;

    private SensorsCollection sensorsCollection;
    private SensorsCollectionListener sensorsCollectionListener = null;

    private boolean fileLoggerSwitcher = false;
    private DataCollectorFileEngine vdrDataCollectorFileEngine;

    /**
     * USed in Clock Correction
     **/
    private boolean gnssClockReferenceBiasSetFlag = false;
    private final int OFFSET_NANOS_WINDOW_SIZE = 30;
    private long gnssClockFullBiasNanos = 0L;
    private double gnssClockBiasNanos = 1.0e-9;
    private long localGnssClockAverageOffsetNanos = 0;
    Vector<Long> localGnssClockOffsetNanosWindow = new Vector();

    private int callbackTriggerCounter = 0;

    public SensorsLoggerEngine(Activity mainActivity) {
        rMainActivity = mainActivity;
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
            locationManager.registerGnssMeasurementsCallback(gnssMeasurementsEventListener);

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
        locationManager.unregisterGnssMeasurementsCallback(gnssMeasurementsEventListener);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // https://stackoverflow.com/questions/5500765/accelerometer-sensorevent-timestamp
        long systemCurrentTimeMillis = System.currentTimeMillis();
        long systemNanoTime = System.nanoTime();
        long systemClockElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        long localGnssClockOffsetNanos = localGnssClockAverageOffsetNanos;

        sensorsCollection.updateSensorsValues(event);

        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.logSensorEvent(systemCurrentTimeMillis, systemClockElapsedRealtimeNanos, localGnssClockOffsetNanos, event);
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
        long systemClockElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        long localGnssClockOffsetNanos = localGnssClockAverageOffsetNanos;

        sensorsCollection.updateLocationValues(location);

        if (fileLoggerSwitcher) {
            vdrDataCollectorFileEngine.logSensorGnssLocation(systemCurrentTimeMillis, systemClockElapsedRealtimeNanos, localGnssClockOffsetNanos, location);
        }
    }

    // https://stackoverflow.com/questions/64638260/android-locationlistener-abstractmethoderror-on-onstatuschanged-and-onproviderd/64643361#64643361
    @Deprecated
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private final GnssMeasurementsEvent.Callback gnssMeasurementsEventListener =
            new GnssMeasurementsEvent.Callback() {
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent gnssMeasurementsEvent) {
                    long systemCurrentTimeMillis = System.currentTimeMillis();
                    long systemClockElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();

                    GnssClock gnssClock = gnssMeasurementsEvent.getClock();

                    /* maintaining constant the 'FullBiasNanos' instead of using the instantaneous value. This avoids the 256 ns
                     jumps each 3 seconds that create a code-phase
                     divergence due to the clock. */
                    if (!gnssClockReferenceBiasSetFlag) {
                        // https://developer.android.google.cn/reference/android/location/GnssClock#getFullBiasNanos()
                        gnssClockFullBiasNanos = gnssClock.getFullBiasNanos();
                        gnssClockBiasNanos = gnssClock.getBiasNanos();
                        gnssClockReferenceBiasSetFlag = true;
                    }

                    // https://home.csis.u-tokyo.ac.jp/~dinesh/GNSS_Raw_files/GNSS%20102%20Measurements%20from%20Phones%20Short%20Course%20Slides.pdf
                    double gnssClockLocalEstimateGpsTimeNanos = gnssClock.getTimeNanos() - (gnssClockFullBiasNanos + gnssClockBiasNanos);
                    long localEstimateGpsTimeNanos = (long) gnssClockLocalEstimateGpsTimeNanos;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (gnssClock.hasElapsedRealtimeNanos()) {
                            systemClockElapsedRealtimeNanos = gnssClock.getElapsedRealtimeNanos();
                        }
                    }

                    long localGnssClockOffsetNanos = localEstimateGpsTimeNanos - systemClockElapsedRealtimeNanos;
                    localGnssClockOffsetNanosWindow.add(localGnssClockOffsetNanos);

                    int statisticsCounter = 0;
                    long statisticsMovingAverageOffsetNanosSummer = 0;
                    if (localGnssClockOffsetNanosWindow.size() >= OFFSET_NANOS_WINDOW_SIZE) {
                        for (int i = localGnssClockOffsetNanosWindow.size() - OFFSET_NANOS_WINDOW_SIZE; i < localGnssClockOffsetNanosWindow.size(); i++) {
                            statisticsMovingAverageOffsetNanosSummer = statisticsMovingAverageOffsetNanosSummer + (localGnssClockOffsetNanosWindow.get(i) - localGnssClockOffsetNanosWindow.get(localGnssClockOffsetNanosWindow.size() - OFFSET_NANOS_WINDOW_SIZE));
                            statisticsCounter = statisticsCounter + 1;
                        }
                        localGnssClockAverageOffsetNanos = statisticsMovingAverageOffsetNanosSummer / statisticsCounter + localGnssClockOffsetNanosWindow.get(localGnssClockOffsetNanosWindow.size() - OFFSET_NANOS_WINDOW_SIZE);
                        localGnssClockOffsetNanosWindow.remove(0);
                    } else {
                        for (int i = 0; i < localGnssClockOffsetNanosWindow.size(); i++) {
                            statisticsMovingAverageOffsetNanosSummer = statisticsMovingAverageOffsetNanosSummer + (localGnssClockOffsetNanosWindow.get(i) - localGnssClockOffsetNanosWindow.get(0));
                            statisticsCounter = statisticsCounter + 1;
                        }
                        localGnssClockAverageOffsetNanos = statisticsMovingAverageOffsetNanosSummer / statisticsCounter + localGnssClockOffsetNanosWindow.get(0);
                    }

                    if (fileLoggerSwitcher) {
                        vdrDataCollectorFileEngine.logSensorGnssMeasurement(systemCurrentTimeMillis, systemClockElapsedRealtimeNanos, localGnssClockAverageOffsetNanos, gnssMeasurementsEvent);
                    }
                }

                @Override
                public void onStatusChanged(int status) {

                }
            };

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
