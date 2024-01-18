package com.dlabeling.db.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlabeling.db.domain.po.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/2
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 创建用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 删除用户
     * @param user
     * @return
     */
    int deleteUser(User user);

    /**
     * 查询用户 （账号、密码）
     * @param user
     * @return
     */
    User selectUser(User user);

    /**
     * 根据id查询用户
     * @param uuid
     * @return
     */
    User selectUserById(Integer uuid);
}
