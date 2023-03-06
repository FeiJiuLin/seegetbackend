package com.xmap.ar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.*;
import com.xmap.ar.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private M2dMapper m2dMapper;

    @Autowired
    private M3dMapper m3dMapper;

    public Collection selectByType(int type, String uid, int user) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("author", user);
        return collectionMapper.selectOne(queryWrapper);
    }

    public List<Collection> selectByAuthor(int type, int user) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("author", user);
        return collectionMapper.selectList(queryWrapper);
    }

    public void deleteM2d(String uid, int user) {
        Collection collection = selectByType(1, uid, user);
        QueryWrapper<M2d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m2dMapper.delete(queryWrapper);
    }

    public void deleteM3d(String uid, int user) {
        Collection collection = selectByType(2, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteObj(String uid, int user) {
        Collection collection = selectByType(3, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteScene(String uid, int user) {
        Collection collection = selectByType(4, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteFolder(String uid, int user) {
        Collection collection = selectByType(5, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteMM2D(String uid, int user) {
        Collection collection = selectByType(6, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteMM3D(String uid, int user) {
        Collection collection = selectByType(7, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteGM2d(String uid, int user) {
        Collection collection = selectByType(8, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void deleteGM3D(String uid, int user) {
        Collection collection = selectByType(9, uid, user);
        QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", collection.getUid());
        m3dMapper.delete(queryWrapper);
    }

    public void add(Collection collection) {
        collectionMapper.insert(collection);
    }


}
