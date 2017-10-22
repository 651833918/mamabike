package cn.powerr.mamabike.common.exception;

import cn.powerr.mamabike.common.constant.Constants;

public class MaMaBikeException extends Exception {
    public MaMaBikeException(String message) {
        super(message);
    }

    public int getStatus() {
        return Constants.RESP_STATUS_INTERNAL_ERROR;
    }
}
