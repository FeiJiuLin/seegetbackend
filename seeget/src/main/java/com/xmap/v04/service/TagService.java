package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xmap.v04.entity.Tag;
import com.xmap.v04.mapper.TagMapper;
import com.xmap.v04.mapper.UserMapper;
import com.xmap.v04.models.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Tag findTagByUuid(String uuid){
        return tagMapper.findTagByUuid(uuid);
    }

    public Tag getById(Integer id){
        return tagMapper.selectById(id);
    }

    public Tag add(TagModel tagModel) {
        Tag tag = new Tag();
        String uuid = "TAG" + UUID.randomUUID().toString().replace("-", "");
        tag.setContent(tagModel.getContent());
        tag.setType(tagModel.getType());
        tag.setCreateTime(get_time());
        tag.setAuthor(tagModel.getAuthor());
        tag.setUuid(uuid);
        tagMapper.insert(tag);
        return tag;
    }

    public Tag update(Tag tag) {
        tag.setUpdateTime(get_time());
        UpdateWrapper<Tag> tagUpdateWrapper = new UpdateWrapper<>();
        tagUpdateWrapper.eq("uuid", tag.getUuid());
        tagMapper.update(tag, tagUpdateWrapper);
        return findTagByUuid(tag.getUuid());
    }


    public Tag findTagById(Integer id) {
        return tagMapper.selectById(id);
    }

    public void deleteTagByUuid(String tagUuid) {
        UpdateWrapper<Tag> tagUpdateWrapper = new UpdateWrapper<>();
        tagUpdateWrapper.eq("uuid", tagUuid);
        tagMapper.delete(tagUpdateWrapper);
    }

    public List<Tag> findTagByAuthor(int id) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("author", id);
        return tagMapper.selectList(tagQueryWrapper);
    }

    public List<Tag> findAllTag() {
        return tagMapper.selectAll();
    }

    public void deleteTag(Integer id) {

    }
}
