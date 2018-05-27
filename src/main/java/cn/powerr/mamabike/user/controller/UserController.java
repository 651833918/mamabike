package cn.powerr.mamabike.user.controller;

import cn.powerr.mamabike.common.constant.Constants;
import cn.powerr.mamabike.common.exception.MaMaBikeException;
import cn.powerr.mamabike.common.response.ApiResult;
import cn.powerr.mamabike.common.rest.BaseController;
import cn.powerr.mamabike.user.entity.LoginInfo;
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
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> login(@RequestBody LoginInfo loginInfo) {
        ApiResult<String> result = new ApiResult<>();
        try {
            String data = loginInfo.getData();
            String key = loginInfo.getKey();
            if(StringUtils.isBlank(data)||StringUtils.isBlank(key)){
                throw new MaMaBikeException("参数校验异常");
            }
            String token = userService.login(data, key);
            result.setData(token);
        } catch (MaMaBikeException e) {
            result.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            result.setMessage(e.getMessage());
        } catch (Exception e){
            result.setMessage("内部错误");
            result.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
        }
        return result;
    }

    /**
     * 修改昵称
     * @param user
     * @return
     */
    @RequestMapping(value = "/modifyNickName")
    public ApiResult modifyNickname(@RequestBody User user) {
        ApiResult resp = new ApiResult();
        try{
            UserElement ue = getCurrentUser();
            user.setId(ue.getUserId());
            userService.modifyNickname(user);
        }catch (MaMaBikeException e) {
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to modifyNickname", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }


    /**
     *
     * @param user
     * @param request 通过拿到ip地址限制发送次数
     * @return
     */
    @RequestMapping(value = "/sendVercode")
    public ApiResult sendVercode(@RequestBody User user, HttpServletRequest request) {
        ApiResult resp = new ApiResult();
        try{
            userService.sendVercode(user.getMobile(),getIpFromRequest(request));
        }catch (MaMaBikeException e) {
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("Fail to send sms vercode", e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }
        return resp;
    }

}
