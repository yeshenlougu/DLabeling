package com.dlabeling.framework.security.handle;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.common.utils.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证失败处理类 返回未授权
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Component
public class UnAuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ServletUtils.responseOut(response, R.fail(StringUtils.format("请求访问: {}, 认证失败， 无法访问系统资源", request.getRequestURI())));
    }
}
