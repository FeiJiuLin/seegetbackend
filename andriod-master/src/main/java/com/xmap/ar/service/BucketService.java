package com.xmap.ar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.Bucket;
import com.xmap.ar.mapper.BucketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BucketService {
    @Autowired
    BucketMapper mapper;

    public Bucket addBucket(String bucketName, int owner) {
        Bucket bucket = new Bucket();
        bucket.setName(bucketName);
        bucket.setOwner(owner);
        mapper.insert(bucket);
        return bucket;
    }

    public Bucket getBucketByName(String bucketName) {
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", bucketName);
        return mapper.selectOne(queryWrapper);
    }

    public void deleteBucketByName(String bucketName) {
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", bucketName);
        mapper.delete(queryWrapper);
    }

    public Bucket getBucketByOwner(int id) {
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", id);
        return mapper.selectOne(queryWrapper);
    }

    public Bucket getBucketById(Integer bucketId) {
        return mapper.selectById(bucketId);
    }
}
