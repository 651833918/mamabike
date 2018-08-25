package cn.powerr.mamabike.bike.service;

import cn.powerr.mamabike.bike.entity.BikeLocation;
import cn.powerr.mamabike.bike.entity.Point;
import com.mongodb.DBObject;

import java.util.List;

public interface BikeGeoService {


    List<BikeLocation> nearBikes(String collection, String locationField, Point point,
                                 long minDistance, long maxDistance,
                                 DBObject query, DBObject fields, int limit);
}
