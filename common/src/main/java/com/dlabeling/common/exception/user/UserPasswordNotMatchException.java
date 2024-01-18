package com.dlabeling.common.exception.user;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
public class UserPasswordNotMatchException extends UserException{

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }
}
