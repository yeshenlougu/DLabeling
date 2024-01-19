package com.dlabeling.framework.web;

import com.dlabeling.common.constant.CacheConstants;
import com.dlabeling.common.core.redis.RedisCache;
import com.dlabeling.common.exception.user.UserPasswordNotMatchException;
import com.dlabeling.common.exception.user.UserPasswordRetryLimitExceedException;
import com.dlabeling.common.utils.SecurityUtils;
import com.dlabeling.framework.security.context.AuthenticationContextHolder;
import com.dlabeling.system.domain.po.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Component
public class SysPasswordService {

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;
    
    @Autowired
    RedisCache redisCache;
    
    public void validate(User user){
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        //查看密码次数连续错误
        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
        if (retryCount == null){
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount)){
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }else {
            clearLoginRecordCache(username);
        }
        
    }

    public boolean matches(User user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisCache.hasKey(getCacheKey(loginName)))
        {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }
}
