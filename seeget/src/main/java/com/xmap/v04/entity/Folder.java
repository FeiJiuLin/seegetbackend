package com.xmap.v04.entity;

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
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("folders")
public class Folder {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer author;
    private String uuid;
    @NotNull
    private String name;
    @TableField(exist = false)
    private List<String> keylists;
    @JSONField(serialize = false)
    private String keys;
    @NotNull
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;
    private Float latitude;
    private Float longitude;
    private String address;
    private String description;
    private String cover;
    private String type;
}
