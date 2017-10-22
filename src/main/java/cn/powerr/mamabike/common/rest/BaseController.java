package cn.powerr.mamabike.common.rest;

import cn.powerr.mamabike.cache.CommonCacheUtil;
import cn.powerr.mamabike.common.constant.Constants;
import cn.powerr.mamabike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseController {
    @Autowired
    private CommonCacheUtil cacheUtil;

    protected UserElement getCurrentUser() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (!StringUtils.isBlank(token)) {
            try {
                UserElement ue = cacheUtil.getUserByToken(token);
                return ue;
            } catch (Exception e) {
                log.error("fail to get user by token", e);
                throw e;
            }

        }
        return null;
    }
}
