package cn.powerr.mamabike.bike.service.impl;

import cn.powerr.mamabike.bike.dao.BikeMapper;
import cn.powerr.mamabike.bike.dao.RideFeeMapper;
import cn.powerr.mamabike.bike.dao.RideRecordMapper;
import cn.powerr.mamabike.bike.dao.WalletMapper;
import cn.powerr.mamabike.bike.entity.*;
import cn.powerr.mamabike.bike.service.BikeService;
import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.common.response.CodeMsg;
import cn.powerr.mamabike.common.util.RandomNumber;
import cn.powerr.mamabike.user.dao.UserMapper;
import cn.powerr.mamabike.user.entity.User;
import cn.powerr.mamabike.user.entity.UserElement;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BikeServiceImpl implements BikeService {

    @Autowired
    private RideRecordMapper rideRecordMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private BikeMapper bikeMapper;
    @Autowired
    private RideFeeMapper feeMapper;

    /**
     * 未认证
     */
    public static final byte NOT_VERIFY = 2;

    public static final int BIKE_UN_RIDING = 2;
    public static final int BIKE_RIDING = 1;

    /**
     * 1.检验用户是否实名认证
     * 2.是否缴付押金（remain_sum）
     * 3.检查用户是否有正在骑行的自行车
     * 4.检查用户余额是否大于一元
     * 5.推送
     * 6.设置数据库  记录骑行时间  （记录骑行轨迹 单车上报坐标）
     * 7.设置mongodb单车状态
     *
     * @param currentUser
     * @param number
     * @return
     */
    @Transactional
    @Override
    public boolean unLockBike(UserElement currentUser, Long number) {
        User user = userMapper.selectByPrimaryKey(currentUser.getUserId());
        if (user.getVerifyFlag() == NOT_VERIFY) {
            throw new MaMaBikeException(CodeMsg.USER_NON_VERIFY);
        }
        Wallet wallet = walletMapper.selectByUserId(currentUser.getUserId());
        if (wallet.getDeposit().intValue() <= 0) {
            throw new MaMaBikeException(CodeMsg.USER_NON_DEPOSIT);
        }
        // 1.骑行中
        RideRecord hasRecorded = rideRecordMapper.selectByStatus(currentUser.getUserId(), (byte) BIKE_RIDING);
        if (hasRecorded != null) {
            throw new MaMaBikeException(CodeMsg.USER_RIDING);
        }
        if (wallet.getRemainSum().intValue() < 1) {
            throw new MaMaBikeException(CodeMsg.USER_NON_REMAIN);
        }
        // 调用推送接口推送给单车，进行解锁
        boolean pushSucc = pushToBike(currentUser.getPushChannelId(), "unLock");
        if (!pushSucc) {
            return false;
        }
        RideRecord rideRecord = new RideRecord();
        rideRecord.setBikeNo(number);
        String recordNo = new Date().getTime() + RandomNumber.verCode();
        rideRecord.setRecordNo(recordNo);
        rideRecord.setUserid(currentUser.getUserId());
        // 骑行记录状态不用设置
        // 默认插入骑行记录就是骑行中
//        rideRecord.setStatus((byte) 1);
        rideRecord.setStartTime(new Date());
        rideRecordMapper.insertSelective(rideRecord);
        Query query = new Query(Criteria.where("bikeNumber").is(number));
        // BIKE_RIDING 骑行中
        Update update = Update.update("status", BIKE_RIDING);
        mongoTemplate.updateFirst(query, update, "bike_location");
        // 插入ride_contrail记录
        return true;
    }

    /**
     * 锁定单车
     * 1.计算花费时间
     * 2.计算花费费用
     * 3.写入ride_record
     * 4.wallet钱包扣费
     * 5.mongodb status状态改为未骑行
     *
     * @param location
     * @return
     */
    @Transactional
    @Override
    public boolean lockBike(BikeLocation location) {
        RideRecord rideRecord = rideRecordMapper.selectByBikeNoOnGoing(location.getBikeNumber());
        long userId = rideRecord.getUserid();
        Bike bike = bikeMapper.selectByBikeNo(location.getBikeNumber());
        // 骑行结束时间
        Date endTime = new Date();
        rideRecord.setEndTime(endTime);
        Date startTime = rideRecord.getStartTime();
        long rideTime = endTime.getTime() - startTime.getTime();
        int minutes = (int) (rideTime / (60 * 1000));
        rideRecord.setStatus((byte) BIKE_UN_RIDING);
        // 骑行花费分钟数
        rideRecord.setRideTime(minutes);
        // 获取骑行时间单位、费用单位
        RideFee rideFee = feeMapper.selectByType(bike.getType());
        Integer minUnit = rideFee.getMinUnit();
        BigDecimal fee = rideFee.getFee();
        BigDecimal cost;
        // 计算骑行总花费
        if (minutes / minUnit == 0) {
            cost = fee;
        } else if (minutes % minUnit == 0) {
            int times = minutes / minUnit;
            cost = fee.multiply(new BigDecimal(times));
        } else {
            // 不整除加一个时间单位
            int times = minutes / minUnit;
            times += 1;
            cost = fee.multiply(new BigDecimal(times));
        }
        rideRecord.setRideCost(cost);
        rideRecordMapper.updateByPrimaryKeySelective(rideRecord);
        // 钱包扣费
        Wallet wallet = walletMapper.selectByUserId(userId);
        wallet.setRemainSum(wallet.getDeposit().subtract(cost));
        walletMapper.updateByPrimaryKey(wallet);
        // 更新mongodb bike_location状态
        Query query = new Query(Criteria.where("bikeNumber").is(location.getBikeNumber()));
        Update update = Update.update("status", BIKE_UN_RIDING)
                .set("location.coordinates", location.getCoordinates());
        mongoTemplate.updateFirst(query, update, "bike_location");
        // 插入ride_contrail记录
        return true;
    }

    /**
     * 上报骑行记录
     * @param bikeLocation
     * @return
     */
    @Override
    public boolean reportLocation(BikeLocation bikeLocation) {
        RideRecord rideRecord = rideRecordMapper.selectByBikeNoOnGoing(bikeLocation.getBikeNumber());
        if (rideRecord == null) {
            throw new MaMaBikeException(CodeMsg.RIDING_RECORD_NON);
        }
        DBObject object = mongoTemplate.getCollection("ride_contrail")
                .findOne(new BasicDBObject("record_no", rideRecord.getRecordNo()));
        if (object == null) {
            List<BasicDBObject> list = Lists.newArrayList();
            BasicDBObject temp = new BasicDBObject("loc", bikeLocation.getCoordinates());
            list.add(temp);
            BasicDBObject insertObj = new BasicDBObject("record_no", rideRecord.getRecordNo())
                    .append("bike_no", rideRecord.getBikeNo())
                    .append("contrail", list);
            mongoTemplate.insert(insertObj, "ride_contrail");
        } else {
            Query query = new Query(Criteria.where("record_no").is(rideRecord.getRecordNo()));
            Update update = new Update().push("contrail", new BasicDBObject("loc", bikeLocation.getCoordinates()));
            mongoTemplate.updateFirst(query, update, "ride_contrail");
        }
        return true;
    }

    private boolean pushToBike(String pushChannelId, String pushOrder) {
        if (pushOrder.equalsIgnoreCase("unLock")) {
            System.out.println("bike is unlock");
            return true;
        }
        // 其他情况
        return false;
    }


}
