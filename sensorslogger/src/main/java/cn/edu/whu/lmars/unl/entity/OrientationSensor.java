package cn.edu.whu.lmars.unl.entity;

public class OrientationSensor {
    public int valueCounts = 3;
    public long sensorEventUpdateSystemTimestamp = 0L;
    public long sensorEventTimestamp = 0L;
    public float[] values = new float[valueCounts];
    public String csvFormattedValues = "0.0, 0.0, 0.0";

    public OrientationSensor() {
    }
}
