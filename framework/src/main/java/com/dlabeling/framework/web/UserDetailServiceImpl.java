package com.dlabeling.framework.web;

import com.dlabeling.common.enums.UserRole;
import com.dlabeling.common.exception.ServiceException;
import com.dlabeling.common.exception.user.UserNameIllegalException;
import com.dlabeling.common.utils.StringUtils;
import com.dlabeling.system.domain.po.user.User;
import com.dlabeling.system.domain.po.user.UserInfo;
import com.dlabeling.system.domain.vo.LoginUser;
import com.dlabeling.system.service.user.ISysUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    RedisTemplate<String, String> redisTemplate;
    
    @Resource
    ISysUserService iSysUserService;
    
    @Resource
    SysPasswordService passwordService;
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        if (StringUtils.isEmail(username)){
            user.setEmail(username);
        }else if (StringUtils.isPhone(username)){
            user.setPhone(username);
        }else {
            throw new UserNameIllegalException();
        }
        User userByEmailOrPhone = iSysUserService.getUserByEmailOrPhone(user);
        if (StringUtils.isNull(userByEmailOrPhone)){
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (StringUtils.isNotNull(userByEmailOrPhone.getDestroyTime())) {
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        passwordService.validate(userByEmailOrPhone);
        
        return null;
    }

    public UserDetails createLoginUser(User user)
    {
        UserInfo userInfoById = iSysUserService.getUserInfoById(user.getId());
        LoginUser loginUser = new LoginUser();
        loginUser.setDelete( user.getDestroyTime()==null? Boolean.FALSE : Boolean.TRUE);
        loginUser.setUsername(user.getEmail());
        loginUser.setUserRole(UserRole.getRoleByCode(userInfoById.getPrivilege()).getRole());
        return loginUser;
    }
    
}