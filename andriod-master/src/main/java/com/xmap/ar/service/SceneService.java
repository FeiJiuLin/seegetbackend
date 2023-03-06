package com.xmap.ar.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.FolderMapper;
import com.xmap.ar.mapper.SceneMapper;
import com.xmap.ar.mapper.TagLinkMapper;
import com.xmap.ar.mapper.TagMapper;
import org.checkerframework.checker.units.qual.A;
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
    private SceneMapper sceneMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagLinkMapper tagLinkMapper;

    @Autowired
    private FolderMapper folderMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Scene addScene(Scene scene, int id) throws Exception{
        QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
        if(scene.getFolder() != null) {
            queryWrapper.eq("uid", scene.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new NotFoundException("folder");
        }
        scene.setAuthor(id);
        String uuid = "SCENE" + UUID.randomUUID().toString().replace("-", "");
        scene.setUid(uuid);
        scene.setCreateTime(get_time());
        scene.setContents(JSON.toJSONString(scene.getContent()));
        sceneMapper.insert(scene);
        return scene;
    }

    public List<Scene> selectALL() {
        QueryWrapper<Scene> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Scene> scenes = sceneMapper.selectList(wrapper);
        return modifyList(scenes);
    }

    public Scene selectById(int id) {
        Scene scene = sceneMapper.selectById(id);
        return modify(scene);
    }

    public Page<Scene> selectByPage(long page, long per_page) {
        QueryWrapper<Scene> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return getPage(page, per_page, wrapper);
    }

    public Page<Scene> selectByPageSort(long page, long per_page, String sort, Boolean desc) {
        QueryWrapper<Scene> wrapper = new QueryWrapper<>();
        if(desc)
            wrapper.orderByDesc(sort);
        else
            wrapper.orderByAsc(sort);
        return getPage(page, per_page, wrapper);
    }

    private Page<Scene> getPage(long page, long per_page, QueryWrapper<Scene> wrapper) {
        wrapper.ne("delete", true);
        Page<Scene> scenePage = new Page<>(page, per_page);
        Page<Scene> sceneList = sceneMapper.selectPage(scenePage, wrapper);
        List<Scene> res = modifyList(sceneList.getRecords());
        scenePage.setRecords(res);
        return sceneList;
    }

    private List<Scene> modifyList(List<Scene> sceneList) {
        List<Scene> res = new ArrayList<>();
        for(Scene scene: sceneList) {
            res.add(modify(scene));
        }
        return res;
    }

    private Scene modify(Scene entity) {
        JSONObject json;
        json = JSONObject.parseObject(entity.getContents());
        entity.setContent(json);
        List<Tag> tags = getTag(entity);

        entity.setTags(tags);
        return entity;
    }

    private List<Tag> getTag(Scene entity) {
        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
        tagLinkQueryWrapper.eq("project", entity.getUid());
        List<TagLink> tagLinks = tagLinkMapper.selectList(tagLinkQueryWrapper);
        List<Tag> tags = new ArrayList<>();
        if(tagLinks !=null){
            for(TagLink tagLink: tagLinks){
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("uid", tagLink.getTag());
                tags.add(tagMapper.selectOne(tagQueryWrapper));
            }
        }
        return tags;
    }

    public Scene selectByUUid(String uuid) throws Exception{
        QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uuid);
        queryWrapper.ne("delete", true);
        Scene scene = sceneMapper.selectOne(queryWrapper);
        if(scene == null)
            throw new NotFoundException("scene");
        return selectById(scene.getId());
    }

    public List<Scene> selectByCreator(int id) {
        QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", id);
        List<Scene> scenes = sceneMapper.selectList(queryWrapper);
        return modifyList(scenes);

    }

    public Scene update(Scene scene, Integer id) {
        QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        sceneMapper.update(scene, queryWrapper);
        return selectById(id);
    }

    public void setTags(Integer id, List<String> tagUidLists) throws Exception{
        Scene scene = sceneMapper.selectById(id);
        if (scene == null)
            throw new NotFoundException("marker");
        else {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            List<String> tags = tagLinkMapper.selectTag(scene.getUid());
            if(tags.size() < tagUidLists.size()) {
                tagUidLists.removeAll(tags);
                for( String tagUid: tagUidLists) {
                    tagQueryWrapper.eq("uid", tagUid);
                    Tag tag = tagMapper.selectOne(tagQueryWrapper);
                    if (tag == null)
                        throw new NotFoundException("tag with uid:"+tagUid);
                    else {
                        TagLink tagLink = new TagLink();
                        tagLink.setTag(tagUid);
                        tagLink.setProject(scene.getUid());
                        tagLinkMapper.insert(tagLink);
                    }
                }
            } else {
                tags.removeAll(tagUidLists);
                for( String tagUid: tags) {
                    tagQueryWrapper.eq("uid", tagUid);
                    Tag tag = tagMapper.selectOne(tagQueryWrapper);
                    if (tag == null)
                        throw new NotFoundException("tag with uid:"+tagUid);
                    else {
                        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
                        tagLinkQueryWrapper.eq("tag", tagUid);
                        tagLinkQueryWrapper.eq("project", scene.getUid());
                        tagLinkMapper.delete(tagLinkQueryWrapper);
                    }
                }
            }
        }
    }
}
