package com.xmap.ar.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("folder")
public class Folder {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer author;
    private String uid;
    private String title;
    private String description;
    private String thumb;

    @TableField(exist = false)
    private List<String> content;
    @JSONField(serialize = false)
    @TableField(value = "content_keys")
    private String contentKeys;

    @TableField(value = "file_size")
    private float fileSize;
    @TableField(value = "file_num")
    private Integer fileNum;

    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;
    @TableField
    private Timestamp deleteTime;

    private String address;
    private Boolean delete;
    private Boolean collect;
}
