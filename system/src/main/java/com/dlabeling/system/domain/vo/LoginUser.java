package com.dlabeling.system.domain.vo;

import com.dlabeling.common.enums.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Data
public class LoginUser implements UserDetails {
    
    private String username;
    private String password;
    private String token;
    private Long loginTime;
    private Long expireTime;
    
    private String userRole;
    private Boolean delete;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (loginTime == null || expireTime == null){
            return true;
        }
        return System.currentTimeMillis() < loginTime + expireTime;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !delete;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (loginTime == null || expireTime == null){
            return true;
        }
        return System.currentTimeMillis() < loginTime + expireTime;
    }

    @Override
    public boolean isEnabled() {
        return !delete;
    }
}
