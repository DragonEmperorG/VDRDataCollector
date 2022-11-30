package cn.edu.whu.lmars.unl.util;

import android.hardware.SensorManager;
import android.util.Log;

public class Conversion {

    private static final String TAG = "Conversion";

    public static float[] rads2Degrees(float[] rads) {
        int length = rads.length;
        float[] degrees = new float[length];
        for (int i = 0; i < length; i++) {
            degrees[i] = (float) Math.toDegrees(rads[i]);
        }
        return degrees;
    }

    public static float[] degrees2Rads(float[] degrees) {
        int length = degrees.length;
        float[] rads = new float[length];
        for (int i = 0; i < length; i++) {
            rads[i] = (float) Math.toRadians(degrees[i]);
        }
        return rads;
    }

    public static float[] rotationVector2EulerAngleDegree(float[] gameRotationVectorSensorValue) {
        /* 使用游戏旋转矢量传感器 坐标系和数值说明
         * https://developer.android.google.cn/guide/topics/sensors/sensors_position#sensors-pos-gamerot
         * https://developer.android.google.cn/guide/topics/sensors/sensors_motion#sensors-motion-rotate
         * https://developer.android.google.cn/reference/android/hardware/SensorEvent#sensor.type_game_rotation_vector:
         * https://developer.android.google.cn/reference/android/hardware/SensorEvent#sensor.type_rotation_vector:
         */

        float[] eulerAngleRad = rotationVector2EulerAngleRad(gameRotationVectorSensorValue);
        float[] eulerAngleDegree = rads2Degrees(eulerAngleRad);
        return eulerAngleDegree;
    }

    public static float[] rotationVector2EulerAngleRad(float[] rotationVectorValue) {
        int length = rotationVectorValue.length;
        float[] quaternion = new float[length];
        // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/hardware/SensorManager.java
        SensorManager.getQuaternionFromVector(quaternion, rotationVectorValue);
        return quaternion2EulerAngle(quaternion);
    }

    public static float[] quaternion2EulerAngle(float[] quaternion) {
        float eulerAngleHeading;
        float eulerAngleAttitude;
        float eulerAngleBank;

        float quaternionW = quaternion[0];
        float quaternionX = quaternion[1];
        float quaternionY = quaternion[2];
        float quaternionZ = quaternion[3];

        float test = quaternionX * quaternionY + quaternionZ * quaternionW;
        float sqx = quaternionX * quaternionX;
        float sqy = quaternionY * quaternionY;
        float sqz = quaternionZ * quaternionZ;

        eulerAngleHeading = (float) Math.atan2(2 * quaternionY * quaternionW - 2 * quaternionX * quaternionZ, 1 - 2 * sqy - 2 * sqz);
        eulerAngleAttitude = (float) Math.asin(2 * test);
        eulerAngleBank = (float) Math.atan2(2 * quaternionX * quaternionW - 2 * quaternionY * quaternionZ, 1 - 2 * sqx - 2 * sqz);

//        float sqw = quaternionW * quaternionW;
//        float modulus = sqx + sqy + sqz + sqw;
//        Log.d(TAG, "quaternion2EulerAngle: " + modulus);

        return new float[]{eulerAngleHeading, eulerAngleAttitude, eulerAngleBank};
    }
}
