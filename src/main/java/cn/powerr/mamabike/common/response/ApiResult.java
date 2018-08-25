package cn.powerr.mamabike.common.response;

import lombok.Data;

@Data
public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    public ApiResult(){
    }

    public ApiResult(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ApiResult(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.message = msg;
    }

    public static <T> ApiResult<T> success(T data,CodeMsg codeMsg) {
        return new ApiResult<>(data,codeMsg.getCode(),codeMsg.getMsg());
    }


    /**
     * 默认CodeMsg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data, CodeMsg.RESP_STATUS_OK.getCode(), CodeMsg.RESP_STATUS_OK.getMsg());
    }

    public static <T> ApiResult<T> error(CodeMsg codeMsg) {
        if (codeMsg != null) {
            return new ApiResult<>(codeMsg.getCode(), codeMsg.getMsg());
        }
        return new ApiResult<>(CodeMsg.RESP_STATUS_INTERNAL_ERROR.getCode(), CodeMsg.RESP_STATUS_INTERNAL_ERROR.getMsg());
    }
}
