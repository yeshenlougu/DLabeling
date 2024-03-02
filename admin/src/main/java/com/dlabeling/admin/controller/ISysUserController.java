package com.dlabeling.admin.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.core.domain.model.LoginBody;
import com.dlabeling.common.core.redis.RedisCache;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.framework.web.SysLoginService;
import com.dlabeling.system.domain.po.LevelApply;
import com.dlabeling.system.domain.po.user.User;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LoginUser;
import com.dlabeling.system.mapper.user.UserInfoMapper;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/3
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class ISysUserController {
    
    @Autowired
    ISysUserService iSysUserService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    SysLoginService sysLoginService;

    @Autowired
    RedisCache redisCache;
    @GetMapping("/test")
    public String test(HttpServletRequest httpServletRequest){
        String key = "login_tokens:18a1b868-1923-4e9e-8b3c-f0c973b4c5a9";
        LoginUser loginUser = (LoginUser) redisCache.getCacheObject(key);
        return loginUser.toString();
    }
    
    @PostMapping("/register")
    public R<String> registerUser(User user){
        try{
            iSysUserService.addUser(user);
            return R.ok(null, "用户注册成功");
        }catch (BusinessException e){
            
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }

    @PostMapping("/login")
    public R<String> login(LoginBody loginBody){
        try{
            String username = loginBody.getUsername();
            String password = loginBody.getPassword();
            String code = loginBody.getCode();
            String captchaUUID = loginBody.getUuid();

            String token = sysLoginService.login(username, password, code, captchaUUID);
            return R.ok(token, "登录成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
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
    
    @PostMapping("/updateUserInfo")
    public  R<String> updateUserInfo(UserInfo userInfo){
        try{
            iSysUserService.updateUserInfo(userInfo);
            return R.ok(null, "更改用户信息成功");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }
    }
    
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
    public R<List<UserInfo>> getAllUserInfo(){
        try{
            List<UserInfo> userInfoList = iSysUserService.getAllUserInfo();
            return R.ok(userInfoList, "获取所有用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }
    
    @GetMapping("/getUserInfo")
    public R<UserInfo> getUserInfo(Integer id){
        try {
            UserInfo userInfo = iSysUserService.getUserInfoById(id);
            return R.ok(userInfo, "获取用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }

    @PostMapping("/privilege/add")
    public R<String> addApplyPrivilege(LevelApply levelApply){
        try{
            iSysUserService.addLevelApply(levelApply);
            return R.ok(null, "以提交申请");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "提交申请失败");
        }
    }

    @PostMapping("/privilege/update")
    public R<String> updateApplyPrivilege(LevelApply levelApply){
        try {
            iSysUserService.updateLevelApply(levelApply);
            return R.ok(null, "完成审核");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "审核失败");
        }

    }

    @GetMapping("/privilege/getAll")
    public R<List<LevelApply>> getAllApplyPrivilege(){
        try {
            List<LevelApply> allLevelApply = iSysUserService.getAllLevelApply();
            return R.ok(allLevelApply, "获取所有权限审核");
        }catch (BusinessException e){
            return R.fail(null, e.getMsg());
        }catch (Exception e){
            return R.fail(null, "获取所有权限审核失败");
        }
    }

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
}
