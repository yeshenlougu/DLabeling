package com.dlabeling.system.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.system.domain.po.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
@Mapper
public interface  UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 创建用户信息
     * @param userInfo
     * @return
     */
    int addUserInfo(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    int updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户信息
     * @param userInfo
     * @return
     */
    int deleteUserInfo(UserInfo userInfo);
    
    /**
     * 根据id批量查询用户信息
     */
    List<UserInfo> fetchBatchUserInfoById(List<Integer> uuidList);
    /**
     * 根据id查询用户信息
     * @param userInfo
     * @return
     */
    UserInfo getUserInfoById(Integer id);
    
    /**
     * 获取所有用户信息
     * @return
     */
    List<UserInfo> getAllUserInfo();

    /**
     * 根据字段查询用户信息
     * @param userInfo
     * @return
     */
    List<UserInfo> getUserInfoByDetail(UserInfo userInfo);
    
    
}
