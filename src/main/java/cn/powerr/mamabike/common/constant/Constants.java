package cn.powerr.mamabike.common.constant;

public class Constants {
    /**
     * 自定义状态码开始
     */
    public static final int RESP_STATUS_OK = 200;

    public static final int RESP_STATUS_NOAUTH = 401;

    public static final int RESP_STATUS_INTERNAL_ERROR = 500;

    public static final int RESP_STATUS_BADREQUEST = 400;
    /**
     * 自定义状态码结束
     */
    public static final String REQUEST_VERSION_KEY = "version";
    public static final String REQUEST_TOKEN_KEY = "user-token";

    /**
     * 秒滴认证开始
     **/
    public static final String MDSMS_AUTH_TOKEN = "735c7a5090074c42b217bd4fc65d6425";
    public static final String MDSMS_ACCOUNT_SID = "5f46ae51f1574dc59d5880203e4e8f30";
    public static final String MDSMS_REST_URL = "https://api.miaodiyun.com/20150822";
    public static final String MDSMS_VERCODE_TPLID = "93565687";
    /**秒滴认证结束**/

    /**七牛云存储key**/
    public static final String QINIU_ACCESS_KEY = "uwNmKMge2w4EUXL3P7FSRF2eHkhmYSCx9AGOQQIT";
    public static final String QINIU_SECRET_KEY = "Pgrs2LIx6tWqakjnnJrSvtycjJlYKBeVLYnfoUug";
    public static final String QINIU_HEAD_IMG_BUCKET_NAME = "mamabike";
    public static final String QINIU_HEAD_IMG_BUCKET_URL = "oyihp3q7n.bkt.clouddn.com";
    /**七牛云存储key结束**/
}
