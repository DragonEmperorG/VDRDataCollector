package cn.edu.whu.lmars.unl.entity;

import android.hardware.SensorEvent;

public class EnvironmentSensors {
    private PressureSensor pressureSensor;

    public EnvironmentSensors() {
        pressureSensor = new PressureSensor();
    }

    public PressureSensor getPressureSensor() {
        return pressureSensor;
    }

    public void setPressureSensor(PressureSensor pressureSensor) {
        this.pressureSensor = pressureSensor;
    }

    public void updatePressureSensor(SensorEvent sensorEvent) {
        pressureSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        pressureSensor.sensorEventTimestamp = sensorEvent.timestamp;
        pressureSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pressureSensor.values[0]);
        for (int i = 1; i < pressureSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(pressureSensor.values[i]);
        }
        pressureSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedPressureSensorValues() {
        return pressureSensor.csvFormattedValues;
    }
}
