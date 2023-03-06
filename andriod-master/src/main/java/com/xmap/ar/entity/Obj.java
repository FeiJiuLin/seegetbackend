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

@Data@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("obj")
public class Obj {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer author;
    private String folder;
    private String uid;

    private String title;
    private String description;
    private String thumb;
    private String address;

    @TableField(value = "file_size")
    private Float fileSize;

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;
    @TableField(value = "delete_time")
    private Timestamp deleteTime;

    private Boolean delete;

    @TableField(exist = false)
    private JSON content;
    @JSONField(serialize = false)
    private String contents;

}

