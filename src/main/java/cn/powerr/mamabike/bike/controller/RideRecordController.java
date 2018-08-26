package cn.powerr.mamabike.bike.controller;

import cn.powerr.mamabike.bike.entity.RideContrail;
import cn.powerr.mamabike.bike.entity.RideRecord;
import cn.powerr.mamabike.bike.service.BikeGeoService;
import cn.powerr.mamabike.bike.service.RideRecordService;
import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.common.response.ApiResult;
import cn.powerr.mamabike.common.response.CodeMsg;
import cn.powerr.mamabike.common.rest.BaseController;
import cn.powerr.mamabike.user.entity.UserElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("rideRecord")
@RestController
public class RideRecordController extends BaseController {

    @Autowired
    private RideRecordService rideRecordService;
    @Autowired
    private BikeGeoService bikeGeoService;

    @RequestMapping("/list/{id}")
    public ApiResult<List<RideRecord>> listRideRecord(@PathVariable("id")Long lastId){
        UserElement ue = getCurrentUser();
        List<RideRecord> list;
        if (ue != null){
            list = rideRecordService.listRideRecord(ue.getUserId(),lastId);
        }else {
            throw new MaMaBikeException(CodeMsg.NON_LOGIN);
        }
        return ApiResult.success(list);
    }


    @RequestMapping("/contrail/{recordNo}")
    public ApiResult<RideContrail> showRideRecord(@PathVariable("recordNo") String recordNo){
        RideContrail oneRecord = bikeGeoService.showOneRecord(recordNo);
        return ApiResult.success(oneRecord);
    }
}
