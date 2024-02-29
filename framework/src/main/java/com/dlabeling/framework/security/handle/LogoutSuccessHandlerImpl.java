package com.dlabeling.framework.security.handle;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.framework.web.TokenService;
import com.dlabeling.system.domain.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    
    @Autowired
    private TokenService tokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)){
            tokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtils.responseOut(response, R.ok("退出成功"));

    }
}
