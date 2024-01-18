package com.dlabeling.system.service.user.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.system.service.user.ISysUserService;
import com.dlabeling.db.domain.po.user.User;
import com.dlabeling.db.domain.po.user.UserInfo;
import com.dlabeling.db.mapper.user.UserInfoMapper;
import com.dlabeling.db.mapper.user.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */

@Slf4j
@Service
public class ISysUserServiceImpl implements ISysUserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    
    @Override
    @Transactional
    public void addUser(User user) {
        
        // TODO 进行参数校验
        
        User selectUser = userMapper.selectUser(user);
        if (selectUser == null){
            try{
                userMapper.addUser(user);
                User insertedUser = userMapper.selectUser(user);
                UserInfo userInfo = new UserInfo(insertedUser);
                userInfoMapper.addUserInfo(userInfo);
            }catch (Exception e){
                log.error(e.getMessage(), e);
                throw new BusinessException(ResponseCode.SQL_INSERT_ERROR, "创建用户失败");
            }
            
        }else {
            String msg = "手机号或邮箱以注册，请找回密码或更改手机号或邮箱再注册";
            log.error(msg);
            throw new BusinessException(ResponseCode.SQL_EXIST_ERROR, msg);
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        try{
            userMapper.updateUser(user);
            userInfoMapper.updateById(new UserInfo(user));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(ResponseCode.SQL_UPDATE_ERROR, "用户更新失败");
        }
        
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userInfoMapper.updateUserInfo(userInfo);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        try{
            userMapper.deleteUser(user);
            userInfoMapper.deleteUserInfo(new UserInfo(user));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(ResponseCode.SQL_DELETE_ERROR, "用户删除失败");
        }
    }
    
    @Override
    public UserInfo getUserInfoById(Integer userId){
        UserInfo userInfoById = userInfoMapper.getUserInfoById(userId);
        
        return userInfoById;
    }
    
    @Override
    public List<UserInfo> getUserInfoByDetail(UserInfo userInfo){
        List<UserInfo> userInfoByDetail = userInfoMapper.getUserInfoByDetail(userInfo);
        
        return userInfoByDetail;
    }
    
    @Override
    public List<UserInfo> getAllUserInfo(){
        List<UserInfo> allUserInfo = userInfoMapper.getAllUserInfo();
        return allUserInfo;
    }
    
    @Override
    public User getUserByEmailOrPhone(User user){
        User selectUser = userMapper.selectUser(user);
        return selectUser;
    }
    
    @Override
    public User login(User user){
        User selectUser = userMapper.selectUser(user);
        return selectUser;
    }
    
}
