package com.dlabeling.system.domain.vo;

import com.dlabeling.common.enums.Gender;
import com.dlabeling.common.enums.UserRole;
import com.dlabeling.system.domain.po.user.UserInfo;
import lombok.Data;

import java.util.Date;
import java.util.Map;

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

    private String password;

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

    public static UserInfoVO convertToUserInfoVO(UserInfo userInfo){
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setUserId(userInfo.getUserId());
        userInfoVO.setUsername(userInfo.getUsername());
        userInfoVO.setEmail(userInfo.getEmail());
        userInfoVO.setPhone(userInfo.getPhone());
        userInfoVO.setAddress(userInfo.getAddress());
        UserRole role = UserRole.getRoleByCode(userInfo.getPrivilege());
        userInfoVO.setPrivilege(role != null ? role.getRole() : null);

        Gender gender1 = Gender.getGenderByCode(userInfo.getGender());
        userInfoVO.setGender(gender1 != null ? gender1.getSex() : null);

        return userInfoVO;
    }

    public static UserInfoVO convertMapToUserInfoVO(Map<String, Object> userInfoVOMap) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId((Integer) userInfoVOMap.get("id"));
        userInfoVO.setPrivilege((String) userInfoVOMap.get("privilege"));
        userInfoVO.setGender((String) userInfoVOMap.get("gender"));
        userInfoVO.setEmail((String) userInfoVOMap.get("email"));
        userInfoVO.setPrivilege((String) userInfoVOMap.get("phone"));
        userInfoVO.setAddress((String) userInfoVOMap.get("address"));
        userInfoVO.setUserId((Integer) userInfoVOMap.get("userId"));
        userInfoVO.setUsername((String) userInfoVOMap.get("username"));
        return userInfoVO;
    }
}
