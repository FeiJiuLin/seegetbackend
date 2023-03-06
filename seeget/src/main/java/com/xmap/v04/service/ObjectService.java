package com.xmap.v04.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xmap.v04.entity.*;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.*;
import org.apache.ibatis.ognl.ObjectElementsAccessor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ObjectToDpMapper objectToDpMapper;

    @Autowired
    private ObjectToTagMapper objectToTagMapper;

    @Autowired
    private DataPackageMapper dataPackageMapper;

    @Autowired
    private DataBlockMapper dataBlockMapper;

    @Autowired
    private TagMapper tagMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Objects add(Objects object) {
        String uuid = "SCENE" + UUID.randomUUID().toString().replace("-", "");
        object.setUuid(uuid);
//        object.setCreateTime(get_time());
        objectMapper.insert(object);
        if(object.getDataPackages() != null) {
            for(DataPackage dp: object.getDataPackages()) {
                ObjectToDp objectToDp =  new ObjectToDp();
                objectToDp.setObjectId(object.getId());
                objectToDp.setDatapackageId(dp.getId());
                objectToDpMapper.insert(objectToDp);
            }
        }
        if(object.getTags() != null) {
            for(Tag tag: object.getTags()) {
                ObjectToTag objectToTag = new ObjectToTag();
                objectToTag.setObjectId(object.getId());
                objectToTag.setTagId(tag.getId());
                objectToTagMapper.insert(objectToTag);
            }
        }
        return object;
    }

    public void delete(int id) throws Exception{
        Objects object = getById(id);
        QueryWrapper<ObjectToTag> objectToTagQueryWrapper = new QueryWrapper<>();
        assert object != null;
        objectToTagQueryWrapper.eq("object_id", object.getId());
        List<ObjectToTag> objectToTags = objectToTagMapper.selectList(objectToTagQueryWrapper);
        if(objectToTags!=null){
            for(ObjectToTag objectToTag :objectToTags) {
                objectToTagMapper.deleteById(objectToTag);
            }
        }

        QueryWrapper<ObjectToDp> objectToDpQueryWrapper = new QueryWrapper<>();
        objectToDpQueryWrapper.eq("object_id", object.getId());
        List<ObjectToDp> objectToDps = objectToDpMapper.selectList(objectToDpQueryWrapper);
        if(objectToDps!=null){
            for(ObjectToDp objectToDp: objectToDps){
                objectToDpMapper.deleteById(objectToDp);
            }
        }
        objectMapper.deleteById(id);
    }

    public DataPackage getDPDetail(Integer id) throws Exception{
        DataPackage dataPackage = dataPackageMapper.selectById(id);
        if (dataPackage == null) {
            return null;
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

        return dataPackage;
    }

    public List<Objects> getPublicObject() throws Exception{
        QueryWrapper<Objects> sceneQueryWrapper = new QueryWrapper<>();
        sceneQueryWrapper.orderByDesc("create_time");
        sceneQueryWrapper.ne("is_public", 2);
        List<Objects> objects = objectMapper.selectList(sceneQueryWrapper);
        List<Objects> result = new ArrayList<>();
        for(Objects object: objects) {
            result.add(getById(object.getId()));
        }
        return result;
    }

    @Nullable
    public Objects getById(Integer id) throws Exception{
        Objects object = objectMapper.selectById(id);
        if (object == null)
            throw new NotFoundException("folder not found");
        QueryWrapper<ObjectToDp> objectToDpQueryWrapper = new QueryWrapper<>();
        objectToDpQueryWrapper.eq("object_id", id);
        List<ObjectToDp> objectToDps = objectToDpMapper.selectList(objectToDpQueryWrapper);

        List<DataPackage> dataPackageList = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (ObjectToDp objectToDp: objectToDps) {
            DataPackage dataPackage = getDPDetail(objectToDp.getDatapackageId());
            dataPackageList.add(dataPackage);
            types.addAll(dataPackage.getTypes());
        }
        object.setDataPackages(dataPackageList);
        object.setTypes(types);

        QueryWrapper<ObjectToTag> objectToTagQueryWrapper = new QueryWrapper<>();
        objectToTagQueryWrapper.eq("object_id", object.getId());
        List<ObjectToTag> objectToTags = objectToTagMapper.selectList(objectToTagQueryWrapper);
        List<Tag> tags = new ArrayList<>();
        if(objectToTags!=null){
            for(ObjectToTag sceneToTag :objectToTags) {
                tags.add(tagMapper.selectById(sceneToTag.getTagId()));
            }
        }
        object.setTags(tags);

        return object;
    }

    public Objects getByUuid(String uuid) throws Exception {
        QueryWrapper<Objects> objectsQueryWrapper = new QueryWrapper<>();
        objectsQueryWrapper.eq("uuid", uuid);
        Objects object = objectMapper.selectOne(objectsQueryWrapper);
        return getById(object.getId());
    }

    public List<Objects> getSelfScene(int id) throws Exception {
        QueryWrapper<Objects> objectsQueryWrapper = new QueryWrapper<>();
        objectsQueryWrapper.orderByDesc("create_time");
        objectsQueryWrapper.eq("author", id);
        List<Objects> objects = objectMapper.selectList(objectsQueryWrapper);
        List<Objects> result = new ArrayList<>();
        for(Objects objects1: objects) {
            result.add(getById(objects1.getId()));
        }
        return result;
    }

    public void addDp(Integer id, int dpId) {
        ObjectToDp objectToDp = new ObjectToDp();
        objectToDp.setObjectId(id);
        objectToDp.setDatapackageId(dpId);
        objectToDpMapper.insert(objectToDp);
    }

    public void addTag(Integer id, int tagId){
        ObjectToTag objectToTag = new ObjectToTag();
        objectToTag.setTagId(tagId);
        objectToTag.setObjectId(id);
        objectToTagMapper.insert(objectToTag);
    }

    public void deleteDp(Integer id, int dpId) {
        QueryWrapper<ObjectToDp> objectToDpQueryWrapper = new QueryWrapper<>();
        objectToDpQueryWrapper.eq("object_id", id);
        objectToDpQueryWrapper.eq("data_package_id", dpId);
        objectToDpMapper.delete(objectToDpQueryWrapper);
    }

    public Objects update(int id, Objects object) throws Exception {
        Timestamp time = get_time();
        object.setUpdateTime(time);
        UpdateWrapper<Objects> objectsUpdateWrapper = new UpdateWrapper<>();
        objectsUpdateWrapper.eq("id", id);
        objectMapper.update(object, objectsUpdateWrapper);
        return getById(object.getId());
    }

    public void deleteByUuid(String uuid) throws Exception{
        QueryWrapper<Objects> objectsQueryWrapper = new QueryWrapper<>();
        objectsQueryWrapper.eq("uuid", uuid);
        Objects object = objectMapper.selectOne(objectsQueryWrapper);
        if(object == null)
            throw new NotFoundException("folder not found");
        else
            delete(object.getId());
    }
}
