package com.dlabeling.framework.web;

import com.dlabeling.common.constant.CacheConstants;
import com.dlabeling.common.core.redis.RedisCache;
import com.dlabeling.common.exception.ServiceException;
import com.dlabeling.common.exception.user.CaptchaException;
import com.dlabeling.common.exception.user.CaptchaExpireException;
import com.dlabeling.common.exception.user.UserPasswordNotMatchException;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.framework.security.context.AuthenticationContextHolder;
import com.dlabeling.system.domain.vo.LoginUser;
import com.dlabeling.system.service.user.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Service
public class SysLoginService {
    
    @Autowired
    private TokenService tokenService;
    
    @Resource
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService iSysUserService;
    
    @Value("${captcha.enable}")
    private boolean captchaEnabled;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param captchaUUID 验证码标识
     * @return 结果
     */
    public String login(String username, String password, String code, String captchaUUID){
        // 验证码开关
        if (captchaEnabled){
            validateCaptcha(code, captchaUUID);
        }        
        
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 存到本地线程中
            AuthenticationContextHolder.setContext(usernamePasswordAuthenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (Exception e){
            if (e instanceof BadCredentialsException){
                throw new UserPasswordNotMatchException();
            }
            else {
                throw new ServiceException(e.getMessage());
            }
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        return tokenService.createToken(loginUser);
    }
    
    
    private void validateCaptcha(String code, String captchaUUID){
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(captchaUUID, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null){
            throw new CaptchaExpireException();
        }
        if (! code.equalsIgnoreCase(captcha)){
            throw new CaptchaException();
        }
    }
}
