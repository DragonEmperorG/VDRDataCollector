package cn.edu.whu.lmars.unl.entity;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AlkaidSensor {

    private static final String TAG = "AlkaidSensor";

    private static final String ALKAID_SENSOR_INTERFACE_URL_SCHEME = "http";

    private long sensorEventUpdateSystemTimestamp = 0L;
    private long sensorSentRequestTimestamp = 0L;
    private long sensorReceivedResponseTimestamp = 0L;
    private String csvFormattedValues = "0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0";

    private final OkHttpClient alkaidSensorClient = new OkHttpClient();

    private int ALKAID_SENSOR_INTERFACE_URL_PORT;
    private String ALKAID_SENSOR_INTERFACE_URL_HOST;
    private HttpUrl ALKAID_SENSOR_INTERFACE_URL;
    private Request ALKAID_SENSOR_INTERFACE_REQUEST;

    private static AlkaidSensorPositionProto rAlkaidSensorPositionProto;

    public AlkaidSensor() {
        ALKAID_SENSOR_INTERFACE_URL = new HttpUrl.Builder()
                .scheme(ALKAID_SENSOR_INTERFACE_URL_SCHEME)
                .host("192.168.2.20")
                .port(4300)
                .addPathSegment("position")
                .build();
        ALKAID_SENSOR_INTERFACE_REQUEST = new Request.Builder()
                .url(ALKAID_SENSOR_INTERFACE_URL)
                .build();
    }

    public void configAlkaidSensor(String host, int port) {
        ALKAID_SENSOR_INTERFACE_URL = new HttpUrl.Builder()
                .scheme(ALKAID_SENSOR_INTERFACE_URL_SCHEME)
                .host(host)
                .port(port)
                .addPathSegment("position")
                .build();
        ALKAID_SENSOR_INTERFACE_REQUEST = new Request.Builder()
                .url(ALKAID_SENSOR_INTERFACE_URL)
                .build();
    }

    public void updateAlkaidSensor() throws Exception {
        sensorEventUpdateSystemTimestamp = System.currentTimeMillis();

        alkaidSensorClient.newCall(ALKAID_SENSOR_INTERFACE_REQUEST).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    assert responseBody != null;
                    String responseBodyString = responseBody.string();
                    Log.d(TAG, "onResponse: " + responseBodyString);
                    AlkaidSensorPositionProto alkaidSensorPositionProto = JSON.parseObject(responseBodyString, AlkaidSensorPositionProto.class);
                    Log.d(TAG, "onResponse: " + alkaidSensorPositionProto.getTimems());

                    sensorSentRequestTimestamp = response.sentRequestAtMillis();
                    sensorReceivedResponseTimestamp = response.receivedResponseAtMillis();
                    StringBuilder stringBuilder;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(sensorSentRequestTimestamp);
                    stringBuilder.append(", ").append(sensorReceivedResponseTimestamp);
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getStatus());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getTimems());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getLongitude());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getLatitude());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getHeight());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getAzimuth());
                    stringBuilder.append(", ").append(alkaidSensorPositionProto.getSpeed());
                    csvFormattedValues = stringBuilder.toString();
                }
            }
        });
    }

    public String getCsvFormattedAlkaidSensorValues() {
        return csvFormattedValues;
    }

    public static AlkaidSensorPositionProto testAlkaidSensor(String host, int port) throws Exception {
        HttpUrl SHANGHAI_PROJECT_URL = new HttpUrl.Builder()
                .scheme(ALKAID_SENSOR_INTERFACE_URL_SCHEME)
                .host(host)
                .port(port)
                .addPathSegment("position")
                .build();

        Request request = new Request.Builder()
                .url(SHANGHAI_PROJECT_URL)
                .build();

        OkHttpClient testAlkaidSensorClient = new OkHttpClient();
        rAlkaidSensorPositionProto = new AlkaidSensorPositionProto();
        try (Response response = testAlkaidSensorClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try (ResponseBody responseBody = response.body()) {
                String responseBodyString = responseBody.string();
                Log.d(TAG, "testAlkaidSensor: " + responseBodyString);
//                rAlkaidSensorPositionProto = JSON.parseObject(responseBodyString, AlkaidSensorPositionProto.class);
                return rAlkaidSensorPositionProto;
            }
        }

    }

}
