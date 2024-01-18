package com.dlabeling.common.exception.user;

/**
 * @Description: 验证码错误异常类
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2023/12/9
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
