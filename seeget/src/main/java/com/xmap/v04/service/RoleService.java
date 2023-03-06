package com.xmap.v04.service;

import com.xmap.v04.entity.Role;
import com.xmap.v04.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleMapper mapper;

    public List<Role> selectByUserid(int id) {
//        QueryWrapper<Role> wrapper = new QueryWrapper<>();
//        wrapper.eq("role.user_id", id);
        List<Role> roleArrayList = mapper.findRoleByUserId(id);
        return roleArrayList;
    }
}
