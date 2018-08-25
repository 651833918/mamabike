package cn.powerr.mamabike.common.exception;

import cn.powerr.mamabike.common.response.CodeMsg;

/**
 * @author bengang
 * @date@time 2018/8/24 16:30
 * @description RuntimeException -> uncheckedException
 *               checkedException E
 */
public class MaMaBikeException extends RuntimeException {

    private CodeMsg codeMsg;

    public MaMaBikeException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg = codeMsg;
    }

    public int getStatus() {
        return codeMsg.getCode();
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
