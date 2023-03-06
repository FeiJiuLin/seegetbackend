package com.xmap.v04.models;

import com.alibaba.fastjson.JSON;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddDataBlock {
    @NotNull
    private Integer type;  // 0 VIDEO, 1 IMAGE, 2 AR
    private Integer fps;
    private Integer image_height;
    private Integer image_width;
    private Boolean barometer;
    private Boolean gps;
    private Boolean gyro;
    private Boolean acc_calib;
    private Boolean geo_calib;
    private Boolean gyro_calib;
    private Boolean deep_map;
    private Boolean confidence;
    private Boolean pose;
    private Boolean aruco_marker;
    @NotNull
    private Integer uploadStatus;  // 0,1,2,3
    private JSON uploadProgress;
    private String minioKey;
    private Integer datapackedId;
    @NotNull
    private Timestamp createTime;
}
