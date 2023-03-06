package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@TableName("role")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String name;

//    @TableField(value = "ru")
    private int userid;

    @Override
    public String getAuthority() {
        return name;
    }
}