package cn.powerr.mamabike.bike.service;

import cn.powerr.mamabike.bike.entity.RideRecord;

import java.util.List;

public interface RideRecordService {
    List<RideRecord> listRideRecord(Long userId, Long lastId);
}
