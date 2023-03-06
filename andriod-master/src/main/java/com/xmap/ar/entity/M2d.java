package com.xmap.ar.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("m2d")
public class M2d {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer author;
    private String title;
    private String description;
    private String thumb;
    private String folder;
    private String src;
    private String edited;
    @TableField(value = "file_size")
    private Float fileSize;

    @TableField(exist = false)
    private List<Tag> tagList;

    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

    private String address;
    private Boolean delete;
    private Boolean collect;
    private String uid;
}

