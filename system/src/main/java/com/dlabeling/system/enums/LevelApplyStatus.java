package com.dlabeling.system.enums;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/2
 */
public enum LevelApplyStatus {
    APPLYING(0, "申请中"), AGREE(1, "已批准"), REJECT(2, "已拒绝");

    private int code;

    private String msg;

    LevelApplyStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static LevelApplyStatus getLevelApplyStatusByMsg(String msg){
        return Stream.of(LevelApplyStatus.values())
                .filter(e->e.msg.equals( msg))
                .findFirst()
                .orElse(null);
    }

    public static LevelApplyStatus getLevelApplyStatusByCode(Integer code){
        return Stream.of(LevelApplyStatus.values())
                .filter(e->e.code == code)
                .findFirst()
                .orElse(null);
    }
}
