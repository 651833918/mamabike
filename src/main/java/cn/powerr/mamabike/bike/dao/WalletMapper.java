package cn.powerr.mamabike.bike.dao;

import cn.powerr.mamabike.bike.entity.Wallet;
import org.apache.ibatis.annotations.Param;

public interface WalletMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Wallet record);

    int insertSelective(Wallet record);

    Wallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Wallet record);

    int updateByPrimaryKey(Wallet record);

    Wallet selectByUserId(@Param("userId")Long userId);
}