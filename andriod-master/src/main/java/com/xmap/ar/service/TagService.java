package com.xmap.ar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.Tag;
import com.xmap.ar.entity.TagLink;
import com.xmap.ar.mapper.TagLinkMapper;
import com.xmap.ar.mapper.TagMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagLinkMapper tagLinkMapper;

    public Tag addTag(Tag tag) {
        String uuid = "TAG" + UUID.randomUUID().toString().replace("-", "");
        tag.setUid(uuid);
        tagMapper.insert(tag);
        return tag;
    }

    public List<Tag> getByAuthor(int id) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("author", id);
        return tagMapper.selectList(tagQueryWrapper);
    }

    public List<Tag> getAll() {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.orderByAsc("id");
        return tagMapper.selectList(tagQueryWrapper);
    }

    public Tag update(Tag tag, int id) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("id", id);
        tagMapper.update(tag, tagQueryWrapper);
        return tagMapper.selectById(id);
    }

    public Tag selectByUuid(String uuid) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("uid", uuid);
        return tagMapper.selectOne(tagQueryWrapper);
    }

    public void deleteById(int id) {
        tagMapper.deleteById(id);
    }

    public List<TagLink> selectLink(String uuid) {
        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
        tagLinkQueryWrapper.eq("tag", uuid);
        return tagLinkMapper.selectList(tagLinkQueryWrapper);
    }

    public void remove(String uuid) {
        QueryWrapper<TagLink> tagLinkQueryWrapper = new QueryWrapper<>();
        tagLinkQueryWrapper.eq("tag", uuid);
        tagLinkMapper.delete(tagLinkQueryWrapper);
    }
}
