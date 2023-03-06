package com.xmap.v04.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xmap.v04.entity.*;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.DataBlockMapper;
import com.xmap.v04.mapper.DataPackageMapper;
import com.xmap.v04.mapper.DataPackageToTagMapper;
import com.xmap.v04.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DataPackageService {
    @Autowired
    DataPackageMapper dataPackageMapper;

    @Autowired
    DataBlockMapper dataBlockMapper;

    @Autowired
    DataPackageToTagMapper dataPackageToTagMapper;

    @Autowired
    TagMapper tagMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public DataPackage addDataPackage(DataPackage dataPackage, User owner) {
        dataPackage.setAuthor(owner.getId());
        String uuid = "DP" + UUID.randomUUID().toString().replace("-", "");
        dataPackage.setUuid(uuid);
        dataPackageMapper.insert(dataPackage);
        return dataPackage;
    }

//    public List<DataPackage> getAll() {
//        QueryWrapper<DataPackage> wrapper= new QueryWrapper<>();
//        wrapper.orderByDesc("create_time");
//        return dataPackageMapper.selectList(wrapper);
//    }

    public DataPackage getById(Integer id) throws NotFoundException {
        DataPackage dataPackage = dataPackageMapper.selectById(id);
        if (dataPackage == null) {
            throw new NotFoundException("datapackage not found!");
        }
        QueryWrapper<DataBlock> dataBlockQueryWrapper = new QueryWrapper<>();
        dataBlockQueryWrapper.eq("data_package_id", id);
        List<DataBlock> datas = dataBlockMapper.selectList(dataBlockQueryWrapper);

        List<String> types = new ArrayList<>();
        for(DataBlock data : datas) {
            int dbType = data.getType();
            if (dbType == 0) {
                types.add("VIDEO");
            } else if (dbType == 1) {
                types.add("IMAGE");
            } else {
                types.add("AR");
            }
            JSONObject json;
            json = JSONObject.parseObject(data.getUploadProgress());
            data.setProgressJson(json);
        }
        dataPackage.setDataBlocks(datas);
        dataPackage.setTypes(types);

        QueryWrapper<DataPackageToTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_package_id", dataPackage.getId());
        List<DataPackageToTag> dataPackageToTags = dataPackageToTagMapper.selectList(queryWrapper);
        List<Tag> tags = new ArrayList<>();

        for (DataPackageToTag toTag: dataPackageToTags) {
            Tag tag = tagMapper.selectById(toTag.getTagId());
            tags.add(tag);
        }
        dataPackage.setTags(tags);
        return dataPackage;
    }



    public void updateSyn(Boolean state, int id) {
        Timestamp time = get_time();
        dataPackageMapper.updateSyn(state, time, id);
    }

    public DataPackage update(DataPackage dataPackage) throws Exception{
        Timestamp time = get_time();
        dataPackage.setUpdateTime(time);
        UpdateWrapper<DataPackage> dataPackageUpdateWrapper = new UpdateWrapper<>();
        dataPackageUpdateWrapper.eq("id", dataPackage.getId());
        dataPackageMapper.update(dataPackage, dataPackageUpdateWrapper);
        return getById(dataPackage.getId());
    }

    public void updateTag(Integer id, List<Integer> tags) {
        for (Integer tag: tags) {
            DataPackageToTag dataPackageToTag = new DataPackageToTag();
            dataPackageToTag.setTagId(tag);
            dataPackageToTag.setDatapackageId(id);
            dataPackageToTagMapper.insert(dataPackageToTag);
        }
    }

    public void deleteById(Integer id) {
        QueryWrapper<DataBlock> dataBlockQueryWrapper = new QueryWrapper<>();
        dataBlockQueryWrapper.eq("data_package_id", id);
        List<DataBlock>  dataBlockList = dataBlockMapper.selectList(dataBlockQueryWrapper);
        for(DataBlock dataBlock : dataBlockList) {
            dataBlockMapper.deleteById(dataBlock.getId());
        }
        dataPackageMapper.deleteById(id);
    }

    public List<DataPackage> getByAuthor(int id) throws Exception{
        QueryWrapper<DataPackage> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("author", id);
        List<DataPackage> dataPackageList =  dataPackageMapper.selectList(wrapper);
        List<DataPackage> result = new ArrayList<>();
        for (DataPackage dp : dataPackageList) {
            result.add(getById(dp.getId()));
        }
        return result;
    }


    public List<DataPackage> getPublic() throws Exception{
        QueryWrapper<DataPackage> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.ne("is_public", 2);
        List<DataPackage> dataPackageList =  dataPackageMapper.selectList(wrapper);
        List<DataPackage> result = new ArrayList<>();
        for (DataPackage dp : dataPackageList) {
            result.add(getById(dp.getId()));
        }
        return result;
    }

    public DataPackage getByUuid(String uuid) {
        QueryWrapper<DataPackage> dataPackageQueryWrapper = new QueryWrapper<>();
        dataPackageQueryWrapper.eq("uuid", uuid);

        return dataPackageMapper.selectOne(dataPackageQueryWrapper);
    }
}
