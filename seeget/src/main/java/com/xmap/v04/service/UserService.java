package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xmap.v04.entity.Role;
import com.xmap.v04.entity.User;
import com.xmap.v04.mapper.RoleMapper;
import com.xmap.v04.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findUserByName(String username){
        return userMapper.findUserByName(username);
    }

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public User regist(String username, String password) {
        try {
            User user = userMapper.findUserByName(username);
            if (user != null){
                throw new ValidationException("username exists.");
            }else {
                String encode_password = passwordEncoder.encode(password);
                //创建uuid
                String uuid = "user-"+UUID.randomUUID().toString().replace("-", "");
                userMapper.register(username, uuid, encode_password, get_time());
                return userMapper.findUserByName(username);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
            return userMapper.findUserByName(username);
        }
    }

    public User update(User user) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", user.getId());
        userMapper.update(user, userUpdateWrapper);
        return userMapper.selectById(user.getId());
    }

    public void changePassword(String password, int id) {
        String encode_password = passwordEncoder.encode(password);
        userMapper.changePassword(encode_password, id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(username);
        if (user == null)
            throw new UsernameNotFoundException(username + "not found");
        int id = user.getId();
        List<Role> roleArrayList = roleMapper.findRoleByUserId(id);
        user.setAuthorities(Objects.requireNonNullElseGet(roleArrayList, ArrayList::new));
        return user;
    }

    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
