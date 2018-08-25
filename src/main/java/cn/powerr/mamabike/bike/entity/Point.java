package cn.powerr.mamabike.bike.entity;

import lombok.Data;

@Data
public class Point {
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;

    public Point() {
    }

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
