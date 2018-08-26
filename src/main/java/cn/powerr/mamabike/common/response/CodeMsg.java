package cn.powerr.mamabike.common.response;

public enum CodeMsg {

    //---------------- 系统标识 ----------------
    RESP_STATUS_OK(200, "成功"),
    RESP_STATUS_INTERNAL_ERROR(500, "内部错误"),
//    RSA_DECRIPT_ERROR(500700,"RSA解密失败"),

    //---------------- 用户 ----------------
    USER_NON_VERIFY(500501, "用户未实名认证"),
    USER_RIDING(500502, "用户已扫码单车"),
    USER_NON_DEPOSIT(500503, "用户未缴付押金"),
    USER_NON_REMAIN(500504, "用户余额不足一元"),
    NON_LOGIN(500505, "用户未登录"),

    ILLEGAL_ARGUMENT_ERROR(500500, "非法参数"),

    //---------------- 登录错误 ----------------
    LOGIN_ERROR(500400, "用户登录失败"),

    //---------------- 验证码异常 ----------------
    VERCODE_IN_EXPIRE(500600, "当前验证码未过期,请稍后重试"),
    VERCODE_VALIDATE_ERROR(500601, "手机号验证码不匹配"),
    VERCODE_DAY_SIZE(500602, "手机号超过当日验证码次数"),
    IP_DAY_SIZE(500603, "ip超过当日验证码次数"),

    //---------------- 单车状态 ----------------
    RIDING_RECORD_NON(500203, "骑行记录不存在"),
    BIKE_REPORT_FAIL(500201, "单车上报坐标失败"),
    BIKE_LOCK_FAIL(500202, "单车锁车失败"),
    BIKE_UNLOCK_FAIL(500200, "单车解锁失败");

    private final int code;
    private final String msg;


    public int getCode() {
        return code;
    }

    CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
