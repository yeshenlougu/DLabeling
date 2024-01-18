package com.dlabeling.common.exception.user;

/**
 * @Description: 黑名单IP异常类
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2023/12/9
 */
public class BlackListException extends UserException{

    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
