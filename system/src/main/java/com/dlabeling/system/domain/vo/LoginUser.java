package com.dlabeling.system.domain.vo;

import com.dlabeling.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
    
    private String username;
    private String password;
    private String token;
    private Long loginTime;
    private Long expireTime;
    
    private Set<String> userRole;
    private Boolean delete;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityCollection = new ArrayList<>();
        if (userRole != null){  //不加这个会报错， 过程中有参数 全为null的LoginUser生成如何报错
            grantedAuthorityCollection = userRole.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return grantedAuthorityCollection;
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
