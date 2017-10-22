package cn.powerr.mamabike.common.response;

import cn.powerr.mamabike.common.constant.Constants;
import lombok.Data;

@Data
public class ApiResult <T> {
    private int code = Constants.RESP_STATUS_OK;
    private String message;
    private T data;
}
