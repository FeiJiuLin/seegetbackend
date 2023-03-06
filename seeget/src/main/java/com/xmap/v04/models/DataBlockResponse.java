package com.xmap.v04.models;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmap.v04.entity.DataBlock;

import java.sql.Timestamp;

public class DataBlockResponse {
    private Integer id;
    private String type;  // 0 VIDEO, 1 IMAGE, 2 AR
    private Timestamp createTime;
    private Integer fps;
    private Integer imageHeight;
    private Integer imageWidth;
    private Boolean barometer;
    private Boolean acc;
    private Boolean gps;
    private Boolean gyro;
    private Boolean accCalib;
    private Boolean geoCalib;
    private Boolean gyroCalib;
    private Boolean deepMap;
    private Boolean confidence;
    private Boolean pose;
    private Boolean arucoMarker;
    private String uploadStatus;  // 0,1,2,3
    private JSON uploadProgress;
    private String minioKey;
    private Integer datapackedId;
    private Integer owner;

    public DataBlockResponse(DataBlock dataBlock) {
        id = dataBlock.getId();
        if(dataBlock.getType() == 0)
            type = "VIDEO";
        else if(dataBlock.getType() == 1)
            type = "IMAGE";
        else
            type = "AR";
        createTime = dataBlock.getCreateTime();
        fps = dataBlock.getFps();
        imageHeight = dataBlock.getImageHeight();
        imageWidth = dataBlock.getImageWidth();
        barometer = dataBlock.getBarometer();
        acc = dataBlock.getAcc();
        gps = dataBlock.getGps();
        gyro = dataBlock.getGyro();
        accCalib = dataBlock.getAccCalib();
        geoCalib = dataBlock.getGeoCalib();
        gyroCalib = dataBlock.getGyroCalib();
        deepMap = dataBlock.getDeepMap();
        confidence = dataBlock.getConfidence();
        pose = dataBlock.getPose();
        arucoMarker = dataBlock.getArucoMarker();
        if (dataBlock.getUploadStatus() == 0)
            uploadStatus = "uploadding";
        else if (dataBlock.getUploadStatus() == 1)
            uploadStatus = "pause";
        else if (dataBlock.getUploadStatus() == 2)
            uploadStatus = "unsynchronized";
        else
            uploadStatus = "uploaded";
        JSONObject json;
        json = JSONObject.parseObject(dataBlock.getUploadProgress());
        uploadProgress = json;
        minioKey = dataBlock.getMinioKey();
        datapackedId = dataBlock.getDatapackedId();
        owner = dataBlock.getOwner();
    }

}
