package com.xmap.v04.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.v04.entity.DataBlock;
import com.xmap.v04.mapper.DataBlockMapper;
import com.xmap.v04.models.AddDataBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DataBlockService {
    @Autowired
    DataBlockMapper dataBlockMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public DataBlock addDataBlock(AddDataBlock addDataBlock, int id) {
        DataBlock dataBlock = new DataBlock();
        String uuid = "DB" + UUID.randomUUID().toString().replace("-", "");
        dataBlock.setType(addDataBlock.getType());
        dataBlock.setCreateTime(addDataBlock.getCreateTime());
        dataBlock.setFps(addDataBlock.getFps());
        dataBlock.setImageHeight(addDataBlock.getImage_height());
        dataBlock.setImageWidth(addDataBlock.getImage_width());
        dataBlock.setBarometer(addDataBlock.getBarometer());
        dataBlock.setGps(addDataBlock.getGps());
        dataBlock.setGyro(addDataBlock.getGyro());
        dataBlock.setAccCalib(addDataBlock.getAcc_calib());
        dataBlock.setGeoCalib(addDataBlock.getGeo_calib());
        dataBlock.setGyroCalib(addDataBlock.getGyro_calib());
        dataBlock.setDeepMap(addDataBlock.getDeep_map());
        dataBlock.setConfidence(addDataBlock.getConfidence());
        dataBlock.setPose(addDataBlock.getPose());
        dataBlock.setArucoMarker(addDataBlock.getAruco_marker());
        dataBlock.setUploadStatus(addDataBlock.getUploadStatus());
        dataBlock.setUploadProgress(addDataBlock.getUploadProgress().toJSONString());
        dataBlock.setMinioKey(addDataBlock.getMinioKey());
        dataBlock.setDatapackedId(addDataBlock.getDatapackedId());
        dataBlock.setOwner(id);
        dataBlock.setUuid(uuid);
        dataBlockMapper.insert(dataBlock);
        return dataBlock;
    }

    public DataBlock getById(Integer id) {
        DataBlock dataBlock = dataBlockMapper.selectById(id);

        JSONObject json;
        json = JSONObject.parseObject(dataBlock.getUploadProgress());
        dataBlock.setProgressJson(json);
        return dataBlock;
    }

    public DataBlock setProgress(String progressJson, Integer id) {
        dataBlockMapper.updateState(progressJson,  id);
        return getById(id);
    }

    public DataBlock setStatus(Integer status, Integer id) {
        dataBlockMapper.updateStatus(status, id);
        return getById(id);
    }

    public DataBlock setMetaData(DataBlock dataBlock, Integer id) {
        if (dataBlock.getProgressJson() != null)
            dataBlock.setUploadProgress(dataBlock.getProgressJson().toJSONString());
        QueryWrapper<DataBlock> dataBlockQueryWrapper = new QueryWrapper<>();
        dataBlockQueryWrapper.eq("id", id);
        dataBlockMapper.update(dataBlock, dataBlockQueryWrapper);
        return getById(id);
    }

    public DataBlock getByUuid(String uuid) {
        QueryWrapper<DataBlock> dataBlockQueryWrapper = new QueryWrapper<>();
        dataBlockQueryWrapper.eq("uuid", uuid);
        DataBlock dataBlock = dataBlockMapper.selectOne(dataBlockQueryWrapper);
        JSONObject json;
        json = JSONObject.parseObject(dataBlock.getUploadProgress());
        dataBlock.setProgressJson(json);
        return dataBlock;
    }
}
