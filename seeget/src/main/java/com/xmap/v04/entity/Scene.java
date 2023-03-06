package com.xmap.v04.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class Scene {
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
    private Integer isPublic;  // 0,1,2

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private List<String> types;

    @TableField(exist = false)
    private List<DataPackage> dataPackages;

    @NotNull
    @TableField(value = "create_time")
    private Timestamp createTime;
    @TableField(value = "update_time")
    private Timestamp updateTime;
    private String algorithm;
    @TableField(value = "algorithm_status")
    private Integer algorithmStatus;
    @TableField(value = "start_time")
    private Timestamp startTime;
    @TableField(value = "end_time")
    private Timestamp endTime;
    @TableField(value = "total_time")
    private String totalTime;
    @TableField(value = "result_info")
    private String resultInfo;
    @TableField(value = "is_publish")
    private Boolean isPublish;
    @TableField(value = "algorithm_progress")
    private Float algorithmProgress;

    private Float latitude;
    private Float longitude;
}
