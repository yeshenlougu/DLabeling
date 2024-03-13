package com.dlabeling.system.service.user;

import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.domain.po.user.User;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LevelApplyVO;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
public interface ISysUserService {

    /**
     * 注册用户
     * @param user
     */
    void addUser(User user);
    
    void updateUser(User user);
    
    void updateUserInfo(UserInfo userInfo);
    
    void deleteUser(User user);
    
    UserInfo getUserInfoById(Integer userId);

    List<UserInfo> getUserInfoByDetail(UserInfo userInfo);
    
    List<UserInfo> getAllUserInfo();
    
    User getUserByEmailOrPhone(User user);
    
    User login(User user);

    void addLevelApply(LevelApply levelApply);

    void updateLevelApply(LevelApplyVO levelApplyVO);

    List<LevelApplyVO> getAllLevelApply( String type);

    List<LevelApply> getLevelApplyByStatus(int status);
}
