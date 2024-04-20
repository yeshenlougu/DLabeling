package com.dlabeling.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.dlabeling.admin.service.LevelApplyService;
import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.core.domain.model.LoginBody;
import com.dlabeling.common.core.redis.RedisCache;
import com.dlabeling.common.enums.ResponseCode;
import com.dlabeling.common.enums.UserRole;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.common.exception.user.UserException;
import com.dlabeling.common.utils.ServletUtils;
import com.dlabeling.framework.web.SysLoginService;
import com.dlabeling.framework.web.TokenService;
import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.domain.po.user.User;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LevelApplyVO;
import com.dlabeling.system.domain.vo.LoginUser;
import com.dlabeling.system.domain.vo.UserInfoVO;
import com.dlabeling.system.mapper.user.UserInfoMapper;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/3
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class ISysUserController {
    
    @Autowired
    ISysUserService iSysUserService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    SysLoginService sysLoginService;
    @Autowired
    TokenService tokenService;
    @Autowired
    LevelApplyService levelApplyService;

    @Autowired
    RedisCache redisCache;
    
    @PostMapping("/register")
    public R<String> registerUser(@RequestBody User user){
        try{
            iSysUserService.addUser(user);
            return R.ok(null, "用户注册成功");
        }catch (UserException e){
            return R.fail(ResponseCode.USER_LOGIN_FAIL.getCode(), e.getMessage());
        }catch (BusinessException e){
            
            return R.fail(e.getMsg());
        }
    }

    @PostMapping("/login")
    public R<Object> login(@RequestBody LoginBody loginBody){
        try{
            String username = loginBody.getUsername();
            String password = loginBody.getPassword();
            String code = loginBody.getCode();
            String captchaUUID = loginBody.getUuid();

            String token = sysLoginService.login(username, password, code, captchaUUID);
            User user = new User();
            user.setUsername(username);
            User userByEmailOrPhone = iSysUserService.getUserByEmailOrPhone(user);
            UserInfo userInfo = iSysUserService.getUserInfoById(userByEmailOrPhone.getId());
            HashMap<String, String> data = new HashMap<>();
            data.put("token", token);
            data.put("role", UserRole.getRoleByCode(userInfo.getPrivilege()).getRole());
            return R.ok(data, "登录成功");
        }catch (BusinessException e){
            return R.fail(e.getMsg());
        }
    }

    /**
     * 更换密码、邮箱、手机号
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public R<String> updateUser(User user){
        try{
            iSysUserService.updateUser(user);
            return R.ok(null, "更改成功");
        }catch (BusinessException e){
            log.info(e.getMsg());
            return R.fail(null, e.getMsg());
        }
    }

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    @PostMapping("/updateUserInfo")
    public  R<String> updateUserInfo(@RequestBody UserInfoVO userInfo){
        try{
            iSysUserService.updateUserInfo(userInfo);
            return R.ok(null, "更改用户信息成功");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }
    }

    /**
     * 删除用户
     * @param user
     * @return
     */
    @GetMapping("/deleteUser")
    public R<String> deleteUser(User user){
        try {
            iSysUserService.deleteUser(user);
            return R.ok(null, "删除用户成功");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }
    }

    /**
     * 获取所有用户信息
     * @return
     */
    //    TODO 进行权限校验
    @PostMapping("/getAllUserInfo")
    public R<List<UserInfoVO>> getAllUserInfo(){
        try{
            List<UserInfo> userInfoList = iSysUserService.getAllUserInfo();
            List<UserInfoVO> userInfoVOList = userInfoList.stream().map(UserInfoVO::convertToUserInfoVO).collect(Collectors.toList());
            return R.ok(userInfoVOList, "获取所有用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }

    /**
     * 筛选用户
     */
    @PostMapping("/getFilterUser")
    public R<List<UserInfo>> getFilterUser(@RequestBody UserInfoVO userInfoVO){
        List<UserInfo> userInfoList = iSysUserService.getUserInfoLike(UserInfo.convertToUserInfo(userInfoVO));
        return R.ok(userInfoList);
    }

    /**
     * 获取某个用户信息
     * @return
     */
    @GetMapping("/getUserInfo")
    public R<UserInfoVO> getUserInfo(){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
            UserInfoVO userInfo = iSysUserService.getUserInfoVOById(loginUser.getId());
            return R.ok(userInfo, "获取用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }

    /**
     * 添加权限申请
     * @param levelApply
     * @return
     */
    @PostMapping("/privilege/add")
    public R<String> addApplyPrivilege(@RequestBody LevelApplyVO levelApply){
        try{
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
            levelApply.setApplyer(loginUser.getId());
            iSysUserService.addLevelApply(levelApply);
            return R.ok(null, "以提交申请");
        }
        catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }
        catch (Exception e){
            return R.fail(null, "提交申请失败");
        }
    }

    /**
     * 处理权限申请
     * @param levelApplyVO
     * @return
     */
    @PostMapping("/privilegeApply/update")
    public R<String> updateApplyPrivilege(@RequestBody LevelApplyVO levelApplyVO){
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getHttpServletRequest());
            levelApplyVO.setJudger(loginUser.getId());
            iSysUserService.updateLevelApply(levelApplyVO);
            return R.ok(null, "完成审核");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "审核失败");
        }

    }

    /**
     * 获取所有权限申请
     * @return
     */
    @GetMapping("/privilegeApply/getAll")
    public R<List<LevelApplyVO>> getAllApplyPrivilege(String type){
        try {
            List<LevelApplyVO> allLevelApply = levelApplyService.getAllLevelApply(type);
            return R.ok(allLevelApply, "获取所有权限审核");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "获取所有权限审核失败");
        }
    }

    /**
     * 根据申请状态筛选 申请
     * @param status
     * @return
     */
    @GetMapping("/privilege/getByStatus")
    public R<List<LevelApply>> getApplyPrivilegeByStatus(int status){
        try {
            List<LevelApply> allLevelApply = iSysUserService.getLevelApplyByStatus(status);
            return R.ok(allLevelApply, "获取特定权限审核");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "获取特定权限审核失败");
        }
    }


    /**
     * 获取用户可以拥有的权限
     * @return
     */
    @GetMapping("/permissionList")
    public R<List<String>> getUserPermissionList(){
        return R.ok(Stream.of(UserRole.values()).map(UserRole::getRole).collect(Collectors.toList()));
    }

    /**
     * 用户权限变更
     * @param data
     * @return
     */
    @PostMapping("/updateUserPermission")
    public R<String> updateUserPermission(@RequestBody Map<String, Object> data){

        iSysUserService.batchUpdateUserPermission((String) data.get("permission"), (List<Map<String, Object>>) data.get("userList"));
        return R.ok();
    }
}
