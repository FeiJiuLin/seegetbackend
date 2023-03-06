package com.xmap.ar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.*;
import com.xmap.ar.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecycleService {
    @Autowired
    private RecycleMapper recycleMapper;

    @Autowired
    private M2dMapper m2dMapper;

    @Autowired
    private M3dMapper m3dMapper;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private ObjMapper objMapper;

    @Autowired
    private SceneMapper sceneMapper;

    @Autowired
    private MM2dMapper mm2dMapper;

    @Autowired
    private MM3dMapper mm3dMapper;

    @Autowired
    private GM2dMapper gm2dMapper;

    @Autowired
    private GM3dMapper gm3dMapper;

    public List<Recycle> selectByAuthor(int type, int author) {
        QueryWrapper<Recycle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("author", author);
        return recycleMapper.selectList(queryWrapper);
    }

    public void deleteM2d(Integer id) {
        List<Recycle> recycles = selectByAuthor(1, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<M2d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            m2dMapper.delete(queryWrapper);
        }
    }

    public void deleteM3d(Integer id) {
        List<Recycle> recycles = selectByAuthor(2, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<M3d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            m3dMapper.delete(queryWrapper);
        }
    }

    public void deleteObj(Integer id) {
        List<Recycle> recycles = selectByAuthor(3, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<Obj> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            objMapper.delete(queryWrapper);
        }
    }

    public void deleteScene(Integer id) {
        List<Recycle> recycles = selectByAuthor(4, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            sceneMapper.delete(queryWrapper);
        }
    }

    public void deleteFolder(Integer id) {
        List<Recycle> recycles = selectByAuthor(5, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<Folder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            folderMapper.delete(queryWrapper);
        }
    }

    public void deleteMM2D(Integer id) {
        List<Recycle> recycles = selectByAuthor(6, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<MM2d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            mm2dMapper.delete(queryWrapper);
        }
    }

    public void deleteMM3D(Integer id) {
        List<Recycle> recycles = selectByAuthor(7, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<MM3d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            mm3dMapper.delete(queryWrapper);
        }
    }

    public void deleteGM2D(Integer id) {
        List<Recycle> recycles = selectByAuthor(8, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<GM2d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            gm2dMapper.delete(queryWrapper);
        }
    }

    public void deleteGM3D(Integer id) {
        List<Recycle> recycles = selectByAuthor(9, id);
        for(Recycle recycle: recycles) {
            QueryWrapper<GM3d> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", recycle.getUid());
            gm3dMapper.delete(queryWrapper);
        }
    }
    public void add(Recycle recycle) {
        recycleMapper.insert(recycle);
    }


}
