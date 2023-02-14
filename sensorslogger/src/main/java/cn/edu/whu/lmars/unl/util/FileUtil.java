package cn.edu.whu.lmars.unl.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static String getNextSequenceFormatName(Context context) {
        int nextSequenceCode = 1;
        String dayFolderName = TimeUtil.getDayFolderName();

        File directory = new File(context.getExternalFilesDir(null), "VdrLMARS" + "/" + dayFolderName);
        if (!directory.exists()) {
            boolean directoryCreationStatus = directory.mkdirs();
            Log.i(TAG, "directoryCreationStatus: " + directoryCreationStatus);
            return String.format(Locale.CHINA,"%04d", nextSequenceCode);
        }

        File[] subDirectories = directory.listFiles();
        if (subDirectories != null) {
            for (int i = 0; i < subDirectories.length; i++) {
                File subDirectory = subDirectories[i];
                if (subDirectory.isDirectory()) {
                    String subDirectoryName = subDirectory.getName();
                    int subDirectoryCode = Integer.parseInt(subDirectoryName);
                    if (subDirectoryCode > nextSequenceCode) {
                        nextSequenceCode = subDirectoryCode;
                    }
                }
            }
        }

        nextSequenceCode = nextSequenceCode + 1;
        return String.format(Locale.CHINA,"%04d", nextSequenceCode);
    }

    public static File getFile(Context context, String folderName, String fileName, String extension) {

        String dayFolderName = TimeUtil.getDayFolderName();

        File directory = new File(context.getExternalFilesDir(null), "VdrLMARS" + "/" + dayFolderName + "/" + folderName + "/" + "raw");
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

    public static void writeSensorEvent(FileOutputStream fileOutputStream, long systemCurrentTimeMillis, long systemClockElapsedRealtimeMillis, SensorEvent sensorEvent) {
        long sensorEventTimestamp = sensorEvent.timestamp;
        float[] sensorEventValues = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(systemCurrentTimeMillis);
        stringBuilder.append(", ").append(systemClockElapsedRealtimeMillis);
        stringBuilder.append(", ").append(sensorEventTimestamp);
        for (int i = 0; i < sensorEventValues.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(sensorEventValues[i]);
        }
        stringBuilder.append("\n");
        try {
            fileOutputStream.write(stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeSensorGNSS(FileOutputStream fileOutputStream, long systemCurrentTimeMillis, long systemClockElapsedRealtimeMillis, Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(systemCurrentTimeMillis);
        stringBuilder.append(", ").append(systemClockElapsedRealtimeMillis);
        stringBuilder.append(", ").append(location.getTime());
        stringBuilder.append(", ").append(location.getElapsedRealtimeNanos());
        stringBuilder.append(", ").append(location.getLongitude());
        stringBuilder.append(", ").append(location.getLatitude());
        stringBuilder.append(", ").append(location.getAltitude());
        stringBuilder.append(", ").append(location.getAccuracy());
        stringBuilder.append(", ").append(location.getVerticalAccuracyMeters());
        stringBuilder.append(", ").append(location.getSpeed());
        stringBuilder.append(", ").append(location.getSpeedAccuracyMetersPerSecond());
        stringBuilder.append(", ").append(location.getBearing());
        stringBuilder.append(", ").append(location.getBearingAccuracyDegrees());
        stringBuilder.append(", ").append(location.getProvider());
        stringBuilder.append("\n");
        try {
            fileOutputStream.write(stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
