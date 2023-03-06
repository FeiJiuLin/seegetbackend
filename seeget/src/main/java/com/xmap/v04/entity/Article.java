package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("article")
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class Article implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer creator;
    @TableField(value = "bucket_id")
    private Integer bucketId;
    private String title;
    private String description;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;
    private String images;
}
