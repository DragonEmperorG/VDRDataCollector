package cn.edu.whu.lmars.unl;

import java.util.ArrayList;

public class SensorsLoggerEngineOption {

    private boolean fileLoggerSwitcher = true;

    private String loggerFolderName;

    private ArrayList<Integer> logSensorsTypeList;

    public SensorsLoggerEngineOption() {
    }

    public boolean getFileLoggerSwitcher() {
        return fileLoggerSwitcher;
    }

    public void setFileLoggerSwitcher(boolean fileLoggerSwitcher) {
        this.fileLoggerSwitcher = fileLoggerSwitcher;
    }

    public String getLoggerFolderName() {
        return loggerFolderName;
    }

    public void setLoggerFolderName(String loggerFolderName) {
        this.loggerFolderName = loggerFolderName;
    }

    public ArrayList<Integer> getLogSensorsTypeList() {
        return logSensorsTypeList;
    }

    public void setLogSensorsTypeList(ArrayList<Integer> logSensorsTypeList) {
        this.logSensorsTypeList = logSensorsTypeList;
    }
}
