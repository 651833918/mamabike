package cn.powerr.mamabike.bike.service;

import cn.powerr.mamabike.user.entity.UserElement;

public interface BikeService {
    boolean unLockBike(UserElement currentUser, Long number);

    boolean lockBike(Long number);
}
