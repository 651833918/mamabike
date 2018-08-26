package cn.powerr.mamabike.bike.controller;

import cn.powerr.mamabike.bike.entity.Bike;
import cn.powerr.mamabike.bike.entity.BikeLocation;
import cn.powerr.mamabike.bike.entity.Point;
import cn.powerr.mamabike.bike.service.BikeGeoService;
import cn.powerr.mamabike.bike.service.BikeService;
import cn.powerr.mamabike.common.response.ApiResult;
import cn.powerr.mamabike.common.response.CodeMsg;
import cn.powerr.mamabike.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("bike")
@RestController
public class BikeController extends BaseController {

    @Autowired
    private BikeGeoService bikeGeoService;
    @Autowired
    private BikeService bikeService;

    /**
     * 查询500米范围内可用单车
     *
     * @param point 安卓端上传用户所处经纬度
     * @return
     */
    @RequestMapping("findAroundBike")
    public ApiResult<List<BikeLocation>> findAroundBike(Point point) {
        List<BikeLocation> bikeLocations = bikeGeoService.nearBikes("bike_location", "location", point,
                0, 500, null, null, 20);
        return ApiResult.success(bikeLocations);
    }

    /**
     * 解锁单车（用户扫码，调用单车解锁接口）
     * 1.推送单车进行解锁
     * 2.解锁成功->插入单车骑行表记录，开始计时；否则返回单车解锁失败给用户
     * 3.将Mongodb中单车状态置为2，其他用户不可扫描
     *
     * @param bike
     * @return
     */
    @RequestMapping("unLockBike")
    public ApiResult<Boolean> unLockBike(@RequestBody Bike bike) {
        boolean pushSucc = bikeService.unLockBike(getCurrentUser(), bike.getNumber());
        if (pushSucc) {
            return ApiResult.success(true);
        }
        return ApiResult.error(CodeMsg.BIKE_UNLOCK_FAIL);
    }


    /**
     * 用户手动搬动单车锁进行锁车
     * 单车锁车并向服务器（接口）发送锁车信息
     * 只能获取单车信息
     * @param location
     * @return
     */
    @RequestMapping("lockBike")
    public ApiResult<Boolean> lockBike(@RequestBody BikeLocation location) {
        boolean pushSucc = bikeService.lockBike( location);
        if (pushSucc) {
            return ApiResult.success(true);
        }
        return ApiResult.error(CodeMsg.BIKE_LOCK_FAIL);
    }

    @RequestMapping("reportLocation")
    public ApiResult<Boolean> reportLocation(@RequestBody BikeLocation bikeLocation){
        boolean succ = bikeService.reportLocation(bikeLocation);
        if(!succ){
            return ApiResult.error(CodeMsg.BIKE_REPORT_FAIL);
        }
        return ApiResult.success(true);
    }
}
