package cn.powerr.mamabike.bike.service.impl;

import cn.powerr.mamabike.bike.entity.BikeLocation;
import cn.powerr.mamabike.bike.entity.Point;
import cn.powerr.mamabike.bike.entity.RideContrail;
import cn.powerr.mamabike.bike.service.BikeGeoService;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeGeoServiceImpl implements BikeGeoService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * @param collection    要查询的集合
     * @param locationField query的键
     * @param point         经纬度
     * @param minDistance
     * @param maxDistance
     * @param query         要执行查询的条件
     * @param fields        可以做一些限制
     * @param limit         查询多少行
     * @return
     */
    @Override
    public List<BikeLocation> nearBikes(String collection, String locationField,
                                        Point point, long minDistance, long maxDistance,
                                        DBObject query, DBObject fields, int limit) {
        if (query == null) {
            query = new BasicDBObject();
        }
        query.put(locationField, new BasicDBObject("$nearSphere",
                new BasicDBObject("$geometry",
                        new BasicDBObject("type", "Point")
                                .append("coordinates", new double[]{point.getLongitude(), point.getLatitude()})
                ).append("$minDistance", minDistance)
                        .append("H$maxDistance", maxDistance)));
        // 查找未骑行的单车
        query.put("status", BikeServiceImpl.BIKE_UN_RIDING);
        List<DBObject> dbObjects = mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
        List<BikeLocation> res = Lists.newArrayList();
        for (DBObject object : dbObjects) {
            BikeLocation bikeLocation = new BikeLocation();
            bikeLocation.setBikeNumber(((Integer) object.get("bikeNumber")).longValue());
            bikeLocation.setStatus((Integer) object.get("status"));
            BasicDBList coordinates = (BasicDBList) ((BasicDBObject) object.get("location")).get("coordinates");
            Double[] tmp = new Double[2];
            coordinates.toArray(tmp);
            bikeLocation.setCoordinates(tmp);
            res.add(bikeLocation);
        }
        return res;
    }

    /**
     * 查询单条骑行记录详细信息
     * @param recordNo
     * @return
     */
    @Override
    public RideContrail showOneRecord(String recordNo) {
        DBObject one = mongoTemplate.getCollection("ride_contrail").findOne(new BasicDBObject("record_no", recordNo));
        RideContrail rideContrail = new RideContrail();
        rideContrail.setRideRecordNo(recordNo);
        rideContrail.setBikeNo(((Integer)one.get("bike_no")).longValue());
        BasicDBList list= (BasicDBList) one.get("contrail");
        List<Point> points = Lists.newArrayList();
        for (Object ob : list) {
            BasicDBList loc = (BasicDBList) ((BasicDBObject) ob).get("loc");
            Double[] temp = new Double[2];
            loc.toArray(temp);
            Point point = new Point(temp[0],temp[1]);
            points.add(point);
        }
        rideContrail.setContrail(points);
        return rideContrail;
    }
}
