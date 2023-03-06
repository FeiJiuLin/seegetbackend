package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("marker")
public class Marker implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private String key;
    private Integer creator;
    private float height;
    private float width;
    @TableField("bucket_id")
    private Integer bucketId;
    private Boolean share;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;
}
