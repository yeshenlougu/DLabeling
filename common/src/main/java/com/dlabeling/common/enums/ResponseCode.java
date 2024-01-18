package com.dlabeling.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 相应状态
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
public enum ResponseCode {
    
    SQL_INSERT_ERROR(2001, "数据插入错误"),
    SQL_EXIST_ERROR(2002, "数据以存在"),
    SQL_UPDATE_ERROR(2003, "数据库更新错误"),
    SQL_DELETE_ERROR(2004, "数据库更新错误"),
    BUSINESS_ERROR(9999, "服务器内部错误");




    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessage(int code) {
        ResponseCode responseCode = Arrays.stream(ResponseCode.values()).filter(r -> r.getCode() == code).findFirst().orElse(null);
        if (responseCode != null) {
            return responseCode.getMessage();
        }
        return null;
    }

    public static String getMessage(int code, String msg) {
        ResponseCode responseCode = Arrays.stream(ResponseCode.values()).filter(r -> r.getCode() == code).findFirst().orElse(null);
        if (responseCode != null) {
            if (responseCode.getMessage().contains("%s") && !responseCode.getMessage().equals(msg)) {
                return String.format(responseCode.getMessage(), msg);
            }
            return StringUtils.isNotBlank(msg) ? msg : responseCode.getMessage();
        }
        return null;
    }

    public static ResponseCode getByCode(int code) {
        return Arrays.stream(ResponseCode.values()).filter(r -> r.getCode() == code).findFirst().orElse(BUSINESS_ERROR);
    }

    /**
     * 将所有枚举转换成map<Integer,String>
     */
    public static List<String> getAllToMap() {
        // List<Map<String, String>> list = new ArrayList<>();
        List<String> list = new ArrayList<>();
        // Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        //计数器
        int count = 0;
        List<ResponseCode> responseCodeList = Arrays.stream(ResponseCode.values()).sorted(Comparator.comparingInt(ResponseCode::getCode)).collect(Collectors.toList());
        System.out.println("总条数：" + responseCodeList.size());
        for (ResponseCode responseCode : responseCodeList) {
            if (count <= 2000) {
                // map.put(responseCode.getCode()+"", responseCode.getMessage());
                sb.append(responseCode.getCode()).append("= ").append(responseCode.getMessage()).append(" ; ");
                count += responseCode.getMessage().length();
            } else {
                list.add(sb.substring(0, sb.length() - 1));
                //  map = new HashMap<>();
                sb = new StringBuilder();
                sb.append(responseCode.getCode()).append("= ").append(responseCode.getMessage()).append(" ; ");

                //map.put(responseCode.getCode()+"", responseCode.getMessage());
                count = 0;
            }
        }
        //添加剩下的数据
        list.add(sb.substring(0, sb.length() - 1));
        return list;
    }
}
