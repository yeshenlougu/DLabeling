package com.dlabeling.common.exception;

import com.dlabeling.common.enums.ResponseCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
@Data
public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -3761694894448422039L;
    private ResponseCode code;
    private int errorCode;
    private String msg;

    public BusinessException(ResponseCode code) {
        super(Arrays.stream(ResponseCode.values()).filter(c -> c.getCode()==code.getCode()).findFirst().orElse(ResponseCode.BUSINESS_ERROR).getMessage());
        this.code = code;
        this.msg = super.getMessage();
    }

    public BusinessException(ResponseCode code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = ResponseCode.BUSINESS_ERROR;
        this.msg = msg;
    }
}
