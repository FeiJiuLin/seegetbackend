package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * FROM role WHERE id = #{id}")
    Role findRoleById(@Param("id") int id);

    @Select("SELECT * FROM role WHERE userid = #{id}")
    ArrayList<Role> findRoleByUserId(@Param("id") int id);
}
