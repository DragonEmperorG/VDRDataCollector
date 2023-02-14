package cn.edu.whu.lmars.unl;


import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import cn.edu.whu.lmars.unl.entity.SensorsCollection;
import cn.edu.whu.lmars.unl.util.FileUtil;

public class DataCollectorFileEngine extends Thread {

    private static final String TAG = "DataCollectorFileEngine";

    private int dataCollectorFileEngineStatus = 0;

    private String dataCollectorFolderName;

    private ArrayBlockingQueue<SensorsCollection> logSensorsCollectionQueue = new ArrayBlockingQueue(1000);

    private FileOutputStream dataCollectorFileOutputStream = null;

    private FileOutputStream sensorGyroscopeFileOutputStream = null;

    private FileOutputStream sensorGyroscopeUncalibratedFileOutputStream = null;

    private FileOutputStream sensorAccelerometerFileOutputStream = null;

    private FileOutputStream sensorAccelerometerUncalibratedFileOutputStream = null;

    private FileOutputStream sensorMagneticFieldFileOutputStream = null;

    private FileOutputStream sensorMagneticFieldUncalibratedFileOutputStream = null;

    private FileOutputStream sensorGNSSFileOutputStream = null;

    public DataCollectorFileEngine(Activity mainActivity, String loggerFolderName) {
        dataCollectorFolderName = loggerFolderName;

        batchInitialSensorFileOutputStream(mainActivity, loggerFolderName);

        logDeviceData(mainActivity, loggerFolderName);
        logDeviceSensorsData(mainActivity, loggerFolderName);
    }

    private void batchInitialSensorFileOutputStream(Activity mainActivity, String loggerFolderName) {
        try {
            dataCollectorFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "VdrExperimentData", ".csv");
            sensorGyroscopeFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "MotionSensorGyroscope", ".csv");
            sensorGyroscopeUncalibratedFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "MotionSensorGyroscopeUncalibrated", ".csv");
            sensorAccelerometerFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "MotionSensorAccelerometer", ".csv");
            sensorAccelerometerUncalibratedFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "MotionSensorAccelerometerUncalibrated", ".csv");
            sensorMagneticFieldFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "PositionSensorMagneticField", ".csv");
            sensorMagneticFieldUncalibratedFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "PositionSensorMagneticFieldUncalibrated", ".csv");
            sensorGNSSFileOutputStream = initialSensorFileOutputStream(mainActivity, loggerFolderName, "GNSS", ".csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileOutputStream initialSensorFileOutputStream(Activity mainActivity, String loggerFolderName, String fileName, String extension) {
        File mFile = FileUtil.getFile(mainActivity, loggerFolderName, fileName, extension);
        try {
            return new FileOutputStream(mFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void logDeviceData(Activity mainActivity, String loggerFolderName) {
        File deviceDataFile = FileUtil.getFile(mainActivity, loggerFolderName, "DeviceData", ".csv");
        try {
            FileOutputStream deviceDataFileOutputStream = new FileOutputStream(deviceDataFile);
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Manufacturer");
            stringBuilder.append(", ").append("Model");
            stringBuilder.append("\n");
            stringBuilder.append(Build.MANUFACTURER);
            stringBuilder.append(", ").append(Build.MODEL);
            deviceDataFileOutputStream.write(stringBuilder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logDeviceSensorsData(Activity mainActivity, String loggerFolderName) {
        SensorManager sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        int sensorListSize = sensorList.size();

        File deviceDataFile = FileUtil.getFile(mainActivity, loggerFolderName, "DeviceSensorsData", ".csv");
        try {
            FileOutputStream deviceDataFileOutputStream = new FileOutputStream(deviceDataFile);
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder();
            stringBuilder.append("name");
            stringBuilder.append(", ").append("vendor");
            stringBuilder.append(", ").append("version");
            stringBuilder.append(", ").append("type");
            stringBuilder.append(", ").append("stringType");
            stringBuilder.append(", ").append("maxRange");
            stringBuilder.append(", ").append("resolution");
            stringBuilder.append(", ").append("power");
            stringBuilder.append(", ").append("maxDelay");
            stringBuilder.append(", ").append("minDelay");

            for (int i = 0; i < sensorListSize; i++) {
                Sensor sensor = sensorList.get(i);
                stringBuilder.append("\n");
                stringBuilder.append(sensor.getName());
                stringBuilder.append(", ").append(sensor.getVendor());
                stringBuilder.append(", ").append(sensor.getVersion());
                stringBuilder.append(", ").append(sensor.getType());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    stringBuilder.append(", ").append(sensor.getStringType());
                } else {
                    stringBuilder.append(", ").append("");
                }
                stringBuilder.append(", ").append(sensor.getMaximumRange());
                stringBuilder.append(", ").append(sensor.getResolution());
                stringBuilder.append(", ").append(sensor.getPower());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stringBuilder.append(", ").append(sensor.getMaxDelay());
                } else {
                    stringBuilder.append(", ").append("");
                }
                stringBuilder.append(", ").append(sensor.getMinDelay());

            }

            deviceDataFileOutputStream.write(stringBuilder.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLogFile() {
        this.start();
    }

    public void stopLogFile() {
        dataCollectorFileEngineStatus = 0;
        try {
            dataCollectorFileOutputStream.close();
            sensorGyroscopeFileOutputStream.close();
            sensorGyroscopeUncalibratedFileOutputStream.close();
            sensorAccelerometerFileOutputStream.close();
            sensorAccelerometerUncalibratedFileOutputStream.close();
            sensorMagneticFieldFileOutputStream.close();
            sensorMagneticFieldUncalibratedFileOutputStream.close();
            sensorGNSSFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logSensorsCollection(SensorsCollection sensorsCollection) {
        try {
            logSensorsCollectionQueue.put(sensorsCollection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void logSensorEvent(long systemCurrentTimeMillis, long systemClockElapsedRealtimeMillis, SensorEvent sensorEvent) {
        final Sensor eventSensor = sensorEvent.sensor;
        final int eventSensorType = eventSensor.getType();
        switch (eventSensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                FileUtil.writeSensorEvent(sensorAccelerometerFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            case Sensor.TYPE_ACCELEROMETER_UNCALIBRATED:
                FileUtil.writeSensorEvent(sensorAccelerometerUncalibratedFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            case Sensor.TYPE_GYROSCOPE:
                FileUtil.writeSensorEvent(sensorGyroscopeFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                FileUtil.writeSensorEvent(sensorGyroscopeUncalibratedFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                FileUtil.writeSensorEvent(sensorMagneticFieldFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                FileUtil.writeSensorEvent(sensorMagneticFieldUncalibratedFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, sensorEvent);
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void logSensorGNSS(long systemCurrentTimeMillis, long systemClockElapsedRealtimeMillis, Location location) {
        FileUtil.writeSensorGNSS(sensorGNSSFileOutputStream, systemCurrentTimeMillis, systemClockElapsedRealtimeMillis, location);
    }

    @Override
    public void run() {
        dataCollectorFileEngineStatus = 1;
        while (dataCollectorFileEngineStatus == 1) {
            if (!logSensorsCollectionQueue.isEmpty()) {
                try {
                    SensorsCollection takenSensorsCollection = logSensorsCollectionQueue.take();
                    try {
                        dataCollectorFileOutputStream.write(takenSensorsCollection.getFormattedVdrSensorsValues().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
