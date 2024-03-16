package com.dlabeling.common.enums;

import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
public enum UserRole {
    ADMINISTRATOR(0, "ADMIN", "管理员，拥有所有权限"),
    LABELER(1, "LABELER", "标注员，只有标注权限"),
    TRAINER(2, "TRAINER", "训练员，拥有标注和训练权限");


    private int code;
    
    private String role;
    
    private String description;

    UserRole(int code, String role, String description) {
        this.code = code;
        this.role = role;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
    
    public static UserRole getRoleByCode(int code){
        return Stream.of(UserRole.values())
                .filter(e->e.code == code)
                .findFirst()
                .orElse(null);
    }

    public static UserRole getRoleByString(String msg){
        return Stream.of(UserRole.values())
                .filter(e->e.role.equals(msg))
                .findFirst()
                .orElse(null);
    }
}
