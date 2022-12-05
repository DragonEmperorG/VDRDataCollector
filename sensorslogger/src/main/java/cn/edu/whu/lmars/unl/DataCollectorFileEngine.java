package cn.edu.whu.lmars.unl;


import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import cn.edu.whu.lmars.unl.entity.SensorsCollection;

public class DataCollectorFileEngine extends Thread {

    private static final String TAG = "DataCollectorFileEngine";

    private int dataCollectorFileEngineStatus = 0;

    private String dataCollectorFolderName;

    private ArrayBlockingQueue<SensorsCollection> logSensorsCollectionQueue = new ArrayBlockingQueue(1000);

    private FileOutputStream dataCollectorFileOutputStream = null;

    public DataCollectorFileEngine(Activity mainActivity, String loggerFolderName) {
        dataCollectorFolderName = loggerFolderName;
        File dataCollectorFile = getFile(mainActivity, "VdrExperimentData", ".csv");
        try {
            dataCollectorFileOutputStream = new FileOutputStream(dataCollectorFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logDeviceData(mainActivity);
        logDeviceSensorsData(mainActivity);
    }

    private void logDeviceData(Activity mainActivity) {
        File deviceDataFile = getFile(mainActivity, "DeviceData", ".csv");
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

    private void logDeviceSensorsData(Activity mainActivity) {
        SensorManager sensorManager = (SensorManager) mainActivity.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        int sensorListSize = sensorList.size();

        File deviceDataFile = getFile(mainActivity, "DeviceSensorsData", ".csv");
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

    private File getFile(Context context, String fileName, String extension) {

        File directory = new File(context.getExternalFilesDir(null), "VdrLMARS" + "/" + dataCollectorFolderName);
        if (!directory.exists()) {
            boolean directoryCreationStatus = directory.mkdirs();
            Log.i(TAG, "directoryCreationStatus: " + directoryCreationStatus);
        }

        File file = new File(directory, fileName + extension);

        if (file.exists()) {
            boolean deletionStatus = file.delete();
            Log.i(TAG, "File already exists, delete it first, deletionStatus: " + deletionStatus);
        }

        if (!file.exists()) {
            try {
                boolean deletionStatus = file.createNewFile();
                Log.i(TAG, "fileCreationStatus: " + deletionStatus);
            } catch (Exception e) {
                Log.i(TAG, "createNewFile failed: " + e.toString());
            }
        }

        return file;
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
