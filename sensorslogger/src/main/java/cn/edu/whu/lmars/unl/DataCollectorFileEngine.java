package cn.edu.whu.lmars.unl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
