package cn.edu.whu.lmars.unl;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

public class SensorHelper {

    private final Activity activity;

    private SensorManager sensorManager;

    public SensorHelper(Activity mainActivity) {
        activity = mainActivity;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    }

    public boolean hasSensor(int sensorType) {
        List<Sensor> sensorList = sensorManager.getSensorList(sensorType);
        return sensorList.size() != 0;
    }

    public String getSensorText(int sensorType) {
        List<Sensor> sensorList = sensorManager.getSensorList(sensorType);
        Sensor sensor = sensorList.get(0);
        String sensorName = sensor.getName();
        String sensorVendor = sensor.getVendor();
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        stringBuilder.append(sensorName);
        stringBuilder.append(" (");
        stringBuilder.append(sensorVendor);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
