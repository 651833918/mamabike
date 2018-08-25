package cn.powerr.mamabike.bike.dao;

import cn.powerr.mamabike.bike.entity.Bike;
import cn.powerr.mamabike.bike.entity.BikeNoGen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bike record);

    int insertSelective(Bike record);

    Bike selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bike record);

    int updateByPrimaryKey(Bike record);

    int generateBike(@Param("bikeNoGen") BikeNoGen bikeNoGen);

    Bike selectByBikeNo(@Param("bikeNo")Long bikeNo);
}