package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.User;
import org.apache.ibatis.annotations.*;


public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findUserByPhone(@Param("phone") String phone);

    @Insert("INSERT into users (phone, uid, password) values (#{phone}, #{uid}, #{password})")
    void register(@Param("phone") String phone,@Param("uid") String uid, @Param("password") String password);

    @Update("UPDATE users set password = #{password} WHERE users.id = #{id}")
    void changePassword(@Param("password") String password, @Param("id") int id);
}
