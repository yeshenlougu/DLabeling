package com.dlabeling.system.domain.po.user;

import com.dlabeling.common.enums.Gender;
import com.dlabeling.common.enums.UserRole;
import com.dlabeling.system.domain.vo.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -4361859233202249298L;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.createTime = user.getCreateTime();

    }


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
    private Integer privilege;

    /**
     * 用户性别
     * 0-无
     * 1-女
     * 2-男
     */
    private Integer gender;

    /**
     * 用户住址
     */
    private String address;

    /**
     * 创建时间
     */
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除时间
     */
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date destroyTime;

    public static UserInfoVO convertToUserInfoVO(UserInfo userInfo){
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setUserId(userInfo.getUserId());
        userInfoVO.setUsername(userInfo.getUsername());
        userInfoVO.setEmail(userInfo.getEmail());
        userInfoVO.setPhone(userInfo.getPhone());
        userInfoVO.setAddress(userInfo.getAddress());

        UserRole role = UserRole.getRoleByCode(userInfo.getPrivilege());
        userInfoVO.setPrivilege(role != null? role.getRole(): null);
        Gender gender1 = Gender.getGenderByCode(userInfo.getGender());
        userInfoVO.setGender(gender1!=null ? gender1.getSex(): null);

        return userInfoVO;

    }

    public static UserInfo convertToUserInfo(UserInfoVO userInfoVO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userInfoVO.getId());
        userInfo.setUserId(userInfoVO.getUserId());
        userInfo.setUsername(userInfoVO.getUsername());
        userInfo.setEmail(userInfoVO.getEmail());
        userInfo.setPhone(userInfoVO.getPhone());
        userInfo.setAddress(userInfoVO.getAddress());

        userInfo.setPrivilege(UserRole.getRoleByString(userInfoVO.getPrivilege())!=null?UserRole.getRoleByString(userInfoVO.getPrivilege()).getCode():null);
        userInfo.setGender(Gender.getGenderByString(userInfoVO.getGender())!=null ?Gender.getGenderByString(userInfoVO.getGender()).getCode():null);
        return userInfo;
    }
}
