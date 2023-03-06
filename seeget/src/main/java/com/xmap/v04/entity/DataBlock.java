package com.xmap.v04.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@TableName("datablock")
public class DataBlock implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String uuid;
    @NotNull
    private Integer type;  // 0 VIDEO, 1 IMAGE, 2 AR
    @NotNull
    @TableField("create_time")
    private Timestamp createTime;
    private Integer fps;
    @TableField("image_height")
    private Integer imageHeight;
    @TableField("image_width")
    private Integer imageWidth;
    private Boolean barometer;
    private Boolean acc;
    private Boolean gps;
    private Boolean gyro;
    @TableField("acc_calib")
    private Boolean accCalib;
    @TableField("geo_calib")
    private Boolean geoCalib;
    @TableField("gyro_calib")
    private Boolean gyroCalib;
    @TableField("deep_map")
    private Boolean deepMap;
    private Boolean confidence;
    private Boolean pose;
    @TableField("aruco_marker")
    private Boolean arucoMarker;
    @NotNull
    @TableField("upload_status")
    private Integer uploadStatus;  // 0,1,2,3
    @TableField("upload_progress")
    @JSONField(serialize = false)
    private String uploadProgress;
    @TableField("minio_key")
    private String minioKey;
    @TableField("data_package_id")
    private Integer datapackedId;
    private Integer owner;
    private Float latitude;
    private Float longitude;

    @TableField(exist = false)
    private JSON progressJson;
}
