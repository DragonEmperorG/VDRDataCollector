package cn.edu.whu.lmars.unl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.edu.whu.lmars.unl.entity.AccelerometerSensor;
import cn.edu.whu.lmars.unl.entity.AlkaidSensor;
import cn.edu.whu.lmars.unl.entity.AlkaidSensorPositionProto;
import cn.edu.whu.lmars.unl.entity.EnvironmentSensors;
import cn.edu.whu.lmars.unl.entity.GnssSensor;
import cn.edu.whu.lmars.unl.entity.MagneticFieldSensor;
import cn.edu.whu.lmars.unl.entity.MotionSensors;
import cn.edu.whu.lmars.unl.entity.PositionSensors;
import cn.edu.whu.lmars.unl.entity.PressureSensor;
import cn.edu.whu.lmars.unl.entity.SensorsCollection;
import cn.edu.whu.lmars.unl.listener.SensorsCollectionListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Drawable ENABLE_MATERIAL_BUTTON_BACKGROUND;
    private Drawable DISABLE_MATERIAL_BUTTON_BACKGROUND;

    private MaterialButton collectorModuleButtonOpenSensors;
    private MaterialButton collectorModuleButtonCloseSensors;
    private MaterialButton collectorModuleButtonStartCollect;
    private MaterialButton collectorModuleButtonStopCollect;
    private MaterialButton collectorModuleButtonTestAlkaidSensor;


    private String logFolderName = "";
    private TextInputLayout collectorModuleTextInputLayoutLogFolderName;
    private EditText collectorModuleLogFolderNameEditText;


    private boolean collectorModuleMotionSensorsCardCheckedValue;
    private MaterialCardView collectorModuleMotionSensorsCardView;
    private MaterialTextView accelerometerSensorTimestampTextView;
    private MaterialTextView accelerometerSensorAccelerationXTextView;
    private MaterialTextView accelerometerSensorAccelerationYTextView;
    private MaterialTextView accelerometerSensorAccelerationZTextView;


    private boolean collectorModulePositionSensorsCardCheckedValue;
    private MaterialCardView collectorModulePositionSensorsCardView;
    private MaterialTextView magneticSensorTimestampTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthXTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthYTextView;
    private MaterialTextView magneticSensorGeomagneticFieldStrengthZTextView;


    private boolean collectorModuleEnvironmentSensorsCardCheckedValue;
    private MaterialCardView collectorModuleEnvironmentSensorsCardView;
    private MaterialTextView pressureSensorTimestampTextView;
    private MaterialTextView pressureSensorGeomagneticFieldStrengthXTextView;


    private boolean collectorModuleGnssSensorsCardCheckedValue;
    private MaterialCardView collectorModuleGnssSensorsCardView;
    private MaterialTextView gnssSensorTimeTextView;
    private MaterialTextView gnssSensorLongitudeTextView;
    private MaterialTextView gnssSensorLatitudeTextView;
    private MaterialTextView gnssSensorAccuracyTextView;


    private boolean collectorModuleAlkaidSensorsCardCheckedValue;
    private MaterialCardView collectorModuleAlkaidSensorsCardView;
    private String alkaidSensorHostSetting = "192.168.2.20";
    private int alkaidSensorPortSetting = 4300;
    private TextInputLayout collectorModuleTextInputLayoutAlkaidSensorHostSetting;
    private EditText collectorModuleAlkaidSensorHostSettingEditText;
    private TextInputLayout collectorModuleTextInputLayoutAlkaidSensorPortSetting;
    private EditText collectorModuleAlkaidSensorPortSettingEditText;


    private SensorsLoggerEngine sensorsLoggerEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isRecordPermissionGranted()) {
            requestRecordPermission();
            return;
        }

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

    void updateAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                accelerometerSensorTimestampTextView.setText(String.valueOf(accelerometerSensor.sensorEventTimestamp));
                accelerometerSensorAccelerationXTextView.setText(String.valueOf(accelerometerSensor.values[0]));
                accelerometerSensorAccelerationYTextView.setText(String.valueOf(accelerometerSensor.values[1]));
                accelerometerSensorAccelerationZTextView.setText(String.valueOf(accelerometerSensor.values[2]));
            }
        });
    }

    void updateMagneticSensor(MagneticFieldSensor magneticFieldSensor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                magneticSensorTimestampTextView.setText(String.valueOf(magneticFieldSensor.sensorEventTimestamp));
                magneticSensorGeomagneticFieldStrengthXTextView.setText(String.valueOf(magneticFieldSensor.values[0]));
                magneticSensorGeomagneticFieldStrengthYTextView.setText(String.valueOf(magneticFieldSensor.values[1]));
                magneticSensorGeomagneticFieldStrengthZTextView.setText(String.valueOf(magneticFieldSensor.values[2]));
            }
        });
    }

    void updatePressureSensor(PressureSensor pressureSensor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pressureSensorTimestampTextView.setText(String.valueOf(pressureSensor.sensorEventTimestamp));
                pressureSensorGeomagneticFieldStrengthXTextView.setText(String.valueOf(pressureSensor.values[0]));
            }
        });
    }

    void updateGnssSensor(GnssSensor gnssSensor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Location location = gnssSensor.getGnssSensorLocation();
                gnssSensorTimeTextView.setText(String.valueOf(location.getTime()));
                gnssSensorLongitudeTextView.setText(String.valueOf(location.getLongitude()));
                gnssSensorLatitudeTextView.setText(String.valueOf(location.getLatitude()));
                gnssSensorAccuracyTextView.setText(String.valueOf(location.getAccuracy()));
            }
        });
    }

    private ArrayList<Integer> getSelectedLogSensorsTypeList() {
        ArrayList<Integer> selectedLogSensorsTypeList = new ArrayList<>();

        if (collectorModuleMotionSensorsCardCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_ACCELEROMETER);
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
//            if (logSensorsTypeList.contains(SensorsLoggerEngine.SENSOR_TYPE_ALKAID)) {
//                sensorsLoggerEngineOption.setAlkaidSensorHost(alkaidSensorHostSetting);
//                sensorsLoggerEngineOption.setAlkaidSensorPort(alkaidSensorPortSetting);
//            }
            sensorsLoggerEngine = new SensorsLoggerEngine(MainActivity.this);
            sensorsLoggerEngine.registerSensorsCollectionListener(new SensorsCollectionListener() {
                @Override
                public void onSensorsCollectionUpdated(SensorsCollection sensorsCollection) {
                    if (collectorModuleMotionSensorsCardCheckedValue) {
                        MotionSensors motionSensors = sensorsCollection.getMotionSensors();
                        AccelerometerSensor accelerometerSensor = motionSensors.getAccelerometerSensor();
                        updateAccelerometerSensor(accelerometerSensor);
                    }

                    if (collectorModulePositionSensorsCardCheckedValue) {
                        PositionSensors positionSensors = sensorsCollection.getPositionSensors();
                        MagneticFieldSensor magneticFieldSensor = positionSensors.getMagneticFieldSensor();
                        updateMagneticSensor(magneticFieldSensor);
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
                }
            });
            sensorsLoggerEngine.openSensors(sensorsLoggerEngineOption);

            disableCollectorModuleButtonOpenSensors();
            enableCollectorModuleButtonCloseSensors();
            enableCollectorModuleButtonStartCollect();
        });

        collectorModuleButtonCloseSensors = findViewById(R.id.collector_module_button_close_sensors);
        collectorModuleButtonCloseSensors.setOnClickListener(view -> {
            sensorsLoggerEngine.closeSensors();

            disableCollectorModuleButtonCloseSensors();
            enableCollectorModuleButtonOpenSensors();
            disableCollectorModuleButtonStartCollect();
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

        collectorModuleButtonTestAlkaidSensor = findViewById(R.id.collector_module_button_test_alkaid_sensor);
        collectorModuleButtonTestAlkaidSensor.setOnClickListener(view -> {
            AlkaidSensorPositionProto testAlkaidSensorPositionProto = null;
            alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingEditText.getText());
            alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingEditText.getText()));
            try {
                testAlkaidSensorPositionProto = AlkaidSensor.testAlkaidSensor(alkaidSensorHostSetting, alkaidSensorPortSetting);
                if (testAlkaidSensorPositionProto.getStatus() != -1) {
                    alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingEditText.getText());
                    alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingEditText.getText()));
                    collectorModuleAlkaidSensorsCardView.setCheckable(true);
                }
            } catch (Exception e) {
                collectorModuleTextInputLayoutAlkaidSensorHostSetting.setError(getResources().getString(R.string.collector_module_alkaid_sensor_host_setting_text_field_error_text));
                collectorModuleAlkaidSensorsCardView.setChecked(false);
                collectorModuleAlkaidSensorsCardView.setCheckable(false);
                e.printStackTrace();
            }
        });

        ENABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonOpenSensors.getBackground();
        DISABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonCloseSensors.getBackground();

        collectorModuleMotionSensorsCardView = findViewById(R.id.collector_module_motion_sensors_card);
        collectorModuleMotionSensorsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                collectorModuleMotionSensorsCardCheckedValue = !collectorModuleMotionSensorsCardView.isChecked();
                collectorModuleMotionSensorsCardView.setChecked(collectorModuleMotionSensorsCardCheckedValue);
                return true;
            }
        });
        collectorModuleMotionSensorsCardCheckedValue = true;
        collectorModuleMotionSensorsCardView.setChecked(true);
        collectorModuleMotionSensorsCardView.setCheckable(false);
        accelerometerSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_timestamp_value);
        accelerometerSensorAccelerationXTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_x_value);
        accelerometerSensorAccelerationYTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_y_value);
        accelerometerSensorAccelerationZTextView = findViewById(R.id.collector_module_motion_sensors_card_accelerometer_sensor_acceleration_z_value);

        collectorModulePositionSensorsCardView = findViewById(R.id.collector_module_position_sensors_card);
        collectorModulePositionSensorsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                collectorModulePositionSensorsCardCheckedValue = !collectorModulePositionSensorsCardView.isChecked();
                collectorModuleMotionSensorsCardView.setChecked(collectorModulePositionSensorsCardCheckedValue);
                return true;
            }
        });
        collectorModulePositionSensorsCardCheckedValue = true;
        collectorModulePositionSensorsCardView.setChecked(true);
        collectorModulePositionSensorsCardView.setCheckable(false);
        magneticSensorTimestampTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_timestamp_value);
        magneticSensorGeomagneticFieldStrengthXTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_x_value);
        magneticSensorGeomagneticFieldStrengthYTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_y_value);
        magneticSensorGeomagneticFieldStrengthZTextView = findViewById(R.id.collector_module_position_sensors_card_magnetic_sensor_geomagnetic_field_strength_z_value);

        collectorModuleEnvironmentSensorsCardView = findViewById(R.id.collector_module_environment_sensors_card);
        collectorModuleEnvironmentSensorsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                collectorModuleEnvironmentSensorsCardCheckedValue = !collectorModuleEnvironmentSensorsCardView.isChecked();
                collectorModuleMotionSensorsCardView.setChecked(collectorModuleEnvironmentSensorsCardCheckedValue);
                return true;
            }
        });
        collectorModuleEnvironmentSensorsCardCheckedValue = true;
        collectorModuleEnvironmentSensorsCardView.setChecked(true);
        collectorModuleEnvironmentSensorsCardView.setCheckable(false);
        pressureSensorTimestampTextView = findViewById(R.id.collector_module_motion_sensors_card_pressure_sensor_timestamp_value);
        pressureSensorGeomagneticFieldStrengthXTextView = findViewById(R.id.collector_module_motion_sensors_card_pressure_sensor_pressure_value);


        collectorModuleGnssSensorsCardView = findViewById(R.id.collector_module_gnss_sensors_card);
        collectorModuleGnssSensorsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                collectorModuleGnssSensorsCardCheckedValue = !collectorModuleGnssSensorsCardView.isChecked();
                collectorModuleGnssSensorsCardView.setChecked(collectorModuleGnssSensorsCardCheckedValue);
                return true;
            }
        });
        collectorModuleGnssSensorsCardCheckedValue = true;
        collectorModuleGnssSensorsCardView.setChecked(true);
        collectorModuleGnssSensorsCardView.setCheckable(false);
        gnssSensorTimeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_time_value);
        gnssSensorLongitudeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_longitude_value);
        gnssSensorLatitudeTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_latitude_value);
        gnssSensorAccuracyTextView = findViewById(R.id.collector_module_gnss_sensor_card_location_accuracy_value);


        collectorModuleAlkaidSensorsCardView = findViewById(R.id.collector_module_alkaid_sensors_card);
        collectorModuleAlkaidSensorsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                collectorModuleAlkaidSensorsCardCheckedValue = !collectorModuleAlkaidSensorsCardView.isChecked();
                collectorModuleAlkaidSensorsCardView.setChecked(collectorModuleAlkaidSensorsCardCheckedValue);
                return true;
            }
        });
        collectorModuleAlkaidSensorsCardCheckedValue = false;
        collectorModuleAlkaidSensorsCardView.setChecked(false);
        collectorModuleAlkaidSensorsCardView.setCheckable(false);

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

        collectorModuleTextInputLayoutAlkaidSensorHostSetting = findViewById(R.id.collector_module_text_field_alkaid_sensor_host_setting);
        collectorModuleAlkaidSensorHostSettingEditText = collectorModuleTextInputLayoutAlkaidSensorHostSetting.getEditText();
        collectorModuleAlkaidSensorHostSettingEditText.setText(alkaidSensorHostSetting);
        collectorModuleAlkaidSensorHostSettingEditText.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (!checkTextInputIsNull(collectorModuleTextInputLayoutAlkaidSensorHostSetting, /* showError= */ true)) {
                            alkaidSensorHostSetting = String.valueOf(collectorModuleAlkaidSensorHostSettingEditText.getText());
                        }
                        return true;
                    }
                    return false;
                });

        collectorModuleTextInputLayoutAlkaidSensorPortSetting = findViewById(R.id.collector_module_text_field_alkaid_sensor_port_setting);
        collectorModuleAlkaidSensorPortSettingEditText = collectorModuleTextInputLayoutAlkaidSensorPortSetting.getEditText();
        collectorModuleAlkaidSensorPortSettingEditText.setText(String.valueOf(alkaidSensorPortSetting));
        collectorModuleAlkaidSensorPortSettingEditText.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (!checkTextInputIsNull(collectorModuleTextInputLayoutAlkaidSensorPortSetting, /* showError= */ true)) {
                            alkaidSensorPortSetting = Integer.parseInt(String.valueOf(collectorModuleAlkaidSensorPortSettingEditText.getText()));
                        }
                        return true;
                    }
                    return false;
                });
    }


}