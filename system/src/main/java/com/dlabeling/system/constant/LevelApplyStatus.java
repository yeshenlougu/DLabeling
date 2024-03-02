package com.dlabeling.system.constant;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/2
 */
public enum LevelApplyStatus {
    APPLYING(0, "申请中"), AGREE(1, "已批准"), REJECT(2, "以拒绝");

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
}
