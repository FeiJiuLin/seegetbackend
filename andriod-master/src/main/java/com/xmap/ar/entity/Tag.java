package com.xmap.ar.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Timestamp;

@TableName("tag")
@Data@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Tag {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer author;
    private String uid;
    @TableField(value = "used")
    private Boolean frequentlyUsed;
    private String content;
}