package cn.edu.whu.lmars.unl.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static final String TAG = "TimeUtil";

    public static String getDayFolderName() {
        SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA);
        Date mDate = new Date();
        return customDateFormat.format(mDate);
    }

}
