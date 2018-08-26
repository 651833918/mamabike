package cn.powerr.mamabike.bike.service.impl;

import cn.powerr.mamabike.bike.dao.RideRecordMapper;
import cn.powerr.mamabike.bike.entity.RideRecord;
import cn.powerr.mamabike.bike.service.RideRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class
RideRecordServiceImpl implements RideRecordService {
    @Autowired
    private RideRecordMapper rideRecordMapper;

    /**
     * 列表展示骑行信息
     * @param userId
     * @param lastId
     * @return
     */
    @Override
    public List<RideRecord> listRideRecord(Long userId, Long lastId) {
        List<RideRecord> records = rideRecordMapper.selectRecords(userId,lastId);
        return records;
    }
}
