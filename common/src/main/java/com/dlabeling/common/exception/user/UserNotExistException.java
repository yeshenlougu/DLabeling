package com.dlabeling.common.exception.user;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
public class UserNotExistException extends UserException {

    public UserNotExistException()
    {
        super("user.not.exists", null);
    }
}
