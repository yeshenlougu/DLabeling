package com.dlabeling.system.domain.vo;

import com.dlabeling.common.enums.Gender;
import com.dlabeling.common.enums.UserRole;
import com.dlabeling.system.domain.po.user.UserInfo;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/3/5
 */

@Data
public class UserInfoVO {

    private Integer id;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户权限
     */
    private String privilege;

    /**
     * 用户性别
     * 0-无
     * 1-女
     * 2-男
     */
    private String gender;

    /**
     * 用户住址
     */
    private String address;

    public static UserInfo convertToUserInfo(UserInfoVO userInfoVO){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoVO.getId());
        userInfo.setUserId(userInfoVO.getUserId());
        userInfo.setUsername(userInfoVO.getUsername());
        userInfo.setEmail(userInfoVO.getEmail());
        userInfo.setPhone(userInfoVO.getPhone());
        userInfo.setAddress(userInfoVO.getAddress());
        userInfo.setPrivilege(UserRole.getRoleByString(userInfoVO.getPrivilege()).getCode());
        userInfo.setGender(Gender.getGenderByString(userInfoVO.getGender()).getCode());

        return userInfo;
    }
}
