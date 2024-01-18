package com.dlabeling.admin.controller;

import com.dlabeling.common.core.domain.R;
import com.dlabeling.common.exception.BusinessException;
import com.dlabeling.db.domain.po.user.User;
import com.dlabeling.db.domain.po.user.UserInfo;
import com.dlabeling.system.service.user.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ISysUserService ISysUserService;
    
    @PostMapping("/register")
    public R<String> registerUser(User user){
        try{
            ISysUserService.addUser(user);
            return R.ok();
        }catch (BusinessException e){
            
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    } 
    
    @PostMapping("/login")
    public R<String> login(User user){
        try{
            ISysUserService.login(user);
            return R.ok(null, "登录成功");
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
            ISysUserService.updateUser(user);
            return R.ok(null, "更改成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }
    
    @PostMapping("/updateUserInfo")
    public  R<String> updateUserInfo(UserInfo userInfo){
        try{
            ISysUserService.updateUserInfo(userInfo);
            return R.ok(null, "更改用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }
    
    @GetMapping("/deleteUser")
    public R<String> deleteUser(User user){
        try {
            ISysUserService.deleteUser(user);
            return R.ok(null, "删除用户成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
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
            List<UserInfo> userInfoList = ISysUserService.getAllUserInfo();
            return R.ok(userInfoList, "获取所有用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }
    
    @GetMapping("/getUserInfo")
    public R<UserInfo> getUserInfo(Integer id){
        try {
            UserInfo userInfo = ISysUserService.getUserInfoById(id);
            return R.ok(userInfo, "获取用户信息成功");
        }catch (BusinessException e){
            return R.fail(e.getCode().getCode(), e.getMsg());
        }
    }
}
