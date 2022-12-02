package cn.edu.whu.lmars.unl.entity;

public class GameRotationVectorSensor {
    public int valueCounts = 4;
    public long sensorEventUpdateSystemTimestamp = 0L;
    public long sensorEventTimestamp = 0L;
    public float[] values = new float[valueCounts];
    public float[] quaternion = new float[4];
    public float[] rotationMatrix = new float[9];
    public float[] eulerAngles = new float[3];
    public String csvFormattedValues = "0.0, 0.0, 0.0, 0.0";

    public GameRotationVectorSensor() {
    }
}
