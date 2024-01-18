package com.dlabeling.framework.security.handle;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.framework.web.TokenService;
import com.dlabeling.db.domain.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Component
public class LogoutSuccessHandler implements LogoutHandler {
    
    @Autowired
    private TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)){
            tokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtils.responseOut(response, R.ok("退出成功"));
        
    }
}
