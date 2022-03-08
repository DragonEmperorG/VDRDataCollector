package cn.edu.whu.lmars.unl.entity;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AlkaidSensor {

    private static final String TAG = "AlkaidSensor";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private long sensorEventUpdateSystemTimestamp = 0L;
    private long sensorEventTimestamp = 0L;
    private String csvFormattedValues = "0.0, 0.0, 0.0";

    private final OkHttpClient alkaidSensorClient = new OkHttpClient();

    public AlkaidSensor() {
    }

    public void updateAlkaidSensor() throws Exception {
        sensorEventUpdateSystemTimestamp = System.currentTimeMillis();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        alkaidSensorClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }

                    Log.d(TAG, "onResponse: " + responseBody.string());
                }
            }
        });

        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        stringBuilder.append(sensorEventTimestamp);
        csvFormattedValues = stringBuilder.toString();
    }

    public String getCsvFormattedAlkaidSensorValues() {
        return csvFormattedValues;
    }
}
