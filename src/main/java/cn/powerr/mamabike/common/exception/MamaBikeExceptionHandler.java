package cn.powerr.mamabike.common.exception;

import cn.powerr.mamabike.common.response.ApiResult;
import cn.powerr.mamabike.common.response.CodeMsg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bengang
 * @date@time 2018/8/24 16:00
 * @description 全局异常处理
 */
@ControllerAdvice
@RestController
public class MamaBikeExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ApiResult<String> mamabikeExceptionHandler(Exception e) {
        if (e instanceof MaMaBikeException) {
            e.printStackTrace();
            MaMaBikeException ex = (MaMaBikeException) e;
            return ApiResult.error(ex.getCodeMsg());
        } else {
            return ApiResult.error(CodeMsg.RESP_STATUS_INTERNAL_ERROR);
        }
    }
}
