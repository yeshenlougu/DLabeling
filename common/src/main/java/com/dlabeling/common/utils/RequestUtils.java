package com.dlabeling.common.utils;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import okhttp3.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/18
 */
public class RequestUtils {

    public static Response sendLabelFile(String url, File file){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            return response;
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.BUSINESS_ERROR, "标注失败");
        }

    }
}
