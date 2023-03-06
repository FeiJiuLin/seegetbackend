package com.xmap.ar.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.exception.ParamsMissException;
import com.xmap.ar.mapper.FolderMapper;
import com.xmap.ar.mapper.M3dMapper;
import com.xmap.ar.mapper.TagLinkMapper;
import com.xmap.ar.mapper.TagMapper;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class M3dService {
    @Autowired
    private M3dMapper m3dMapper;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private TagLinkMapper tagLinkMapper;

    @Autowired
    private TagMapper tagMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public M3d addMaker(M3d marker, int userId) throws Exception{
        QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
        if(marker.getFolder() != null) {
            queryWrapper.eq("uid", marker.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new NotFoundException("folder");
        }
        marker.setAuthor(userId);
        marker.setContentJson(JSON.toJSONString(marker.getContent()));
        String uuid = "M3D" + UUID.randomUUID().toString().replace("-", "");
        marker.setUid(uuid);
        marker.setCreateTime(get_time());
        m3dMapper.insert(marker);
        return marker;
    }

    public List<M3d> selectALL() throws Exception {
        QueryWrapper<M3d> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.ne("delete", true);
        List<M3d> m3dList = m3dMapper.selectList(wrapper);
        return modifyList(m3dList);
    }

    public Page<M3d> selectByPage(Long current, Long size) throws Exception {
        QueryWrapper<M3d> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return getM3dPage(current, size, wrapper);
    }

    public Page<M3d> selectByPageSort(Long current, Long size, String sort, Boolean desc) throws Exception {
        QueryWrapper<M3d> wrapper= new QueryWrapper<>();
        if(desc)
            wrapper.orderByDesc(sort);
        else wrapper.orderByAsc(sort);

        return getM3dPage(current, size, wrapper);
    }

    @NotNull
    private Page<M3d> getM3dPage(Long current, Long size, QueryWrapper<M3d> wrapper) throws Exception {
        wrapper.ne("delete", true);
        Page<M3d> m3dPage = new Page<>(current,size);
        Page<M3d> m3dList = m3dMapper.selectPage(m3dPage, wrapper);
        List<M3d> res = modifyList(m3dList.getRecords());
        m3dPage.setRecords(res);
        return m3dList;
    }

    public List<M3d> selectByCreator(int id) throws Exception {
        QueryWrapper<M3d> wrapper= new QueryWrapper<>();
        wrapper.eq("author", id);
        List<M3d> m3dList = m3dMapper.selectList(wrapper);
        return modifyList(m3dList);
    }

    public void delete(int id) throws Exception{
        M3d marker =  m3dMapper.selectById(id);
        if(marker == null)
            throw new NotFoundException("marker not found");
        else
            m3dMapper.deleteById(id);
    }

    public M3d selectById(int id) throws Exception{
        M3d marker = m3dMapper.selectById(id);

        if(marker == null)
            throw new NotFoundException("marker");
        JSONObject json;
        json = JSONObject.parseObject(marker.getContentJson());
        marker.setContent(json);
        List<Tag> tags = getTag(marker);
        marker.setTagList(tags);
        return marker;
    }

    private List<M3d> modifyList(List<M3d> m3dList) {
        List<M3d> res = new ArrayList<>();
        for(M3d m3d: m3dList) {
            JSONObject json;
            json = JSONObject.parseObject(m3d.getContentJson());
            m3d.setContent(json);
            List<Tag> tags = getTag(m3d);

            m3d.setTagList(tags);
            res.add(m3d);
        }
        return res;
    }

    private List<Tag> getTag(M3d marker) {
        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
        tagLinkQueryWrapper.eq("project", marker.getUid());
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

    public M3d selectByUUid(String uuid) throws Exception {
        QueryWrapper<M3d> wrapper= new QueryWrapper<>();
        wrapper.eq("uid", uuid);
        wrapper.ne("delete", true);
        M3d marker = m3dMapper.selectOne(wrapper);
        if(marker == null)
            throw new NotFoundException("marker");
        return selectById(marker.getId());
    }

    public M3d update(@NotNull M3d marker, int id) throws Exception{
        QueryWrapper<M3d> m3dQueryWrapper = new QueryWrapper<>();

        if(marker.getFolder() != null) {
            QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", marker.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new NotFoundException("folder");
        }

        marker.setUpdateTime(get_time());
        m3dQueryWrapper.eq("id", id);
        m3dMapper.update(marker, m3dQueryWrapper);
        return selectById(id);
    }

    public void setTags(Integer id, List<String> tagUidLists) throws Exception {
        M3d marker = m3dMapper.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker");
        else {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            List<String> tags = tagLinkMapper.selectTag(marker.getUid());

//            if (tags.size() == tagUidLists.size()) {
//                List<String> copyUid = tagUidLists;
//                copyUid.removeAll(tags);
//                tagQueryWrapper.eq("uid", copyUid.get(0));
//                Tag addtag = tagMapper.selectOne(tagQueryWrapper);
//                if (addtag == null)
//                    throw new NotFoundException("tag with uid:" + copyUid.get(0));
//                else {
//                    TagLink tagLink = new TagLink();
//                    tagLink.setTag(addtag.getUid());
//                    tagLink.setProject(marker.getUid());
//                    tagLinkMapper.insert(tagLink);
//                }

                if (tags.size() < tagUidLists.size()) {
                    tagUidLists.removeAll(tags);
                    for (String tagUid : tagUidLists) {
                        tagQueryWrapper.eq("uid", tagUid);
                        Tag tag = tagMapper.selectOne(tagQueryWrapper);
                        if (tag == null)
                            throw new NotFoundException("tag with uid:" + tagUid);
                        else {
                            TagLink tagLink = new TagLink();
                            tagLink.setTag(tagUid);
                            tagLink.setProject(marker.getUid());
                            tagLinkMapper.insert(tagLink);
                        }
                    }
                } else {
                    tags.removeAll(tagUidLists);
                    for (String tagUid : tags) {
                        tagQueryWrapper.eq("uid", tagUid);
                        Tag tag = tagMapper.selectOne(tagQueryWrapper);
                        if (tag == null)
                            throw new NotFoundException("tag with uid:" + tagUid);
                        else {
                            QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
                            tagLinkQueryWrapper.eq("tag", tagUid);
                            tagLinkQueryWrapper.eq("project", marker.getUid());
                            tagLinkMapper.delete(tagLinkQueryWrapper);
                        }
                    }
                }
        }
    }
}



