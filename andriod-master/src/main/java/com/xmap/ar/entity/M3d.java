package com.xmap.ar.entity;


import com.alibaba.fastjson.JSON;
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
@TableName("M3d")
public class M3d {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer author;
    private String title;
    private String description;
    private String thumb;
    private String folder;

    @TableField(exist = false)
    private JSON content;
    @TableField(value = "content_json")
    @JSONField(serialize = false)
    private String contentJson;

    @TableField(exist = false)
    private List<Tag> tagList;

    @TableField(value = "file_size")
    private Float fileSize;


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

