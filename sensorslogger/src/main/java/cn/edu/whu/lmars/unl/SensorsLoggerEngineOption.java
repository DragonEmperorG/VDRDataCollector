package cn.edu.whu.lmars.unl;

import java.util.ArrayList;

public class SensorsLoggerEngineOption {

    private ArrayList<Integer> logSensorsTypeList;

    private String alkaidSensorHost;

    private int alkaidSensorPort;

    public SensorsLoggerEngineOption() {
    }

    public ArrayList<Integer> getLogSensorsTypeList() {
        return logSensorsTypeList;
    }

    public void setLogSensorsTypeList(ArrayList<Integer> logSensorsTypeList) {
        this.logSensorsTypeList = logSensorsTypeList;
    }

    public String getAlkaidSensorHost() {
        return alkaidSensorHost;
    }

    public void setAlkaidSensorHost(String alkaidSensorHost) {
        this.alkaidSensorHost = alkaidSensorHost;
    }

    public int getAlkaidSensorPort() {
        return alkaidSensorPort;
    }

    public void setAlkaidSensorPort(int alkaidSensorPort) {
        this.alkaidSensorPort = alkaidSensorPort;
    }
}
