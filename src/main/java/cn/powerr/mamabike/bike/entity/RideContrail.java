package cn.powerr.mamabike.bike.entity;

import lombok.Data;

import java.util.List;

@Data
public class RideContrail {
    private String rideRecordNo;
    private Long bikeNo;
    private List<Point> contrail;
}
