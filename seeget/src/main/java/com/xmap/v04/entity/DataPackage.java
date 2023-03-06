package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xmap.v04.models.DataBlockResponse;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("datapackage")
public class DataPackage {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer author;
    private Integer folder;
    private String uuid;

    private String title;
    private String description;
    private String cover;
    private String thumb;
    private String location;
    @TableField(value = "file_num")
    private Integer fileNum;
    @TableField(value = "file_size")
    private Float fileSize;
    @TableField(value = "is_public")
    private Integer isPublic;  // 0,1,2,3

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private List<String> types;

    @TableField(exist = false)
    private List<DataBlock> dataBlocks;

    @NotNull
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;

    private Float latitude;
    private Float longitude;
    private Boolean is_archived;
}
