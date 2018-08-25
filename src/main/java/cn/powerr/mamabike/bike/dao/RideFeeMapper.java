package cn.powerr.mamabike.bike.dao;

import cn.powerr.mamabike.bike.entity.RideFee;

public interface RideFeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RideFee record);

    int insertSelective(RideFee record);

    RideFee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RideFee record);

    int updateByPrimaryKey(RideFee record);

    RideFee selectByType(Byte type);
}