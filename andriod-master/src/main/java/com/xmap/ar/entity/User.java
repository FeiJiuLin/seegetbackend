package com.xmap.ar.entity;


import com.alibaba.fastjson.annotation.JSONField;
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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@TableName("users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails, Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(serialize = false)
    private Integer id;

    private String phone;
    private String email;

    @JSONField(serialize = false)
    private String password;
    private String avatar;
    private String city;
    private String name;
    private String job;
    private int age;
    private String nation;

    private String uid;

    @TableField(value = "idcard")
    private String idCard;
    private String wechat;
    private String qq;

    private boolean gender;
    private Timestamp birthday;

    @TableField(value = "full_name")
    private String fullName;

    @TableField(value = "bank_card")
    private String bankCard;
    @TableField(value = "bank_card_number")
    private String bankCardNumber;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Role> authorities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return phone;
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
