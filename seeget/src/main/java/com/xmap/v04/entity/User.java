package com.xmap.v04.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Data
@TableName("users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails, Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(serialize = false)
    private int id;
    private String username;
    @JSONField(serialize = false)
    private String password;
    private String avatar;
    private String description;
    private String phone;
    private String realname;
    @TableField("id_number")
    private String idNumber;
    private String city;
    @TableField("wexin_id")
    private String wexinId;
    @TableField("qq_id")
    private String qqId;
    private String email;

    private boolean sex;
    private int age;
    private int education;

    @TableField("create_time")
    private Timestamp createTime;

    private String uuid;
    private String nickname;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Role> authorities = new ArrayList<>();

    public User (String username){
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
