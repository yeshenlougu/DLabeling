package com.dlabeling.framework.security.context;

import org.springframework.security.core.Authentication;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
public class AuthenticationContextHolder {

    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext(){
        return contextHolder.get();
    }
    
    public static void setContext(Authentication context){
        contextHolder.set(context);
    }
    
    public static void clearContext(){
        contextHolder.remove();
    }
}
