package com.xmap.ar.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.MM2d;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.MM2dMapper;
import com.xmap.ar.mapper.UserMapper;
import com.xmap.ar.util.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MM2dService {
    @Autowired
    private MM2dMapper mm2dMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtil;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public MM2d addMM2d(MM2d mm2d) {
        String uuid = "MM2D" + UUID.randomUUID().toString().replace("-", "");
        if  (mm2d.getContent()!=null)
            mm2d.setContentKeys(String.join("`", mm2d.getContent()));

        mm2d.setCreateTime(get_time());
        mm2d.setUid(uuid);
        mm2dMapper.insert(mm2d);
        return mm2d;
    }

    public void deleteMM2d(int id) {
        mm2dMapper.deleteById(id);
    }

    public MM2d updateMM2d(MM2d update, int id) throws Exception{
        QueryWrapper<MM2d> mm2dQueryWrapper = new QueryWrapper<>();
        if(update.getContent() != null){
            update.setContentKeys(String.join("`", update.getContent()));
        }
        update.setUpdateTime(get_time());
        mm2dQueryWrapper.eq("id", id);
        mm2dMapper.update(update, mm2dQueryWrapper);
        return getMM2dById(id);
    }

    public MM2d getMM2dById(int id) throws Exception{
        MM2d mm2d = mm2dMapper.selectById(id);
        User user = userMapper.selectById(mm2d.getAuthor());

        if(mm2d == null)
            throw new NotFoundException("mm2d");
        setUrl(mm2d, user);
        return mm2d;
    }

    private void setUrl(MM2d mm2d, User user) throws Exception {
        if(mm2d.getContentKeys() != null){
            List<String> Content = List.of(mm2d.getContentKeys().split("`"));
            List<String> url = new ArrayList<>();
            for(String key:Content){
                url.add(minioUtil.getPresignedObjectUrl(user.getUid(), key, 7));
            }
            mm2d.setContent(url);
        }
    }

    public List<MM2d> getMM2dByAuthor(int aid) throws Exception {
        QueryWrapper<MM2d> mm2dQueryWrapper = new QueryWrapper<>();
        mm2dQueryWrapper.eq("author", aid);
        mm2dQueryWrapper.ne("delete", true);
        List<MM2d> mm2ds = mm2dMapper.selectList(mm2dQueryWrapper);
        User user = userMapper.selectById(aid);
        for (MM2d mm2d : mm2ds) {
            setUrl(mm2d, user);
        }
        return mm2ds;
    }
//    public List<MM2d> filterByType(String type, int aid) {
//        QueryWrapper<MM2d> mm2dQueryWrapper = new QueryWrapper<>();
//        mm2dQueryWrapper.eq("type", type);
//        mm2dQueryWrapper.eq("author", aid);
//        List<MM2d> mm2ds = mm2dMapper.selectList(mm2dQueryWrapper);
//        for (MM2d mm2d : mm2ds) {
//            if(mm2d.getContentKeys()!=null)
//                setUrl(mm2d, user);
//        }
//        return mm2ds;
//    }

    public MM2d getMM2dByUuid(String uuid) throws Exception {
        QueryWrapper<MM2d> mm2dQueryWrapper = new QueryWrapper<>();
        mm2dQueryWrapper.eq("uid", uuid);
        mm2dQueryWrapper.ne("delete", true);
        MM2d mm2d = mm2dMapper.selectOne(mm2dQueryWrapper);
        if (mm2d == null)
            throw new NotFoundException("mm2d");

        return getMM2dById(mm2d.getId());
    }
}