package com.xmap.ar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@TableName("bucket")
@Getter
@Setter
@NoArgsConstructor
public class Bucket implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer owner;
}

