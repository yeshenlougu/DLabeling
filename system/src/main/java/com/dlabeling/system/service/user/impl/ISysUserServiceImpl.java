package com.dlabeling.system.service.user.impl;

import com.dlabeling.common.enums.Gender;
import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.enums.UserRole;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.user.UserException;
import com.dlabeling.common.exception.user.UserNameIllegalException;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.system.domain.po.DatasetPermission;
import com.dlabeling.system.domain.vo.LevelApplyVO;
import com.dlabeling.system.domain.vo.UserInfoVO;
import com.dlabeling.system.enums.LevelApplyStatus;
import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.enums.LevelApplyType;
import com.dlabeling.system.mapper.DatasetPermissionMapper;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private DatasetPermissionMapper datasetPermissionMapper;

    
    
    @Override
    @Transactional
    public void addUser(User user) {
        if(StringUtils.isEmail(user.getUsername())){
            user.setEmail(user.getUsername());
        } else if (StringUtils.isPhone(user.getUsername())) {
            user.setPhone(user.getUsername());
        }else {
            throw new UserException("用户名格式错误");
        }

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
            throw new UserException(msg);
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
    public void updateUserInfo(UserInfoVO userInfoVO) {

        UserInfo userInfo = UserInfoVO.convertToUserInfo(userInfoVO);
        userInfo.setUpdateTime(new Date());
        userInfoMapper.updateUserInfo(userInfo);

        User user = new User();
        user.setId(userInfoVO.getUserId());
        user.setUsername(userInfoVO.getUsername());
        user.setPhone(userInfoVO.getPhone());
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
        if (StringUtils.isEmail(user.getUsername())){
            user.setEmail(user.getUsername());
        }else if (StringUtils.isPhone(user.getUsername())){
            user.setPhone(user.getUsername());
        }else {
            throw new UserNameIllegalException();
        }
        User selectUser = userMapper.selectUser(user);
        return selectUser;
    }
    
    @Override
    public User login(User user){
        User selectUser = userMapper.selectUser(user);
        return selectUser;
    }

    @Override
    public void addLevelApply(LevelApplyVO levelApplyVO) {
        try {
            LevelApply levelApply = LevelApplyVO.convertToLevelApply(levelApplyVO);
            levelApply.setCreateTime(new Date());
            levelApply.setStatus(LevelApplyStatus.APPLYING.getCode());
            levelApplyMapper.addLevelApply(levelApply);
        }catch (Exception e){
            throw new BusinessException(ResponseCode.SQL_INSERT_ERROR, "申请已提交");
        }

    }

    @Override
    @Transactional
    public void updateLevelApply(LevelApplyVO levelApplyVO) {
        try {
            LevelApply levelApply = LevelApplyVO.convertToLevelApply(levelApplyVO);
            levelApply.setUpdateTime(new Date());
            if (levelApplyVO.getApproval()){
                levelApply.setStatus(LevelApplyStatus.AGREE.getCode());
            }else {
                levelApply.setStatus(LevelApplyStatus.REJECT.getCode());
            }
            levelApplyMapper.updateLevelApply(levelApply);
            // 更新userInfo


            if (levelApply.getStatus() == LevelApplyStatus.AGREE.getCode()){
                if (levelApply.getType() == LevelApplyType.USER_APPLY.getCode()){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(levelApply.getApplyer());
                    userInfo.setPrivilege(levelApply.getPrivilege());
                    userInfo.setUpdateTime(levelApply.getUpdateTime());
                    userInfoMapper.updateUserInfo(userInfo);
                }else if (levelApply.getType() == LevelApplyType.DATASET_APPLY.getCode()){
                    DatasetPermission datasetPermission = new DatasetPermission();
                    datasetPermission.setDatasetId(levelApply.getPrivilege());
                    datasetPermission.setUserId(levelApply.getApplyer());
                    datasetPermissionMapper.addDatasetPermission(datasetPermission);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException(ResponseCode.SQL_UPDATE_ERROR, "权限更新失败");
        }

    }

    @Deprecated
    @Override
    public List<LevelApplyVO> getAllLevelApply(String type) {
        Integer typeNum = LevelApplyType.getLevelApplyTypeByType(type).getCode();
        List<LevelApply> allLevelApply = levelApplyMapper.getAllLevelApply(typeNum);

        List<LevelApplyVO> levelApplyVOList = allLevelApply.stream().map(LevelApplyVO::converToLevelApplyVO).collect(Collectors.toList());

        Set<Integer> userIdList = new HashSet<>();
        allLevelApply.stream().map(levelApply->{
            userIdList.add(levelApply.getApplyer());
            userIdList.add(levelApply.getJudger());
            return null;
        });
        List<UserInfo> userInfoList = userInfoMapper.getUserInfoByListID(userIdList);
        Map<Integer, String> userIdToName = new HashMap<>();

        for (UserInfo userInfo : userInfoList) {
            userIdToName.put(userInfo.getUserId(), userInfo.getUsername());
        }

        for (LevelApplyVO levelApplyVO : levelApplyVOList) {
            levelApplyVO.setApplyerName(userIdToName.get(levelApplyVO.getApplyer()));
            levelApplyVO.setJugerName(userIdToName.get(levelApplyVO.getJudger()));
        }


        return levelApplyVOList;
    }

    @Override
    public List<LevelApply> getLevelApplyByStatus(int status) {
        List<LevelApply> levelApplyByStatus = levelApplyMapper.getLevelApplyByStatus(status);
        return levelApplyByStatus;
    }

    @Override
    public List<UserInfo> getUserInfoLike(UserInfo userInfo) {
        List<UserInfo> userInfoList = userInfoMapper.selectUserInfoLike(userInfo);
        return userInfoList;
    }

    @Override
    public UserInfoVO getUserInfoVOById(Integer id) {
        UserInfoVO userInfoVO = userInfoMapper.selectUserInfoVO(id);
        userInfoVO.setPrivilege(UserRole.getRoleByCode(Integer.parseInt(userInfoVO.getPrivilege())).getRole());
        userInfoVO.setGender(Gender.getGenderByCode(Integer.parseInt(userInfoVO.getGender())).getSex());
        return userInfoVO;
    }

    @Override
    public void batchUpdateUserPermission(String permission, List<Map<String, Object>> userList) {
        int permi = UserRole.getRoleByString(permission).getCode();
        for (Map<String, Object> userInfoVOMap : userList) {
            UserInfoVO userInfoVO = UserInfoVO.convertMapToUserInfoVO(userInfoVOMap);
            userInfoVO.setPrivilege(UserRole.getRoleByCode(permi).getRole());
            userInfoMapper.updateUserInfo(UserInfoVO.convertToUserInfo(userInfoVO));
        }

    }
}
