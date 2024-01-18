package com.dlabeling.common.exception.user;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
public class UserNameIllegalException extends UserException{
    public UserNameIllegalException() {
        super("user.name.illegal", null);
    }
}
