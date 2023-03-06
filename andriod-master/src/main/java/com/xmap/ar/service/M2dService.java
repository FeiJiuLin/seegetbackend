package com.xmap.ar.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.exception.ParamsMissException;
import com.xmap.ar.mapper.*;
import com.xmap.ar.util.MinioUtils;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class M2dService {
    @Autowired
    private M2dMapper m2dMapper;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private TagLinkMapper tagLinkMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MinioUtils minioUtil;

    @Autowired
    private UserMapper userMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public M2d addMaker(M2d marker, int userId) throws Exception{
        QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
        if(marker.getFolder() != null) {
            queryWrapper.eq("uid", marker.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new ParamsMissException("folder uid wrong");
        }
        marker.setAuthor(userId);
        String uuid = "M2D" + UUID.randomUUID().toString().replace("-", "");
        marker.setUid(uuid);
        marker.setCreateTime(get_time());
        m2dMapper.insert(marker);
        return marker;
    }

    private void modify(@NotNull M2d marker) throws Exception{
        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
        tagLinkQueryWrapper.eq("project", marker.getUid());
        List<TagLink> tagLinks = tagLinkMapper.selectList(tagLinkQueryWrapper);
        if(tagLinks!=null){
            List<Tag> tags = new ArrayList<>();
            for(TagLink tagLink: tagLinks){
                QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
                tagQueryWrapper.eq("uid", tagLink.getTag());
                Tag tag = tagMapper.selectOne(tagQueryWrapper);
                tags.add(tag);
            }
            marker.setTagList(tags);
        }

        User author = userMapper.selectById(marker.getAuthor());

        if(marker.getSrc() != null){
            String url = minioUtil.getPresignedObjectUrl(author.getUid(), marker.getSrc(), 7);
            marker.setSrc(url);
        }

        if(marker.getEdited() != null) {
            String url = minioUtil.getPresignedObjectUrl(author.getUid(), marker.getSrc(), 7);
            marker.setSrc(url);
        }

    }

    public List<M2d> selectALL() throws Exception{
        QueryWrapper<M2d> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.ne("delete", true);
        List<M2d> m2dList = m2dMapper.selectList(wrapper);
        return modifyList(m2dList);
    }

    public Page<M2d> selectByPage(Long current, Long size) throws Exception {
        QueryWrapper<M2d> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return getM2dPage(current, size, wrapper);
    }

    public Page<M2d> selectByPageSort(Long current, Long size, String sort, Boolean desc) throws Exception {
        QueryWrapper<M2d> wrapper = new QueryWrapper<>();
        wrapper.ne("delete", true);
        Page<M2d> objPage = new Page<>(current,size);
        if(desc)
            objPage.setOrders(OrderItem.descs(sort));
        else
            objPage.setOrders(OrderItem.ascs(sort));

        Page<M2d> objList = m2dMapper.selectPage(objPage, wrapper);
        List<M2d> res = modifyList(objList.getRecords());
        objList.setRecords(res);
        return objList;
    }

    @NotNull
    private Page<M2d> getM2dPage(Long current, Long size, QueryWrapper<M2d> wrapper) throws Exception {
        wrapper.ne("delete", true);
        Page<M2d> m2dPage = new Page<>(current,size);
        Page<M2d> m2dList = m2dMapper.selectPage(m2dPage, wrapper);
        List<M2d> res = modifyList(m2dList.getRecords());
        m2dPage.setRecords(res);
        return m2dList;
    }

    public List<M2d> selectByAuthor(Integer id) throws Exception{
        QueryWrapper<M2d> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("author", id);
        List<M2d> m2dList = m2dMapper.selectList(wrapper);
        return modifyList(m2dList);
    }

    @NotNull
    private List<M2d> modifyList(List<M2d> m2dList) throws Exception {

        List<M2d> res = new ArrayList<>();
        for (M2d m2d : m2dList) {
            modify(m2d);
            res.add(m2d);
        }
        return res;
    }

    public void delete(int id) throws Exception{
        M2d marker =  m2dMapper.selectById(id);
        if(marker == null)
            throw new NotFoundException("marker not found");
        else
            m2dMapper.deleteById(id);
    }

    public M2d selectById(int id) throws Exception {
        M2d marker =  m2dMapper.selectById(id);
        modify(marker);
        return marker;
    }


    public M2d selectByUUid(String uuid) throws Exception {
        QueryWrapper<M2d> wrapper= new QueryWrapper<>();
        wrapper.eq("uid", uuid);
        wrapper.ne("delete", true);
        M2d marker = m2dMapper.selectOne(wrapper);
        if(marker == null)
            throw new NotFoundException("marker");
        return selectById(marker.getId());
    }

    public M2d update(@NotNull M2d marker, int id) throws Exception{
        QueryWrapper<M2d> m2dQueryWrapper = new QueryWrapper<>();

        if(marker.getFolder() != null) {
            QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", marker.getFolder());
            Folder folder = folderMapper.selectOne(queryWrapper);
            if (folder == null)
                throw new ParamsMissException("folder uid wrong");
        }

        marker.setUpdateTime(get_time());
        m2dQueryWrapper.eq("id", id);
        m2dMapper.update(marker, m2dQueryWrapper);
        return selectById(id);
    }

    public void setTags(Integer id, List<String> tagUidLists) throws Exception{
        M2d marker = m2dMapper.selectById(id);
        if (marker == null)
            throw new NotFoundException("marker");
        else {
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            List<String> tags = tagLinkMapper.selectTag(marker.getUid());

//            if(tags.size() == tagUidLists.size()) {
//                List<String> copyUid = tagUidLists;
//                copyUid.removeAll(tags);
//                tagQueryWrapper.eq("uid", copyUid.get(0));
//                Tag addtag = tagMapper.selectOne(tagQueryWrapper);
//                if (addtag == null)
//                    throw new NotFoundException("tag with uid:"+copyUid.get(0));
//                else {
//                    TagLink tagLink = new TagLink();
//                    tagLink.setTag(addtag.getUid());
//                    tagLink.setProject(marker.getUid());
//                    tagLinkMapper.insert(tagLink);
//            }

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
                        tagLink.setProject(marker.getUid());
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
                        tagLinkQueryWrapper.eq("project", marker.getUid());
                        tagLinkMapper.delete(tagLinkQueryWrapper);
                    }
                }
            }
        }

    }


//    public Page<Marker> selectPage() {
//        QueryWrapper<Marker> wrapper= new QueryWrapper<>();
//        wrapper.orderByDesc("create_time");
//        Page<Marker> p = new Page<>();
//        p.
//        return markerMapper.selectPage(p, wrapper);
//    }
}
