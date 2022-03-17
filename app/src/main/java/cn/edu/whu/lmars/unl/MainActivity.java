package cn.edu.whu.lmars.unl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.edu.whu.lmars.unl.entity.AlkaidSensor;
import cn.edu.whu.lmars.unl.entity.AlkaidSensorPositionProto;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Drawable ENABLE_MATERIAL_BUTTON_BACKGROUND;
    private Drawable DISABLE_MATERIAL_BUTTON_BACKGROUND;

    private MaterialButton collectorModuleButtonOpenSensors;
    private MaterialButton collectorModuleButtonCloseSensors;
    private MaterialButton collectorModuleButtonStartCollect;
    private MaterialButton collectorModuleButtonStopCollect;
    private MaterialButton collectorModuleButtonTestAlkaidSensor;

    private boolean collectorModuleChipMotionSensorsChoiceCheckedValue;
    private boolean collectorModuleChipPositionSensorsChoiceCheckedValue;
    private boolean collectorModuleChipEnvironmentSensorsChoiceCheckedValue;
    private boolean collectorModuleChipGnssSensorsChoiceCheckedValue;
    private boolean collectorModuleChipAlkaidSensorsChoiceCheckedValue;
    private Chip collectorModuleChipMotionSensorsChoice;
    private Chip collectorModuleChipPositionSensorsChoice;
    private Chip collectorModuleChipEnvironmentSensorsChoice;
    private Chip collectorModuleChipGnssSensorsChoice;
    private Chip collectorModuleChipAlkaidSensorsChoice;

    private String logFolderName = "";
    private TextInputLayout collectorModuleTextInputLayoutLogFolderName;
    private EditText collectorModuleLogFolderNameEditText;

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

    private ArrayList<Integer> getSelectedLogSensorsTypeList() {
        ArrayList<Integer> selectedLogSensorsTypeList = new ArrayList<>();

        if (collectorModuleChipMotionSensorsChoiceCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_ACCELEROMETER);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GRAVITY);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GYROSCOPE);
            selectedLogSensorsTypeList.add(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
            selectedLogSensorsTypeList.add(Sensor.TYPE_LINEAR_ACCELERATION);
            selectedLogSensorsTypeList.add(Sensor.TYPE_ROTATION_VECTOR);
        }

        if (collectorModuleChipPositionSensorsChoiceCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_GAME_ROTATION_VECTOR);
            selectedLogSensorsTypeList.add(Sensor.TYPE_MAGNETIC_FIELD);
            selectedLogSensorsTypeList.add(Sensor.TYPE_ORIENTATION);
        }

        if (collectorModuleChipEnvironmentSensorsChoiceCheckedValue) {
            selectedLogSensorsTypeList.add(Sensor.TYPE_PRESSURE);
        }

        if (collectorModuleChipGnssSensorsChoiceCheckedValue) {
            selectedLogSensorsTypeList.add(SensorsLoggerEngine.SENSOR_TYPE_GNSS);
        }

        if (collectorModuleChipAlkaidSensorsChoiceCheckedValue) {
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
            logSensorsTypeList.add(2022);
            sensorsLoggerEngineOption.setLogSensorsTypeList(logSensorsTypeList);
            if (logSensorsTypeList.contains(SensorsLoggerEngine.SENSOR_TYPE_ALKAID)) {
                sensorsLoggerEngineOption.setAlkaidSensorHost(alkaidSensorHostSetting);
                sensorsLoggerEngineOption.setAlkaidSensorPort(alkaidSensorPortSetting);
            }
            sensorsLoggerEngine = new SensorsLoggerEngine(MainActivity.this);
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
                    collectorModuleChipAlkaidSensorsChoice.setCheckable(true);
                }
            } catch (Exception e) {
                collectorModuleTextInputLayoutAlkaidSensorHostSetting.setError(getResources().getString(R.string.collector_module_alkaid_sensor_host_setting_text_field_error_text));
                collectorModuleChipAlkaidSensorsChoice.setChecked(false);
                collectorModuleChipAlkaidSensorsChoice.setCheckable(false);
                e.printStackTrace();
            }
        });

        ENABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonOpenSensors.getBackground();
        DISABLE_MATERIAL_BUTTON_BACKGROUND = collectorModuleButtonCloseSensors.getBackground();

        collectorModuleChipMotionSensorsChoice = findViewById(R.id.collector_module_motion_sensors_choice_chip);
        collectorModuleChipMotionSensorsChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                collectorModuleChipMotionSensorsChoiceCheckedValue = b;
            }
        });
        collectorModuleChipMotionSensorsChoiceCheckedValue = true;
        collectorModuleChipMotionSensorsChoice.setChecked(true);
        collectorModuleChipMotionSensorsChoice.setCheckable(false);

        collectorModuleChipPositionSensorsChoice = findViewById(R.id.collector_module_position_sensors_choice_chip);
        collectorModuleChipPositionSensorsChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                collectorModuleChipPositionSensorsChoiceCheckedValue = b;
            }
        });
        collectorModuleChipPositionSensorsChoiceCheckedValue = true;
        collectorModuleChipPositionSensorsChoice.setChecked(true);
        collectorModuleChipPositionSensorsChoice.setCheckable(false);

        collectorModuleChipEnvironmentSensorsChoice = findViewById(R.id.collector_module_environment_sensors_choice_chip);
        collectorModuleChipEnvironmentSensorsChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                collectorModuleChipEnvironmentSensorsChoiceCheckedValue = b;
            }
        });
        collectorModuleChipEnvironmentSensorsChoiceCheckedValue = true;
        collectorModuleChipEnvironmentSensorsChoice.setChecked(true);
        collectorModuleChipEnvironmentSensorsChoice.setCheckable(false);

        collectorModuleChipGnssSensorsChoice = findViewById(R.id.collector_module_gnss_sensor_choice_chip);
        collectorModuleChipGnssSensorsChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                collectorModuleChipGnssSensorsChoiceCheckedValue = b;
            }
        });
        collectorModuleChipGnssSensorsChoiceCheckedValue = true;
        collectorModuleChipGnssSensorsChoice.setChecked(true);
        collectorModuleChipGnssSensorsChoice.setCheckable(false);

        collectorModuleChipAlkaidSensorsChoice = findViewById(R.id.collector_module_alkaid_sensor_choice_chip);
        collectorModuleChipAlkaidSensorsChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                collectorModuleChipAlkaidSensorsChoiceCheckedValue = b;
            }
        });
        collectorModuleChipAlkaidSensorsChoiceCheckedValue = false;
        collectorModuleChipAlkaidSensorsChoice.setChecked(false);
        collectorModuleChipAlkaidSensorsChoice.setCheckable(false);

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