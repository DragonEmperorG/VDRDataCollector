package cn.edu.whu.lmars.unl.entity;

import android.hardware.SensorEvent;
import android.location.Location;

public class GNSSSensor {
    private long sensorEventUpdateSystemTimestamp = 0L;
    private long sensorEventTimestamp = 0L;
    private Location gnssSensorLocation = new Location("");
    private String csvFormattedValues = "0.0, 0.0, 0.0";

    public GNSSSensor() {
    }

    public void updateGNSSSensor(Location location) {
        sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        sensorEventTimestamp = location.getTime();
        gnssSensorLocation = new Location(location);
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        stringBuilder.append(sensorEventTimestamp);
        stringBuilder.append(", ").append(gnssSensorLocation.getLongitude());
        stringBuilder.append(", ").append(gnssSensorLocation.getLatitude());
        stringBuilder.append(", ").append(gnssSensorLocation.getAccuracy());
        csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedGNSSSensorValues() {
        return csvFormattedValues;
    }
}
