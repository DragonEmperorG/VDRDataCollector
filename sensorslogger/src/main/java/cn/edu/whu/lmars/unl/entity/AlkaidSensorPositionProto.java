package cn.edu.whu.lmars.unl.entity;

public class AlkaidSensorPositionProto {
    private int status = -1;
    private String timems = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double height = 0.0;
    private double azimuth = 0.0;
    private double speed = 0.0;
    private double age = 0.0;
    private int satsnum = 0;

    public AlkaidSensorPositionProto() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimems() {
        return timems;
    }

    public void setTimems(String timems) {
        this.timems = timems;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public int getSatsnum() {
        return satsnum;
    }

    public void setSatsnum(int satsnum) {
        this.satsnum = satsnum;
    }
}
