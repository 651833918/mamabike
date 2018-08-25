package cn.powerr.mamabike.user.controller;

import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.common.response.ApiResult;
import cn.powerr.mamabike.common.response.CodeMsg;
import cn.powerr.mamabike.common.rest.BaseController;
import cn.powerr.mamabike.user.entity.TransferInfo;
import cn.powerr.mamabike.user.entity.User;
import cn.powerr.mamabike.user.entity.UserElement;
import cn.powerr.mamabike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("user")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@RequestBody TransferInfo transferInfo) {
        String data = transferInfo.getData();
        String key = transferInfo.getKey();
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            throw new MaMaBikeException(CodeMsg.ILLEGAL_ARGUMENT_ERROR);
        }
        String token = userService.login(data, key);
        return ApiResult.success(token);
    }

    /**
     * 修改昵称
     * @param user
     * @return
     */
    @RequestMapping(value = "/modifyNickName")
    public ApiResult<Boolean> modifyNickname(@RequestBody User user) {
        UserElement ue = getCurrentUser();
        user.setId(ue.getUserId());
        userService.modifyNickname(user);
        return ApiResult.success(true);
    }


    /**
     * 发送验证码
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendVercode")
    public ApiResult<Boolean> sendVercode(@RequestBody User user, HttpServletRequest request) {
        userService.sendVercode(user.getMobile(), getIpFromRequest(request));
        return ApiResult.success(true);
    }

}
