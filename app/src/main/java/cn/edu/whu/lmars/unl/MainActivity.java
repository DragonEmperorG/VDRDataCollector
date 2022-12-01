package cn.edu.whu.lmars.unl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.edu.whu.lmars.unl.entity.AccelerometerSensor;
import cn.edu.whu.lmars.unl.entity.AccelerometerUncalibratedSensor;
import cn.edu.whu.lmars.unl.entity.AlkaidSensor;
import cn.edu.whu.lmars.unl.entity.AlkaidSensorPositionProto;
import cn.edu.whu.lmars.unl.entity.EnvironmentSensors;
import cn.edu.whu.lmars.unl.entity.GameRotationVectorSensor;
import cn.edu.whu.lmars.unl.entity.GnssSensor;
import cn.edu.whu.lmars.unl.entity.GyroscopeSensor;
import cn.edu.whu.lmars.unl.entity.GyroscopeUncalibratedSensor;
import cn.edu.whu.lmars.unl.entity.MagneticFieldSensor;
import cn.edu.whu.lmars.unl.entity.MotionSensors;
import cn.edu.whu.lmars.unl.entity.PositionSensors;
import cn.edu.whu.lmars.unl.entity.PressureSensor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Drawable ENABLE_MATERIAL_BUTTON_BACKGROUND;
    private Drawable DISABLE_MATERIAL_BUTTON_BACKGROUND;

    private MaterialButton collectorModuleButtonOpenSensors;
    private MaterialButton collectorModuleButtonCloseSensors;
    private MaterialButton collectorModuleButtonStartCollect;
    private MaterialButton collectorModuleButtonStopCollect;

    private String logFolderName = "";
    private TextInputLayout collectorModuleTextInputLayoutLogFolderName;
    private EditText collectorModuleLogFolderNameEditText;


    private boolean collectorModuleMotionSensorsCardCheckedValue;
    private boolean collectorModuleMotionSensorsCardCheckableValue;
    private MaterialCardView collectorModuleMotionSensorsCardView;

    private MaterialTextView accelerometerSensorTimestampTextView;
    private MaterialTextView accelerometerSensorAccelerationXTextView;
    private MaterialTextView accelerometerSensorAccelerationYTextView;
    private MaterialTextView accelerometerSensorAccelerationZTextView;

    private MaterialTextView accelerometerUncalibratedSensorTimestampTextView;
    private MaterialTextView accelerometerUncalibratedSensorAccelerationXTextView;
    private MaterialTextView accelerometerUncalibratedSensorAccelerationYTextView;
    private MaterialTextView accelerometerUncalibratedSensorAccelerationZTextView;

    private MaterialTextView gyroscopeSensorTimestampTextView;
    private MaterialTextView gyroscopeSensorAccelerationXTextView;
    private MaterialTextView gyroscopeSensorAccelerationYTextView;
    private MaterialTextView gyroscopeSensorAccelerationZTextView;

    private MaterialTextView gyroscopeUncalibratedSensorTimestampTextView;
    private MaterialTextView gyroscopeUncalibratedSensorAccelerationXTextView;
    private MaterialTextView gyroscopeUncalibratedSensorAccelerationYTextView;
    private MaterialTextView gyroscopeUncalibratedSensorAccelerationZTextView;


    private boolean collectorModulePositionSensorsCardCheckedValue;
    private boolean collectorModulePositionSensorsCardCheckableValue;
    private MaterialCardView collectorModulePositionSensorsCardView;

    private MaterialTextView gameRotationVectorSensorTimestampTextView;
    private MaterialTextView gameRotationVectorSensorRotationVectorXTextView;
    private MaterialTextView gameRotationVectorSensorRotationVectorYTextView;
    private MaterialTextView gameRotationVectorSensorRotationVectorZTextView;
    private MaterialTextView gameRotationVectorSensorRotationVectorWTextView;
    private MaterialTextView gameRotationVectorSensorQuaternionXTextView;
    private MaterialTextView gameRotationVectorSensorQuaternionYTextView;
    private MaterialTextView gameRotationVectorSensorQuaternionZTextView;
    private MaterialTextView gameRotationVectorSensorQuaternionWTextView;
    private MaterialTextView gameRotationVectorSensorEulerAngleAzimuthTextView;
    private MaterialTextView gameRotationVectorSensorEulerAnglePitchTextView;
    private MaterialTextView gameRotationVectorSensorEulerAngleRollTextView;

    private MaterialTextView magneticSensorTimestampTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthXTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthYTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthZTextView;

    private boolean collectorModuleEnvironmentSensorsCardCheckedValue;
    private boolean collectorModuleEnvironmentSensorsCardCheckableValue;
    private MaterialCardView collectorModuleEnvironmentSensorsCardView;
    private MaterialTextView pressureSensorNameTextView;
    private MaterialTextView pressureSensorTimestampTextView;
    private MaterialTextView pressureSensorGeomagneticFieldStrengthXTextView;

    private boolean collectorModuleGnssSensorsCardCheckedValue;
    private boolean collectorModuleGnssSensorsCardCheckableValue;
    private MaterialCardView collectorModuleGnssSensorsCardView;
    private MaterialTextView gnssSensorTimeTextView;
    private MaterialTextView gnssSensorLongitudeTextView;
    private MaterialTextView gnssSensorLatitudeTextView;
    private MaterialTextView gnssSensorAccuracyTextView;


    private boolean collectorModuleAlkaidSensorsCardCheckedValue;
    private boolean collectorModuleAlkaidSensorsCardCheckableValue;
    private MaterialCardView collectorModuleAlkaidSensorsCardView;
    private MaterialTextView alkaidSensorStatusTextView;
    private MaterialTextView alkaidSensorTimeTextView;
    private MaterialTextView alkaidSensorLongitudeTextView;
    private MaterialTextView alkaidSensorLatitudeTextView;
    private MaterialTextView alkaidSensorHeightTextView;
    private MaterialTextView alkaidSensorAzimuthTextView;
    private MaterialTextView alkaidSensorSpeedTextView;
    private MaterialTextView alkaidSensorAgeTextView;
    private MaterialTextView alkaidSensorSatsnumTextView;
    private String alkaidSensorHostSetting;
    private int alkaidSensorPortSetting;
    private TextInputLayout collectorModuleTextInputLayoutAlkaidSensorHostSetting;
    private AutoCompleteTextView collectorModuleAlkaidSensorHostSettingTextView;
    private TextInputLayout collectorModuleTextInputLayoutAlkaidSensorPortSetting;
    private AutoCompleteTextView collectorModuleAlkaidSensorPortSettingTextView;
    MaterialButton collectorModuleButtonTestAlkaidSensor;

    private SensorHelper sensorHelper;
    private SensorsLoggerEngine sensorsLoggerEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isRecordPermissionGranted()) {
            requestRecordPermission();
            return;
        }

        initHelper();
        initView();

    }

    private boolean isRecordPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                0);
    }

    private String getDefaultTimeStamp4Folder() {
        SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        Date mDate = new Date();
        return customDateFormat.format(mDate);
    }

    private void enableCollectorModuleButtonOpenSensors() {
        collectorModuleButtonOpenSensors.setEnabled(true);
        collectorModuleButtonOpenSensors.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void disableCollectorModuleButtonOpenSensors() {
        collectorModuleButtonOpenSensors.setEnabled(false);
        collectorModuleButtonOpenSensors.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void enableCollectorModuleButtonCloseSensors() {
        collectorModuleButtonCloseSensors.setEnabled(true);
        collectorModuleButtonCloseSensors.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void disableCollectorModuleButtonCloseSensors() {
        collectorModuleButtonCloseSensors.setEnabled(false);
        collectorModuleButtonCloseSensors.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void enableCollectorModuleButtonStartCollect() {
        collectorModuleButtonStartCollect.setEnabled(true);
        collectorModuleButtonStartCollect.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void disableCollectorModuleButtonStartCollect() {
        collectorModuleButtonStartCollect.setEnabled(false);
        collectorModuleButtonStartCollect.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void enableCollectorModuleButtonStopCollect() {
        collectorModuleButtonStopCollect.setEnabled(true);
        collectorModuleButtonStopCollect.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void disableCollectorModuleButtonStopCollect() {
        collectorModuleButtonStopCollect.setEnabled(false);
        collectorModuleButtonStopCollect.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void enableCollectorModuleButtonTestAlkaidSensor() {
        collectorModuleButtonTestAlkaidSensor.setEnabled(true);
        collectorModuleButtonTestAlkaidSensor.setBackground(ENABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    private void disableCollectorModuleButtonTestAlkaidSensor() {
        collectorModuleButtonTestAlkaidSensor.setEnabled(false);
        collectorModuleButtonTestAlkaidSensor.setBackground(DISABLE_MATERIAL_BUTTON_BACKGROUND);
    }

    void initMotionSensorsCard() {
        collectorModuleMotionSensorsCardView = findViewById(R.id.collector_module_motion_sensors_card);
        collectorModuleMotionSensorsCardCheckedValue = true;
        if (sensorHelper.hasSensor(Sensor.TYPE_ACCELEROMETER)) {
            collectorModuleMotionSensorsCardView.setChecked(true);
            collectorModuleMotionSensorsCardView.setCheckable(false);
            collectorModuleMotionSensorsCardView.setOnLongClickListener(view -> {
                if (collectorModuleMotionSensorsCardView.isCheckable()) {
                    collectorModuleMotionSensorsCardCheckedValue = !collectorModuleMotionSensorsCardView.isChecked();
                    collectorModuleMotionSensorsCardView.setChecked(collectorModuleMotionSensorsCardCheckedValue);
                }
                return true;
            });
        } else {
            collectorModuleMotionSensorsCardView.setChecked(false);
            collectorModuleMotionSensorsCardView.setCheckable(false);
            LinearLayout linearLayout = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_info_layout);
            linearLayout.setVisibility(View.GONE);
        }

        MaterialTextView accelerometerSensorNameTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_name_value);
        accelerometerSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_ACCELEROMETER));
        accelerometerSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_timestamp_value);
        accelerometerSensorAccelerationXTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_x_value);
        accelerometerSensorAccelerationYTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_y_value);
        accelerometerSensorAccelerationZTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_z_value);

        MaterialTextView accelerometerUncalibratedSensorNameTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_uncalibrated_sensor_name_value);
        accelerometerUncalibratedSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED));
        accelerometerUncalibratedSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_uncalibrated_sensor_timestamp_value);
        accelerometerUncalibratedSensorAccelerationXTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_uncalibrated_sensor_acceleration_x_value);
        accelerometerUncalibratedSensorAccelerationYTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_uncalibrated_sensor_acceleration_y_value);
        accelerometerUncalibratedSensorAccelerationZTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_uncalibrated_sensor_acceleration_z_value);

        MaterialTextView gyroscopeSensorNameTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_sensor_name_value);
        gyroscopeSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_GYROSCOPE));
        gyroscopeSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_sensor_timestamp_value);
        gyroscopeSensorAccelerationXTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_sensor_acceleration_x_value);
        gyroscopeSensorAccelerationYTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_sensor_acceleration_y_value);
        gyroscopeSensorAccelerationZTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_sensor_acceleration_z_value);

        MaterialTextView gyroscopeUncalibratedSensorNameTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_uncalibrated_sensor_name_value);
        gyroscopeUncalibratedSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_GYROSCOPE_UNCALIBRATED));
        gyroscopeUncalibratedSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_uncalibrated_sensor_timestamp_value);
        gyroscopeUncalibratedSensorAccelerationXTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_uncalibrated_sensor_acceleration_x_value);
        gyroscopeUncalibratedSensorAccelerationYTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_uncalibrated_sensor_acceleration_y_value);
        gyroscopeUncalibratedSensorAccelerationZTextView = findViewById(R.id.collector_module_motion_sensors_card_gyroscope_uncalibrated_sensor_acceleration_z_value);

    }

    void updateAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        runOnUiThread(() -> {
            accelerometerSensorTimestampTextView.setText(String.valueOf(accelerometerSensor.sensorEventTimestamp));
            accelerometerSensorAccelerationXTextView.setText(String.valueOf(accelerometerSensor.values[0]));
            accelerometerSensorAccelerationYTextView.setText(String.valueOf(accelerometerSensor.values[1]));
            accelerometerSensorAccelerationZTextView.setText(String.valueOf(accelerometerSensor.values[2]));
        });
    }

    void updateAccelerometerUncalibratedSensor(AccelerometerUncalibratedSensor accelerometerUncalibratedSensor) {
        runOnUiThread(() -> {
            accelerometerUncalibratedSensorTimestampTextView.setText(String.valueOf(accelerometerUncalibratedSensor.sensorEventTimestamp));
            accelerometerUncalibratedSensorAccelerationXTextView.setText(String.valueOf(accelerometerUncalibratedSensor.values[0]));
            accelerometerUncalibratedSensorAccelerationYTextView.setText(String.valueOf(accelerometerUncalibratedSensor.values[1]));
            accelerometerUncalibratedSensorAccelerationZTextView.setText(String.valueOf(accelerometerUncalibratedSensor.values[2]));
        });
    }

    void updateGyroscopeSensor(GyroscopeSensor gyroscopeSensor) {
        runOnUiThread(() -> {
            gyroscopeSensorTimestampTextView.setText(String.valueOf(gyroscopeSensor.sensorEventTimestamp));
            gyroscopeSensorAccelerationXTextView.setText(String.valueOf(gyroscopeSensor.values[0]));
            gyroscopeSensorAccelerationYTextView.setText(String.valueOf(gyroscopeSensor.values[1]));
            gyroscopeSensorAccelerationZTextView.setText(String.valueOf(gyroscopeSensor.values[2]));
        });
    }

    void updateGyroscopeUncalibratedSensor(GyroscopeUncalibratedSensor gyroscopeUncalibratedSensor) {
        runOnUiThread(() -> {
            gyroscopeUncalibratedSensorTimestampTextView.setText(String.valueOf(gyroscopeUncalibratedSensor.sensorEventTimestamp));
            gyroscopeUncalibratedSensorAccelerationXTextView.setText(String.valueOf(gyroscopeUncalibratedSensor.values[0]));
            gyroscopeUncalibratedSensorAccelerationYTextView.setText(String.valueOf(gyroscopeUncalibratedSensor.values[1]));
            gyroscopeUncalibratedSensorAccelerationZTextView.setText(String.valueOf(gyroscopeUncalibratedSensor.values[2]));
        });
    }

    void initPositionSensorsCard() {
        collectorModulePositionSensorsCardView = findViewById(R.id.collector_module_position_sensors_card);
        collectorModulePositionSensorsCardCheckedValue = true;
        collectorModulePositionSensorsCardView.setChecked(true);
        collectorModulePositionSensorsCardView.setCheckable(false);
        collectorModulePositionSensorsCardView.setOnLongClickListener(view -> {
            if (collectorModulePositionSensorsCardView.isCheckable()) {
                collectorModulePositionSensorsCardCheckedValue = !collectorModulePositionSensorsCardView.isChecked();
                collectorModuleMotionSensorsCardView.setChecked(collectorModulePositionSensorsCardCheckedValue);
            }
            return true;
        });

        MaterialTextView gameRotationVectorSensorNameTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_name_value);
        gameRotationVectorSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_GAME_ROTATION_VECTOR));
        gameRotationVectorSensorTimestampTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_timestamp_value);
        gameRotationVectorSensorRotationVectorXTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_rotation_vector_x_value);
        gameRotationVectorSensorRotationVectorYTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_rotation_vector_y_value);
        gameRotationVectorSensorRotationVectorZTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_rotation_vector_z_value);
        gameRotationVectorSensorRotationVectorWTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_rotation_vector_w_value);
        gameRotationVectorSensorQuaternionXTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_quaternion_x_value);
        gameRotationVectorSensorQuaternionYTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_quaternion_y_value);
        gameRotationVectorSensorQuaternionZTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_quaternion_z_value);
        gameRotationVectorSensorQuaternionWTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_quaternion_w_value);
        gameRotationVectorSensorEulerAngleAzimuthTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_euler_angle_azimuth_value);
        gameRotationVectorSensorEulerAnglePitchTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_euler_angle_pitch_value);
        gameRotationVectorSensorEulerAngleRollTextView = findViewById(R.id.collector_module_position_sensors_card_game_rotation_vector_sensor_euler_angle_roll_value);

        MaterialTextView magneticSensorNameTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_name_value);
        magneticSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_MAGNETIC_FIELD));
        magneticSensorTimestampTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_timestamp_value);
        magneticSensorGeomagneticFieldStrengthXTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_x_value);
        magneticSensorGeomagneticFieldStrengthYTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_y_value);
        magneticSensorGeomagneticFieldStrengthZTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_z_value);
    }

    void updateGameRotationVectorSensor(GameRotationVectorSensor gameRotationVectorSensor) {
        runOnUiThread(() -> {
            gameRotationVectorSensorTimestampTextView.setText(String.valueOf(gameRotationVectorSensor.sensorEventTimestamp));
            gameRotationVectorSensorRotationVectorXTextView.setText(String.valueOf(gameRotationVectorSensor.values[0]));
            gameRotationVectorSensorRotationVectorYTextView.setText(String.valueOf(gameRotationVectorSensor.values[1]));
            gameRotationVectorSensorRotationVectorZTextView.setText(String.valueOf(gameRotationVectorSensor.values[2]));
            gameRotationVectorSensorRotationVectorWTextView.setText(String.valueOf(gameRotationVectorSensor.values[3]));
//            gameRotationVectorSensorQuaternionXTextView.setText(String.valueOf(gameRotationVectorSensor.quaternions[1]));
//            gameRotationVectorSensorQuaternionYTextView.setText(String.valueOf(gameRotationVectorSensor.quaternions[2]));
//            gameRotationVectorSensorQuaternionZTextView.setText(String.valueOf(gameRotationVectorSensor.quaternions[3]));
//            gameRotationVectorSensorQuaternionWTextView.setText(String.valueOf(gameRotationVectorSensor.quaternions[0]));
            gameRotationVectorSensorEulerAngleAzimuthTextView.setText(String.valueOf(gameRotationVectorSensor.eulerAngles[0]));
            gameRotationVectorSensorEulerAnglePitchTextView.setText(String.valueOf(gameRotationVectorSensor.eulerAngles[1]));
            gameRotationVectorSensorEulerAngleRollTextView.setText(String.valueOf(gameRotationVectorSensor.eulerAngles[2]));
        });
    }

    void updateMagneticSensor(MagneticFieldSensor magneticFieldSensor) {
        runOnUiThread(() -> {
            magneticSensorTimestampTextView.setText(String.valueOf(magneticFieldSensor.sensorEventTimestamp));
            magneticSensorGeomagneticFieldStrengthXTextView.setText(String.valueOf(magneticFieldSensor.values[0]));
            magneticSensorGeomagneticFieldStrengthYTextView.setText(String.valueOf(magneticFieldSensor.values[1]));
            magneticSensorGeomagneticFieldStrengthZTextView.setText(String.valueOf(magneticFieldSensor.values[2]));
        });
    }

    void initEnvironmentSensorsCard() {
        collectorModuleEnvironmentSensorsCardView = findViewById(R.id.collector_module_environment_sensors_card);
        collectorModuleEnvironmentSensorsCardCheckedValue = true;
        if (sensorHelper.hasSensor(Sensor.TYPE_PRESSURE)) {
            collectorModuleEnvironmentSensorsCardView.setChecked(true);
            collectorModuleEnvironmentSensorsCardView.setCheckable(false);
            collectorModuleEnvironmentSensorsCardView.setOnLongClickListener(view -> {
                if (collectorModuleEnvironmentSensorsCardView.isCheckable()) {
                    collectorModuleEnvironmentSensorsCardCheckedValue = !collectorModuleEnvironmentSensorsCardView.isChecked();
                    collectorModuleMotionSensorsCardView.setChecked(collectorModuleEnvironmentSensorsCardCheckedValue);
                }
                return true;
            });

            pressureSensorNameTextView = findViewById(R.id.collector_module_environment_sensors_card_pressure_sensor_name_value);
            pressureSensorNameTextView.setText(sensorHelper.getSensorText(Sensor.TYPE_PRESSURE));
            pressureSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_pressure_sensor_timestamp_value);
            pressureSensorGeomagneticFieldStrengthXTextView = findViewById(R.id.collector_module_environment_sensors_card_pressure_sensor_pressure_value);
        } else {
            collectorModuleEnvironmentSensorsCardView.setChecked(false);
            collectorModuleEnvironmentSensorsCardView.setCheckable(false);
            LinearLayout linearLayout = findViewById(R.id.collector_module_environment_sensors_card_pressure_sensor_info_layout);
            linearLayout.setVisibility(View.GONE);
        }


    }

    void updatePressureSensor(PressureSensor pressureSensor) {
        runOnUiThread(() -> {
            pressureSensorTimestampTextView.setText(String.valueOf(pressureSensor.sensorEventTimestamp));
            pressureSensorGeomagneticFieldStrengthXTextView.setText(String.valueOf(pressureSensor.values[0]));
        });
    }

    void initGnssSensorCard() {
        collectorModuleGnssSensorsCardView = findViewById(R.id.collector_module_gnss_sensors_card);
        collectorModuleGnssSensorsCardCheckedValue = true;
        collectorModuleGnssSensorsCardView.setChecked(true);
        collectorModuleGnssSensorsCardView.setCheckable(false);
        collectorModuleGnssSensorsCardView.setOnLongClickListener(view -> {
            if (collectorModuleGnssSensorsCardView.isCheckable()) {
                collectorModuleGnssSensorsCardCheckedValue = !collectorModuleGnssSensorsCardView.isChecked();
                collectorModuleGnssSensorsCardView.setChecked(collectorModuleGnssSensorsCardCheckedValue);
            }
            return true;
        });

        gnssSensorTimeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_time_value);
        gnssSensorLongitudeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_longitude_value);
        gnssSensorLatitudeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_latitude_value);
        gnssSensorAccuracyTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_accuracy_value);
    }

    void updateGnssSensor(GnssSensor gnssSensor) {
        runOnUiThread(() -> {
            Location location = gnssSensor.getGnssSensorLocation();
            gnssSensorTimeTextView.setText(String.valueOf(location.getTime()));
            gnssSensorLongitudeTextView.setText(String.valueOf(location.getLongitude()));
            gnssSensorLatitudeTextView.setText(String.valueOf(location.getLatitude()));
            gnssSensorAccuracyTextView.setText(String.valueOf(location.getAccuracy()));
        });
    }

    void initAlkaidSensorCard() {
        collectorModuleAlkaidSensorsCardView = findViewById(R.id.collector_module_alkaid_sensors_card);
        collectorModuleAlkaidSensorsCardCheckedValue = false;
        collectorModuleAlkaidSensorsCardView.setChecked(false);
        collectorModuleAlkaidSensorsCardView.setCheckable(false);
        collectorModuleAlkaidSensorsCardView.setOnLongClickListener(view -> {
            if (collectorModuleAlkaidSensorsCardView.isCheckable()) {
                collectorModuleAlkaidSensorsCardCheckedValue = !collectorModuleAlkaidSensorsCardView.isChecked();
                collectorModuleAlkaidSensorsCardView.setChecked(collectorModuleAlkaidSensorsCardCheckedValue);
            }
            return true;
        });


        alkaidSensorStatusTextView = findViewById(R.id.collector_module_alkaid_sensor_card_status_value);
        alkaidSensorTimeTextView = findViewById(R.id.collector_module_alkaid_sensor_card_timems_value);
        alkaidSensorLongitudeTextView = findViewById(R.id.collector_module_alkaid_sensor_card_longitude_value);
        alkaidSensorLatitudeTextView = findViewById(R.id.collector_module_alkaid_sensor_card_latitude_value);
        alkaidSensorHeightTextView = findViewById(R.id.collector_module_alkaid_sensor_card_height_value);
        alkaidSensorAzimuthTextView = findViewById(R.id.collector_module_alkaid_sensor_card_azimuth_value);
        alkaidSensorSpeedTextView = findViewById(R.id.collector_module_alkaid_sensor_card_speed_value);
        alkaidSensorAgeTextView = findViewById(R.id.collector_module_alkaid_sensor_card_age_value);
        alkaidSensorSatsnumTextView = findViewById(R.id.collector_module_alkaid_sensor_card_satsnum_value);

        collectorModuleTextInputLayoutAlkaidSensorHostSetting = findViewById(R.id.collector_module_text_field_alkaid_sensor_host_setting);
        collectorModuleAlkaidSensorHostSettingTextView = (AutoCompleteTextView) collectorModuleTextInputLayoutAlkaidSensorHostSetting.getEditText();
        if (collectorModuleAlkaidSensorHostSettingTextView != null) {
            String alkaidSensorHostSetting1 = "139.196.6.110";
            String alkaidSensorHostSetting2 = "192.168.2.20";
            ArrayList<String> alkaidSensorHostSettingList = new ArrayList<>();
            alkaidSensorHostSettingList.add(alkaidSensorHostSetting1);
            alkaidSensorHostSettingList.add(alkaidSensorHostSetting2);
            ArrayAdapter<String> alkaidSensorHostSettingAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.alkaid_sensor_host_setting_list_item, alkaidSensorHostSettingList);
            collectorModuleAlkaidSensorHostSettingTextView.setAdapter(alkaidSensorHostSettingAdapter);
            alkaidSensorHostSetting = alkaidSensorHostSetting1;
            collectorModuleAlkaidSensorHostSettingTextView.setText(alkaidSensorHostSetting);
            collectorModuleAlkaidSensorHostSettingTextView.setOnEditorActionListener(
                    (v, actionId, event) -> {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (!checkTextInputIsNull(collectorModuleTextInputLayoutAlkaidSensorHostSetting, /* showError= */ true)) {
                                alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingTextView.getText());
                            }
                            return true;
                        }
                        return false;
                    });
        }


        collectorModuleTextInputLayoutAlkaidSensorPortSetting = findViewById(R.id.collector_module_text_field_alkaid_sensor_port_setting);
        collectorModuleAlkaidSensorPortSettingTextView = (AutoCompleteTextView) collectorModuleTextInputLayoutAlkaidSensorPortSetting.getEditText();
        if (collectorModuleAlkaidSensorPortSettingTextView != null) {
            Integer alkaidSensorPortSetting1 = 34300;
            Integer alkaidSensorPortSetting2 = 4300;
            ArrayList<Integer> alkaidSensorPortSettingList = new ArrayList<>();
            alkaidSensorPortSettingList.add(alkaidSensorPortSetting1);
            alkaidSensorPortSettingList.add(alkaidSensorPortSetting2);
            ArrayAdapter<Integer> alkaidSensorHostSettingAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.alkaid_sensor_port_setting_list_item, alkaidSensorPortSettingList);
            collectorModuleAlkaidSensorPortSettingTextView.setAdapter(alkaidSensorHostSettingAdapter);
            alkaidSensorPortSetting = alkaidSensorPortSetting1;
            collectorModuleAlkaidSensorPortSettingTextView.setText(String.valueOf(alkaidSensorPortSetting));
            collectorModuleAlkaidSensorPortSettingTextView.setOnEditorActionListener(
                    (v, actionId, event) -> {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (!checkTextInputIsNull(collectorModuleTextInputLayoutAlkaidSensorPortSetting, /* showError= */ true)) {
                                alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingTextView.getText()));
                            }
                            return true;
                        }
                        return false;
                    });
        }

        collectorModuleButtonTestAlkaidSensor = findViewById(R.id.collector_module_button_test_alkaid_sensor);
        collectorModuleButtonTestAlkaidSensor.setOnClickListener(view -> {
            alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingTextView.getText());
            alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingTextView.getText()));

            Handler handler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 2022) {
                        AlkaidSensorPositionProto testAlkaidSensorPositionProto = (AlkaidSensorPositionProto) msg.obj;
                        if (testAlkaidSensorPositionProto.getStatus() != -1) {
                            alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingTextView.getText());
                            alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingTextView.getText()));
                            collectorModuleAlkaidSensorsCardView.setCheckable(true);
                            updateAlkaidSensor(testAlkaidSensorPositionProto);
                        } else {
                            collectorModuleTextInputLayoutAlkaidSensorHostSetting.setError(getResources().getString(R.string.collector_module_alkaid_sensor_host_setting_text_field_error_text));
                            collectorModuleAlkaidSensorsCardView.setChecked(false);
                            collectorModuleAlkaidSensorsCardView.setCheckable(false);
                        }
                    }
                }
            };
            AlkaidSensor.testAlkaidSensor(handler, alkaidSensorHostSetting, alkaidSensorPortSetting);
        });
    }

    void updateAlkaidSensor(AlkaidSensorPositionProto alkaidSensorPositionProto) {
        runOnUiThread(() -> {
            alkaidSensorStatusTextView.setText(String.valueOf(alkaidSensorPositionProto.getStatus()));
            alkaidSensorTimeTextView.setText(String.valueOf(alkaidSensorPositionProto.getTimems()));
            alkaidSensorLongitudeTextView.setText(String.valueOf(alkaidSensorPositionProto.getLongitude()));
            alkaidSensorLatitudeTextView.setText(String.valueOf(alkaidSensorPositionProto.getLatitude()));
            alkaidSensorHeightTextView.setText(String.valueOf(alkaidSensorPositionProto.getHeight()));
            alkaidSensorAzimuthTextView.setText(String.valueOf(alkaidSensorPositionProto.getAzimuth()));
            alkaidSensorSpeedTextView.setText(String.valueOf(alkaidSensorPositionProto.getSpeed()));
            alkaidSensorAgeTextView.setText(String.valueOf(alkaidSensorPositionProto.getAge()));
            alkaidSensorSatsnumTextView.setText(String.valueOf(alkaidSensorPositionProto.getSatsnum()));
        });
    }

    void lockSensorsCardCheckableStatue() {
        collectorModuleMotionSensorsCardCheckableValue = collectorModuleMotionSensorsCardView.isCheckable();
        collectorModuleMotionSensorsCardView.setCheckable(false);
        collectorModulePositionSensorsCardCheckableValue = collectorModulePositionSensorsCardView.isCheckable();
        collectorModulePositionSensorsCardView.setCheckable(false);
        collectorModuleEnvironmentSensorsCardCheckableValue= collectorModuleEnvironmentSensorsCardView.isCheckable();
        collectorModuleEnvironmentSensorsCardView.setCheckable(false);
        collectorModuleGnssSensorsCardCheckableValue = collectorModuleGnssSensorsCardView.isCheckable();
        collectorModuleGnssSensorsCardView.setCheckable(false);
        collectorModuleAlkaidSensorsCardCheckableValue = collectorModuleAlkaidSensorsCardView.isCheckable();
        collectorModuleAlkaidSensorsCardView.setCheckable(false);
    }

    void unlockSensorsCardCheckableStatue() {
        collectorModuleMotionSensorsCardView.setCheckable(collectorModuleMotionSensorsCardCheckableValue);
        collectorModulePositionSensorsCardView.setCheckable(collectorModulePositionSensorsCardCheckableValue);
        collectorModuleEnvironmentSensorsCardView.setCheckable(collectorModuleEnvironmentSensorsCardCheckableValue);
        collectorModuleGnssSensorsCardView.setCheckable(collectorModuleGnssSensorsCardCheckableValue);
        collectorModuleAlkaidSensorsCardView.setCheckable(collectorModuleAlkaidSensorsCardCheckableValue);
    }

    private ArrayList<Integer> getSelectedLogSensorsTypeList() {
        ArrayList<Integer> selectedLogSensorsTypeList = new ArrayList<>();

        if (collectorModuleMotionSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_ACCELEROMETER);
            selectedLogSensorsTypeList.add(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GRAVITY);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GYROSCOPE);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
            selectedLogSensorsTypeList.add(Sensor.TYPE_LINEAR_ACCELERATION);
            selectedLogSensorsTypeList.add(Sensor.TYPE_ROTATION_VECTOR);
        }

        if (collectorModulePositionSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_GAME_ROTATION_VECTOR);
            selectedLogSensorsTypeList.add(Sensor.TYPE_MAGNETIC_FIELD);
            selectedLogSensorsTypeList.add(Sensor.TYPE_ORIENTATION);
        }

        if (collectorModuleEnvironmentSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_PRESSURE);
        }

        if (collectorModuleGnssSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(SensorsLoggerEngine.SENSOR_TYPE_GNSS);
        }

        if (collectorModuleAlkaidSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(SensorsLoggerEngine.SENSOR_TYPE_ALKAID);
        }

        return selectedLogSensorsTypeList;
    }

    private boolean checkTextInputIsNull(TextInputLayout textInputLayout, boolean showError) {
        if (textInputLayout.getEditText().getText() == null
                || textInputLayout.getEditText().length() == 0) {
            if (showError) {
                textInputLayout.setError(
                        getResources().getString(R.string.cat_textfield_null_input_error_text));
            }
            return true;
        }
        textInputLayout.setError(null);
        return false;
    }

    private void initView() {

        collectorModuleButtonOpenSensors = findViewById(R.id.collector_module_button_open_sensors);
        collectorModuleButtonOpenSensors.setOnClickListener(view -> {
            SensorsLoggerEngineOption sensorsLoggerEngineOption = new SensorsLoggerEngineOption();
            ArrayList<Integer> logSensorsTypeList = getSelectedLogSensorsTypeList();
//            logSensorsTypeList.add(2022);
            sensorsLoggerEngineOption.setLogSensorsTypeList(logSensorsTypeList);
            if (logSensorsTypeList.contains(SensorsLoggerEngine.SENSOR_TYPE_ALKAID)) {
                sensorsLoggerEngineOption.setAlkaidSensorHost(alkaidSensorHostSetting);
                sensorsLoggerEngineOption.setAlkaidSensorPort(alkaidSensorPortSetting);
            }
            sensorsLoggerEngine = new SensorsLoggerEngine(MainActivity.this);
            sensorsLoggerEngine.registerSensorsCollectionListener(sensorsCollection -> {
                if (collectorModuleMotionSensorsCardCheckedValue) {
                    MotionSensors motionSensors = sensorsCollection.getMotionSensors();

                    AccelerometerSensor accelerometerSensor = motionSensors.getAccelerometerSensor();
                    updateAccelerometerSensor(accelerometerSensor);

                    AccelerometerUncalibratedSensor accelerometerUncalibratedSensor = motionSensors.getAccelerometerUncalibratedSensor();
                    updateAccelerometerUncalibratedSensor(accelerometerUncalibratedSensor);

                    GyroscopeSensor gyroscope = motionSensors.getGyroscopeSensor();
                    updateGyroscopeSensor(gyroscope);

                    GyroscopeUncalibratedSensor gyroscopeUncalibratedSensor = motionSensors.getGyroscopeUncalibratedSensor();
                    updateGyroscopeUncalibratedSensor(gyroscopeUncalibratedSensor);
                }

                if (collectorModulePositionSensorsCardCheckedValue) {
                    PositionSensors positionSensors = sensorsCollection.getPositionSensors();
                    MagneticFieldSensor magneticFieldSensor = positionSensors.getMagneticFieldSensor();
                    updateMagneticSensor(magneticFieldSensor);

                    GameRotationVectorSensor gameRotationVectorSensor = positionSensors.getGameRotationVectorSensor();
                    updateGameRotationVectorSensor(gameRotationVectorSensor);
                }
                if (collectorModuleEnvironmentSensorsCardCheckedValue) {
                    EnvironmentSensors environmentSensors = sensorsCollection.getEnvironmentSensors();
                    PressureSensor pressureSensor = environmentSensors.getPressureSensor();
                    updatePressureSensor(pressureSensor);
                }

                if (collectorModuleGnssSensorsCardCheckedValue) {
                    GnssSensor gnssSensor = sensorsCollection.getGnssSensor();
                    updateGnssSensor(gnssSensor);
                }

                if (collectorModuleAlkaidSensorsCardCheckedValue) {
                    AlkaidSensor alkaidSensor = sensorsCollection.getAlkaidSensor();
                    updateAlkaidSensor(alkaidSensor.getAlkaidSensorPositionProto());
                }
            });
            sensorsLoggerEngine.openSensors(sensorsLoggerEngineOption);

            disableCollectorModuleButtonOpenSensors();
            enableCollectorModuleButtonCloseSensors();
            enableCollectorModuleButtonStartCollect();

            lockSensorsCardCheckableStatue();
            disableCollectorModuleButtonTestAlkaidSensor();
        });

        collectorModuleButtonCloseSensors = findViewById(R.id.collector_module_button_close_sensors);
        collectorModuleButtonCloseSensors.setOnClickListener(view -> {
            sensorsLoggerEngine.closeSensors();

            disableCollectorModuleButtonCloseSensors();
            enableCollectorModuleButtonOpenSensors();
            disableCollectorModuleButtonStartCollect();
            unlockSensorsCardCheckableStatue();
            enableCollectorModuleButtonTestAlkaidSensor();
        });

        collectorModuleButtonStartCollect = findViewById(R.id.collector_module_button_start_collect);
        collectorModuleButtonStartCollect.setOnClickListener(view -> {
            logFolderName = String.valueOf(collectorModuleLogFolderNameEditText.getText());
            sensorsLoggerEngine.startLogger(getDefaultTimeStamp4Folder() + "_" + logFolderName);

            disableCollectorModuleButtonCloseSensors();
            disableCollectorModuleButtonStartCollect();
            enableCollectorModuleButtonStopCollect();
        });

        collectorModuleButtonStopCollect = findViewById(R.id.collector_module_button_stop_collect);
        collectorModuleButtonStopCollect.setOnClickListener(view -> {
            sensorsLoggerEngine.stopLogger();

            disableCollectorModuleButtonStopCollect();
            enableCollectorModuleButtonStartCollect();
            enableCollectorModuleButtonCloseSensors();
        });

        ENABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonOpenSensors.getBackground();
        DISABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonCloseSensors.getBackground();

        collectorModuleTextInputLayoutLogFolderName = findViewById(R.id.collector_module_text_field_log_folder_name);
        collectorModuleLogFolderNameEditText = collectorModuleTextInputLayoutLogFolderName.getEditText();
        collectorModuleLogFolderNameEditText.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (!checkTextInputIsNull(collectorModuleTextInputLayoutLogFolderName, /* showError= */ true)) {
                            logFolderName = String.valueOf(collectorModuleLogFolderNameEditText.getText());
                        }
                        return true;
                    }
                    return false;
                });

        initMotionSensorsCard();
        initPositionSensorsCard();
        initEnvironmentSensorsCard();
        initGnssSensorCard();
        initAlkaidSensorCard();
    }

    private void initHelper() {
        sensorHelper = new SensorHelper(MainActivity.this);
    }

}