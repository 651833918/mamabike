package cn.powerr.mamabike.user.service;

import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.user.entity.User;

public interface UserService {
    String login(String data, String key) throws MaMaBikeException;

    void modifyNickname(User user) throws MaMaBikeException;

    void sendVercode(String mobile, String ip)throws MaMaBikeException;
}
