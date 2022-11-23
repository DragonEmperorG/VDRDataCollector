package cn.edu.whu.lmars.unl.entity;

import android.hardware.SensorEvent;

public class MotionSensors {
    private AccelerometerSensor accelerometerSensor;
    private AccelerometerUncalibratedSensor accelerometerUncalibratedSensor;
    private GravitySensor gravitySensor;
    private GyroscopeSensor gyroscopeSensor;
    private GyroscopeUncalibratedSensor gyroscopeUncalibratedSensor;
    private LinearAccelerationSensor linearAccelerationSensor;
    private RotationVectorSensor rotationVectorSensor;

    public MotionSensors() {
        accelerometerSensor = new AccelerometerSensor();
        accelerometerUncalibratedSensor = new AccelerometerUncalibratedSensor();
        gravitySensor = new GravitySensor();
        gyroscopeSensor = new GyroscopeSensor();
        gyroscopeUncalibratedSensor = new GyroscopeUncalibratedSensor();
        linearAccelerationSensor = new LinearAccelerationSensor();
        rotationVectorSensor = new RotationVectorSensor();
    }

    public AccelerometerSensor getAccelerometerSensor() {
        return accelerometerSensor;
    }

    public void setAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.accelerometerSensor = accelerometerSensor;
    }

    public void updateAccelerometerSensor(SensorEvent sensorEvent) {
        accelerometerSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        accelerometerSensor.sensorEventTimestamp = sensorEvent.timestamp;
        accelerometerSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(accelerometerSensor.values[0]);
        for (int i = 1; i < accelerometerSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(accelerometerSensor.values[i]);
        }
        accelerometerSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedAccelerometerSensorValues() {
        return accelerometerSensor.csvFormattedValues;
    }

    public AccelerometerUncalibratedSensor getAccelerometerUncalibratedSensor() {
        return accelerometerUncalibratedSensor;
    }

    public void setAccelerometerUncalibratedSensor(AccelerometerUncalibratedSensor accelerometerUncalibratedSensor) {
        this.accelerometerUncalibratedSensor = accelerometerUncalibratedSensor;
    }

    public void updateAccelerometerUncalibratedSensor(SensorEvent sensorEvent) {
        accelerometerUncalibratedSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        accelerometerUncalibratedSensor.sensorEventTimestamp = sensorEvent.timestamp;
        accelerometerUncalibratedSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(accelerometerUncalibratedSensor.values[0]);
        for (int i = 1; i < accelerometerUncalibratedSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(accelerometerUncalibratedSensor.values[i]);
        }
        accelerometerUncalibratedSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedAccelerometerUncalibratedSensorValues() {
        return accelerometerUncalibratedSensor.csvFormattedValues;
    }

    public GravitySensor getGravitySensor() {
        return gravitySensor;
    }

    public void setGravitySensor(GravitySensor gravitySensor) {
        this.gravitySensor = gravitySensor;
    }

    public void updateGravitySensor(SensorEvent sensorEvent) {
        gravitySensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        gravitySensor.sensorEventTimestamp = sensorEvent.timestamp;
        gravitySensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gravitySensor.values[0]);
        for (int i = 1; i < gravitySensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(gravitySensor.values[i]);
        }
        gravitySensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedGravitySensorValues() {
        return gravitySensor.csvFormattedValues;
    }

    public GyroscopeSensor getGyroscopeSensor() {
        return gyroscopeSensor;
    }

    public void setGyroscopeSensor(GyroscopeSensor gyroscopeSensor) {
        this.gyroscopeSensor = gyroscopeSensor;
    }

    public void updateGyroscopeSensor(SensorEvent sensorEvent) {
        gyroscopeSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        gyroscopeSensor.sensorEventTimestamp = sensorEvent.timestamp;
        gyroscopeSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gyroscopeSensor.values[0]);
        for (int i = 1; i < gyroscopeSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(gyroscopeSensor.values[i]);
        }
        gyroscopeSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedGyroscopeSensorValues() {
        return gyroscopeSensor.csvFormattedValues;
    }

    public GyroscopeUncalibratedSensor getGyroscopeUncalibratedSensor() {
        return gyroscopeUncalibratedSensor;
    }

    public void setGyroscopeUncalibratedSensor(GyroscopeUncalibratedSensor gyroscopeUncalibratedSensor) {
        this.gyroscopeUncalibratedSensor = gyroscopeUncalibratedSensor;
    }

    public void updateGyroscopeUncalibratedSensor(SensorEvent sensorEvent) {
        gyroscopeUncalibratedSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        gyroscopeUncalibratedSensor.sensorEventTimestamp = sensorEvent.timestamp;
        gyroscopeUncalibratedSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gyroscopeUncalibratedSensor.values[0]);
        for (int i = 1; i < gyroscopeUncalibratedSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(gyroscopeUncalibratedSensor.values[i]);
        }
        gyroscopeUncalibratedSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedGyroscopeUncalibratedSensorValues() {
        return gyroscopeUncalibratedSensor.csvFormattedValues;
    }

    public LinearAccelerationSensor getLinearAccelerationSensor() {
        return linearAccelerationSensor;
    }

    public void setLinearAccelerationSensor(LinearAccelerationSensor linearAccelerationSensor) {
        this.linearAccelerationSensor = linearAccelerationSensor;
    }

    public void updateLinearAccelerationSensor(SensorEvent sensorEvent) {
        linearAccelerationSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        linearAccelerationSensor.sensorEventTimestamp = sensorEvent.timestamp;
        linearAccelerationSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(linearAccelerationSensor.values[0]);
        for (int i = 1; i < linearAccelerationSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(linearAccelerationSensor.values[i]);
        }
        linearAccelerationSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedLinearAccelerationSensorValues() {
        return linearAccelerationSensor.csvFormattedValues;
    }

    public RotationVectorSensor getRotationVectorSensor() {
        return rotationVectorSensor;
    }

    public void setRotationVectorSensor(RotationVectorSensor rotationVectorSensor) {
        this.rotationVectorSensor = rotationVectorSensor;
    }

    public void updateRotationVectorSensor(SensorEvent sensorEvent) {
        rotationVectorSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        rotationVectorSensor.sensorEventTimestamp = sensorEvent.timestamp;
        rotationVectorSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rotationVectorSensor.values[0]);
        for (int i = 1; i < rotationVectorSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(rotationVectorSensor.values[i]);
        }
        rotationVectorSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedRotationVectorSensorValues() {
        return rotationVectorSensor.csvFormattedValues;
    }
}
