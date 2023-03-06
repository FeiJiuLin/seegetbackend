package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.v04.entity.Marker;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.BucketMapper;
import com.xmap.v04.mapper.MarkerMapper;
import com.xmap.v04.models.MarkerModel;
import com.xmap.v04.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkerService {
    @Autowired
    private MarkerMapper markerMapper;

    public Marker addMaker(MarkerModel markerModel, int userId) {
        Marker marker = new Marker();
        marker.setCreator(userId);
        marker.setDescription(markerModel.getDescription());
        marker.setCreateTime(Util.getTime());
        marker.setTitle(markerModel.getTitle());
        marker.setHeight(markerModel.getHeight());
        marker.setWidth(markerModel.getWidth());
        marker.setKey(markerModel.getKey());
        marker.setBucketId(markerModel.getBucketId());
        marker.setShare(markerModel.getShare());
        markerMapper.insert(marker);
        return marker;
    }

    public List<Marker> selectALL() {
        QueryWrapper<Marker> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return markerMapper.selectList(wrapper);
    }

    public List<Marker> selectByCreator(int id) {
        QueryWrapper<Marker> wrapper= new QueryWrapper<>();
        wrapper.eq("creator", id);
        return markerMapper.selectList(wrapper);
    }

    public void delete(int id) throws Exception{
        Marker marker =  markerMapper.selectById(id);
        if(marker == null)
            throw new NotFoundException("marker not found");
        else
            markerMapper.deleteById(id);
    }

    public Marker selectById(int id) {
        return markerMapper.selectById(id);
    }

//    public Page<Marker> selectPage() {
//        QueryWrapper<Marker> wrapper= new QueryWrapper<>();
//        wrapper.orderByDesc("create_time");
//        Page<Marker> p = new Page<>();
//        p.
//        return markerMapper.selectPage(p, wrapper);
//    }
}
