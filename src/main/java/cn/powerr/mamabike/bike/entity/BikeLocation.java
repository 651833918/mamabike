package cn.powerr.mamabike.bike.entity;

import lombok.Data;

@Data
public class BikeLocation {
    private String id;
    private Long bikeNumber;
    private int status;
    private Double[] coordinates;
    private Double distance;
}
