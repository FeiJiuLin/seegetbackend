package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;


public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findUserByName(@Param("username") String username);

    @Insert("INSERT into users (username, uuid, password, create_time) values (#{username}, #{uuid}, #{password}, #{create_time})")
    void register(@Param("username") String username,@Param("uuid") String uuid, @Param("password") String password, @Param("create_time") Timestamp create_time);

    @Update("UPDATE users set password = #{password} WHERE users.id = #{id}")
    void changePassword(@Param("password") String password, @Param("id") int id);
}