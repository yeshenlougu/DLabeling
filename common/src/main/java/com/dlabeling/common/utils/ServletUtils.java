package com.dlabeling.common.utils;

import com.dlabeling.common.constant.HttpStatus;
import com.dlabeling.common.core.domain.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
public class ServletUtils {
    
    public static void responseOut(HttpServletResponse response, R r){
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.SUCCESS);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getWriter(), r);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            // 获取HttpServletRequest对象
            HttpServletRequest request = attributes.getRequest();
            return request;
        }
        return null;
    }
}
