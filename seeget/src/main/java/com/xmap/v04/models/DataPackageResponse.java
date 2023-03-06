package com.xmap.v04.models;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmap.v04.entity.DataPackage;
import io.swagger.models.auth.In;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataPackageResponse {
    private Integer id;
    private JSON metadata;
    private JSON state;
    private Boolean synchronization;
    private Integer bucketId;
    private String objectKey;
    private Timestamp createTime;
    private Timestamp updateTime;

//    public DataPackageResponse(DataPackage dataPackage) {
//        JSONObject json;
//        json = JSONObject.parseObject(dataPackage.getMetadata());
//        metadata = json;
//        json = JSONObject.parseObject(dataPackage.getState());
//        state = json;
//        id = dataPackage.getId();
//        synchronization = dataPackage.getSynchronization();
//        bucketId = dataPackage.getBucketId();
//        objectKey = dataPackage.getObjectKey();
//        createTime = dataPackage.getCreateTime();
//        updateTime = dataPackage.getUpdateTime();
//    }
}
