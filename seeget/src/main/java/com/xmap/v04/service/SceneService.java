package com.xmap.v04.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xmap.v04.entity.*;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SceneService {
    @Autowired
    SceneMapper sceneMapper;

    @Autowired
    DataPackageMapper dataPackageMapper;

    @Autowired
    SceneToDPMapper sceneToDPMapper;

    @Autowired
    SceneToTagMapper sceneToTagMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    DataBlockMapper dataBlockMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Scene add(@NotNull Scene scene) {
        String uuid = "SCENE" + UUID.randomUUID().toString().replace("-", "");
        scene.setUuid(uuid);
        scene.setCreateTime(get_time());
        sceneMapper.insert(scene);
        if(scene.getDataPackages() != null) {
            for(DataPackage dp: scene.getDataPackages()) {
                SceneToDP sceneToDP =  new SceneToDP();
                sceneToDP.setSceneId(scene.getId());
                sceneToDP.setDatapackageId(dp.getId());
                sceneToDPMapper.insert(sceneToDP);
            }
        }
        if(scene.getTags() != null) {
            for(Tag tag: scene.getTags()) {
                SceneToTag sceneToTag = new SceneToTag();
                sceneToTag.setSceneId(scene.getId());
                sceneToTag.setTagId(tag.getId());
                sceneToTagMapper.insert(sceneToTag);
            }
        }
        return scene;
    }

    public void delete(int id) throws NotFoundException {
        Scene scene = getById(id);
        QueryWrapper<SceneToTag> sceneToTagQueryWrapper = new QueryWrapper<>();
        sceneToTagQueryWrapper.eq("scene_id", scene.getId());
        List<SceneToTag> sceneToTags = sceneToTagMapper.selectList(sceneToTagQueryWrapper);
        if(sceneToTags!=null){
            for(SceneToTag sceneToTag :sceneToTags) {
                sceneToTagMapper.deleteById(sceneToTag);
            }
        }

        QueryWrapper<SceneToDP> sceneToDPQueryWrapper = new QueryWrapper<>();
        sceneToDPQueryWrapper.eq("scene_id", scene.getId());
        List<SceneToDP> sceneToDPS = sceneToDPMapper.selectList(sceneToDPQueryWrapper);
        if(sceneToDPS!=null){
            for(SceneToDP sceneToDP: sceneToDPS){
                sceneToDPMapper.deleteById(sceneToDP);
            }
        }
        sceneMapper.deleteById(id);
    }

    public void deleteByUuid(String uuid) throws NotFoundException {
        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
        sceneQueryWrapper.orderByDesc("create_time");
        sceneQueryWrapper.eq("uuid", uuid);
        Scene scene = sceneMapper.selectOne(sceneQueryWrapper);
        delete(scene.getId());
    }

    public DataPackage getDetail(Integer id) {
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
    public Scene getById(int id) throws NotFoundException {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null) {
            throw new NotFoundException("scene not found");
        }
        QueryWrapper<SceneToDP> sceneToDPQueryWrapper = new QueryWrapper<>();
        sceneToDPQueryWrapper.eq("scene_id", id);
        List<SceneToDP> sceneToDPS = sceneToDPMapper.selectList(sceneToDPQueryWrapper);

        List<DataPackage> dataPackageList = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (SceneToDP sceneToDP: sceneToDPS) {
            DataPackage dataPackage = getDetail(sceneToDP.getDatapackageId());
            dataPackageList.add(dataPackage);
            types.addAll(dataPackage.getTypes());
        }
        scene.setDataPackages(dataPackageList);

        QueryWrapper<SceneToTag> sceneToTagQueryWrapper = new QueryWrapper<>();
        sceneToTagQueryWrapper.eq("scene_id", scene.getId());
        List<SceneToTag> sceneToTags = sceneToTagMapper.selectList(sceneToTagQueryWrapper);
        List<Tag> tags = new ArrayList<>();
        if(sceneToTags!=null){
            for(SceneToTag sceneToTag :sceneToTags) {
                tags.add(tagMapper.selectById(sceneToTag.getTagId()));
            }
        }
        scene.setTags(tags);

        return scene;
    }

    public Scene getByUuid(String uuid) throws NotFoundException {
        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
        sceneQueryWrapper.orderByDesc("create_time");
        sceneQueryWrapper.eq("uuid", uuid);
        Scene scene = sceneMapper.selectOne(sceneQueryWrapper);
        return getById(scene.getId());
    }

    public List<Scene> getPublicScene() throws NotFoundException {
        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
        sceneQueryWrapper.orderByDesc("create_time");
        sceneQueryWrapper.ne("is_public", 2);
        List<Scene> scenes = sceneMapper.selectList(sceneQueryWrapper);
        List<Scene> result = new ArrayList<>();
        for(Scene scene: scenes) {
            result.add(getById(scene.getId()));
        }
        return result;
    }

    public List<Scene> getSelfScene(int author) throws NotFoundException {
        QueryWrapper<Scene> sceneQueryWrapper = new QueryWrapper<>();
        sceneQueryWrapper.orderByDesc("create_time");
        sceneQueryWrapper.eq("author", author);
        List<Scene> scenes = sceneMapper.selectList(sceneQueryWrapper);
        List<Scene> result = new ArrayList<>();
        for(Scene scene: scenes) {
            result.add(getById(scene.getId()));
        }
        return result;
    }


    public void addDp(Integer id, int dpId) {
        SceneToDP sceneToDP = new SceneToDP();
        sceneToDP.setSceneId(id);
        sceneToDP.setDatapackageId(dpId);
        sceneToDPMapper.insert(sceneToDP);
    }

    public void addTag(Integer id, int tagId) {
        SceneToTag sceneToTag = new SceneToTag();
        sceneToTag.setSceneId(id);
        sceneToTag.setTagId(tagId);
        sceneToTagMapper.insert(sceneToTag);
    }

    public void deleteDp(Integer id, int dpId) {
        QueryWrapper<SceneToDP> sceneToDPQueryWrapper = new QueryWrapper<>();
        sceneToDPQueryWrapper.eq("scene_id", id);
        sceneToDPQueryWrapper.eq("data_package_id", dpId);
        sceneToDPMapper.delete(sceneToDPQueryWrapper);
    }

    public Scene update(int id, Scene scene) throws NotFoundException {

        Timestamp time = get_time();
        scene.setUpdateTime(time);
        UpdateWrapper<Scene> sceneUpdateWrapper = new UpdateWrapper<>();
        sceneUpdateWrapper.eq("id", id);
        sceneMapper.update(scene, sceneUpdateWrapper);
        return getById(scene.getId());
    }
}
