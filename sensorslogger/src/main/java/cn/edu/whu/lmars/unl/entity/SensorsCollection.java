package cn.edu.whu.lmars.unl.entity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;

public class SensorsCollection {
    private static final String TAG = "SensorsCollection";
    private MotionSensors motionSensors;
    private PositionSensors positionSensors;
    private EnvironmentSensors environmentSensors;
    private GnssSensor gnssSensor;
    private AlkaidSensor alkaidSensor;

    public SensorsCollection() {
        motionSensors = new MotionSensors();
        positionSensors = new PositionSensors();
        environmentSensors = new EnvironmentSensors();
        gnssSensor = new GnssSensor();
        alkaidSensor = new AlkaidSensor();
    }

    public MotionSensors getMotionSensors() {
        return motionSensors;
    }

    public void setMotionSensors(MotionSensors motionSensors) {
        this.motionSensors = motionSensors;
    }

    public PositionSensors getPositionSensors() {
        return positionSensors;
    }

    public void setPositionSensors(PositionSensors positionSensors) {
        this.positionSensors = positionSensors;
    }

    public EnvironmentSensors getEnvironmentSensors() {
        return environmentSensors;
    }

    public void setEnvironmentSensors(EnvironmentSensors environmentSensors) {
        this.environmentSensors = environmentSensors;
    }

    public GnssSensor getGnssSensor() {
        return gnssSensor;
    }

    public void setGnssSensor(GnssSensor gnssSensor) {
        this.gnssSensor = gnssSensor;
    }

    public AlkaidSensor getAlkaidSensor() {
        return alkaidSensor;
    }

    public void setAlkaidSensor(AlkaidSensor alkaidSensor) {
        this.alkaidSensor = alkaidSensor;
    }

    public void updateSensorsValues(SensorEvent sensorEvent) {
        final Sensor eventSensor = sensorEvent.sensor;
        final int eventSensorType = eventSensor.getType();
        switch (eventSensorType) {
            case Sensor.TYPE_ACCELEROMETER:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_ACCELEROMETER: " + sensorEvent.sensor.getName());
                motionSensors.updateAccelerometerSensor(sensorEvent);
                break;
            case Sensor.TYPE_ACCELEROMETER_UNCALIBRATED:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_ACCELEROMETER_UNCALIBRATED: " + sensorEvent.sensor.getName());
                // motionSensors.updateAccelerometerUncalibratedSensor(sensorEvent);
                motionSensors.updateAccelerometerUncalibratedSensor(sensorEvent, positionSensors.getGameRotationVectorSensor());
                break;
            case Sensor.TYPE_GRAVITY:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_GRAVITY: " + sensorEvent.sensor.getName());
                motionSensors.updateGravitySensor(sensorEvent);
                break;
            case Sensor.TYPE_GYROSCOPE:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_GYROSCOPE: " + sensorEvent.sensor.getName());
                motionSensors.updateGyroscopeSensor(sensorEvent);
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_GYROSCOPE_UNCALIBRATED: " + sensorEvent.sensor.getName());
                motionSensors.updateGyroscopeUncalibratedSensor(sensorEvent);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_LINEAR_ACCELERATION: " + sensorEvent.sensor.getName());
                motionSensors.updateLinearAccelerationSensor(sensorEvent);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_ROTATION_VECTOR: " + sensorEvent.sensor.getName());
                motionSensors.updateRotationVectorSensor(sensorEvent);
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_GAME_ROTATION_VECTOR: " + sensorEvent.sensor.getName());
                positionSensors.updateGameRotationVectorSensor(sensorEvent);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_MAGNETIC_FIELD: " + sensorEvent.sensor.getName());
                positionSensors.updateMagneticFieldSensor(sensorEvent);
                break;
            case Sensor.TYPE_ORIENTATION:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_ORIENTATION: " + sensorEvent.sensor.getName());
                positionSensors.updateOrientationSensor(sensorEvent);
                break;
            case Sensor.TYPE_PRESSURE:
//                Log.d(TAG, "updateSensorsValues: Sensor.TYPE_PRESSURE: " + sensorEvent.sensor.getName());
                environmentSensors.updatePressureSensor(sensorEvent);
                break;
            default:
                break;
        }
    }

    public void updateLocationValues(Location location) {
        gnssSensor.updateGNSSSensor(location);
    }

    public void updateAlkaidValues() {
        try {
            alkaidSensor.updateAlkaidSensor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configAlkaidValues(String host, int port) {
        alkaidSensor.configAlkaidSensor(host, port);
    }

    public String getFormattedVdrSensorsValues() {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedAccelerometerSensorValues());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedGyroscopeSensorValues());
        stringBuilder.append(", ").append(positionSensors.getCsvFormattedGameRotationVectorSensorValues());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedRotationVectorSensorValues());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedGyroscopeUncalibratedSensorValues());
        stringBuilder.append(", ").append(positionSensors.getCsvFormattedOrientationSensorValues());
        stringBuilder.append(", ").append(positionSensors.getCsvFormattedMagneticFieldSensorValues());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedGravitySensorValues());
        stringBuilder.append(", ").append(motionSensors.getCsvFormattedLinearAccelerationSensorValues());
        stringBuilder.append(", ").append(environmentSensors.getCsvFormattedPressureSensorValues());
        stringBuilder.append(", ").append(gnssSensor.getCsvFormattedGNSSSensorValues());
        stringBuilder.append(", ").append(alkaidSensor.getCsvFormattedAlkaidSensorValues());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
