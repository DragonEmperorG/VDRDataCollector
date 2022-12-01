package cn.edu.whu.lmars.unl.entity;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import cn.edu.whu.lmars.unl.util.Conversion;

public class PositionSensors {
    private GameRotationVectorSensor gameRotationVectorSensor;
    private MagneticFieldSensor magneticFieldSensor;
    private OrientationSensor orientationSensor;

    public PositionSensors() {
        gameRotationVectorSensor = new GameRotationVectorSensor();
        magneticFieldSensor = new MagneticFieldSensor();
        orientationSensor = new OrientationSensor();
    }

    public GameRotationVectorSensor getGameRotationVectorSensor() {
        return gameRotationVectorSensor;
    }

    public void setGameRotationVectorSensor(GameRotationVectorSensor gameRotationVectorSensor) {
        this.gameRotationVectorSensor = gameRotationVectorSensor;
    }

    public void updateGameRotationVectorSensor(SensorEvent sensorEvent) {
        gameRotationVectorSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        gameRotationVectorSensor.sensorEventTimestamp = sensorEvent.timestamp;
        gameRotationVectorSensor.values = sensorEvent.values;
        gameRotationVectorSensor.eulerAngles = Conversion.rotationVectorToEulerAngleDegree(gameRotationVectorSensor.values);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gameRotationVectorSensor.values[0]);
        for (int i = 1; i < gameRotationVectorSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(gameRotationVectorSensor.values[i]);
        }
        gameRotationVectorSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedGameRotationVectorSensorValues() {
        return gameRotationVectorSensor.csvFormattedValues;
    }

    public MagneticFieldSensor getMagneticFieldSensor() {
        return magneticFieldSensor;
    }

    public void setMagneticFieldSensor(MagneticFieldSensor magneticFieldSensor) {
        this.magneticFieldSensor = magneticFieldSensor;
    }

    public void updateMagneticFieldSensor(SensorEvent sensorEvent) {
        magneticFieldSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        magneticFieldSensor.sensorEventTimestamp = sensorEvent.timestamp;
        magneticFieldSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(magneticFieldSensor.values[0]);
        for (int i = 1; i < magneticFieldSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(magneticFieldSensor.values[i]);
        }
        magneticFieldSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedMagneticFieldSensorValues() {
        return magneticFieldSensor.csvFormattedValues;
    }

    public OrientationSensor getOrientationSensor() {
        return orientationSensor;
    }

    public void setOrientationSensor(OrientationSensor orientationSensor) {
        this.orientationSensor = orientationSensor;
    }

    public void updateOrientationSensor(SensorEvent sensorEvent) {
        orientationSensor.sensorEventUpdateSystemTimestamp = System.currentTimeMillis();
        orientationSensor.sensorEventTimestamp = sensorEvent.timestamp;
        orientationSensor.values = sensorEvent.values;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(orientationSensor.values[0]);
        for (int i = 1; i < orientationSensor.valueCounts; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(orientationSensor.values[i]);
        }
        orientationSensor.csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedOrientationSensorValues() {
        return orientationSensor.csvFormattedValues;
    }
}
