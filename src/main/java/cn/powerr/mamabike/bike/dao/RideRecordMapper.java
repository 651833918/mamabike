package cn.powerr.mamabike.bike.dao;

import cn.powerr.mamabike.bike.entity.RideRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

@Mapper
public interface RideRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RideRecord record);

    int insertSelective(RideRecord record);

    RideRecord selectByPrimaryKey(Long id);
    RideRecord selectByStatus(@Param("userId") Long userId,@Param("status")byte status);

    int updateByPrimaryKeySelective(RideRecord record);

    int updateByPrimaryKey(RideRecord record);

    RideRecord selectByBikeNoOnGoing(@Param("bikeNo")Long bikeNo);
}