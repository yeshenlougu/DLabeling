package com.dlabeling.common.exception.user;

import com.dlabeling.common.exception.base.BaseException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
public class UserException extends BaseException {
    
    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }

    public UserException(String msg){
        super(msg);
    }
}
