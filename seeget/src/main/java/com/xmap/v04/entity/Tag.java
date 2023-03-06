package com.xmap.v04.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Getter @Setter@AllArgsConstructor @NoArgsConstructor
@TableName("tags")
public class Tag implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer author;
    private String uuid;
    private String type;
    private String content;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;


}
