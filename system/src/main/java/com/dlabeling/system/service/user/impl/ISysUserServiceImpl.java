package com.dlabeling.system.service.user.impl;

import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.system.constant.LevelApplyStatus;
import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.mapper.LevelApplyMapper;
import com.dlabeling.system.service.user.ISysUserService;
import com.dlabeling.system.domain.po.user.User;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.mapper.user.UserInfoMapper;
import com.dlabeling.system.mapper.user.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
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
    private LevelApplyMapper levelApplyMapper;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    
    
    @Override
    @Transactional
    public void addUser(User user) {
        
        // TODO 进行参数校验
        String pwd = bCryptPasswordEncoder.encode(user.getPassword());
        log.debug(pwd);
        //进行密码加密
        user.setPassword(null);
        User selectUser = userMapper.selectUser(user);

        if (selectUser == null){
            try{
                user.setPassword(pwd);
                user.setCreateTime(new Date());
                log.info(user.toString());
                userMapper.addUser(user);
                User insertedUser = userMapper.selectUser(user);
                log.info(insertedUser.toString());
                UserInfo userInfo = new UserInfo(insertedUser);
                log.info(userInfo.toString());
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
            if (StringUtils.isNotEmpty(user.getPassword())){
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            if (!StringUtils.isNotNull(user.getId())){
                throw new BusinessException(ResponseCode.USER_UPDATE_FAIL, "缺少userID");
            }

            userMapper.updateUser(user);
            userInfoMapper.updateUserInfo(new UserInfo(user));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new BusinessException(ResponseCode.SQL_UPDATE_ERROR, "用户更新失败");
        }
        
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateUserInfo(userInfo);

        User user = new User();
        user.setId(userInfo.getUserId());
        user.setUsername(userInfo.getUsername());
        user.setPhone(userInfo.getPhone());
        user.setUpdateTime(userInfo.getUpdateTime());
        userMapper.updateUser(user);
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

    @Override
    public void addLevelApply(LevelApply levelApply) {
        levelApply.setCreateTime(new Date());
        levelApply.setStatus(LevelApplyStatus.APPLYING.getCode());
        levelApplyMapper.addLevelApply(levelApply);
    }

    @Override
    @Transactional
    public void updateLevelApply(LevelApply levelApply) {
        try {
            levelApply.setUpdateTime(new Date());
            levelApplyMapper.updateLevelApply(levelApply);
            // 更新userInfo
            if (levelApply.getStatus() == LevelApplyStatus.AGREE.getCode()){
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(levelApply.getApplyer());
                userInfo.setPrivilege(levelApply.getPrivilege());
                userInfo.setUpdateTime(levelApply.getUpdateTime());
                userInfoMapper.updateUserInfo(userInfo);
            }
        }catch (Exception e){
            throw new BusinessException(ResponseCode.SQL_UPDATE_ERROR, "权限更新失败");
        }

    }

    @Override
    public List<LevelApply> getAllLevelApply() {
        List<LevelApply> allLevelApply = levelApplyMapper.getAllLevelApply();
        return allLevelApply;
    }

    @Override
    public List<LevelApply> getLevelApplyByStatus(int status) {
        List<LevelApply> levelApplyByStatus = levelApplyMapper.getLevelApplyByStatus(status);
        return levelApplyByStatus;
    }
}
