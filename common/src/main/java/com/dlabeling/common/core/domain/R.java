package com.dlabeling.common.core.domain;

import com.dlabeling.common.constant.HttpStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 响应信息主体
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = -4732905712099958563L;
    
    public static final int SUCCESS = HttpStatus.SUCCESS;
    
    public static final int FAIL = HttpStatus.ERROR;
    
    private int code;
    
    private String msg;
    
    private T data;
    
    public static <T> R<T> ok(){
        return restResult(null, SUCCESS, "操作成功");
    }
    
    public static <T> R<T> ok(T data){
        return restResult(data, SUCCESS, "操作成功");
    }
    
    public static <T> R<T> ok(String msg){
        return restResult(null, SUCCESS, msg);
    }
    
    public static <T> R<T> ok(T data, String msg){
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }
    

    private static <T> R<T> restResult(T data, int code, String msg)
    {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
