package com.xmap.ar.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.FolderMapper;
import com.xmap.ar.mapper.ObjMapper;
import com.xmap.ar.mapper.TagLinkMapper;
import com.xmap.ar.mapper.TagMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ObjService {
    @Autowired
    private ObjMapper objMapper;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagLinkMapper tagLinkMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Obj addObj(Obj obj, int userId) throws Exception{
        QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
        if(obj.getFolder() != null) {
            queryWrapper.eq("uid", obj.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new NotFoundException("folder");
        }
        obj.setAuthor(userId);
        String uuid = "OBJ" + UUID.randomUUID().toString().replace("-", "");
        obj.setUid(uuid);
        obj.setCreateTime(get_time());
        obj.setContents(JSON.toJSONString(obj.getContent()));
        objMapper.insert(obj);
        return obj;
    }

    public List<Obj> selectALL() {
        QueryWrapper<Obj> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Obj> objs = objMapper.selectList(wrapper);
        return modifyList(objs);
    }

    public Obj selectById(int id) {
        Obj obj = objMapper.selectById(id);
        return modify(obj);
    }

    public Obj selectByUUid(String uuid) throws Exception{
        QueryWrapper<Obj> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uuid);
        queryWrapper.ne("delete", true);
        Obj obj = objMapper.selectOne(queryWrapper);
        if(obj == null)
            throw new NotFoundException("obj");
        return modify(obj);
    }

    public List<Obj> selectByCreator(int id) {
        QueryWrapper<Obj> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", id);
        List<Obj> objs = objMapper.selectList(queryWrapper);
        return modifyList(objs);
    }

    

    public Page<Obj> selectByPage(long page, long per_page) throws Exception {
        QueryWrapper<Obj> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return getPage(page, per_page, wrapper);
    }


    public Page<Obj> selectByPageSort(long page, long per_page, String sort, Boolean desc) throws Exception {
        QueryWrapper<Obj> wrapper = new QueryWrapper<>();
        if(desc)
            wrapper.orderByDesc(sort);
        else
            wrapper.orderByAsc(sort);
        return getPage(page, per_page, wrapper);
    }

    @NotNull
    private Page<Obj> getPage(Long current, Long size, QueryWrapper<Obj> wrapper) throws Exception {
        wrapper.ne("delete", true);
        Page<Obj> objPage = new Page<>(current,size);
        Page<Obj> objList = objMapper.selectPage(objPage, wrapper);
        List<Obj> res = modifyList(objList.getRecords());
        objList.setRecords(res);
        return objList;
    }
    
    private List<Obj> modifyList(List<Obj> objList) {
        List<Obj> res = new ArrayList<>();
        for(Obj obj: objList) {
            res.add(modify(obj));
        }
        return res;
    }
    
    private Obj modify(Obj entity) {
        JSONObject json;
        json = JSONObject.parseObject(entity.getContents());
        entity.setContent(json);
        List<Tag> tags = getTag(entity);

        entity.setTags(tags);
        return entity;
    }

    private List<Tag> getTag(Obj entity) {
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

    public Obj update(Obj obj, Integer id) {
        QueryWrapper<Obj> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        objMapper.update(obj, queryWrapper);
        return selectById(id);
    }

    public void setTags(Integer id, List<String> tagUidLists) throws NotFoundException {
        Obj obj = objMapper.selectById(id);
        if (obj == null)
            throw new NotFoundException("marker");
        else {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            List<String> tags = tagLinkMapper.selectTag(obj.getUid());
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
                        tagLink.setProject(obj.getUid());
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
                        tagLinkQueryWrapper.eq("project", obj.getUid());
                        tagLinkMapper.delete(tagLinkQueryWrapper);
                    }
                }
            }
        }
    }


}
