package com.xmap.ar.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.GM2d;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.GM2dMapper;
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
public class GM2dService {
    @Autowired
    private GM2dMapper gm2dMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtil;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public GM2d addGM2d(GM2d gm2d) {
        String uuid = "GM2D" + UUID.randomUUID().toString().replace("-", "");
        if  (gm2d.getContent()!=null)
            gm2d.setContentKeys(String.join("`", gm2d.getContent()));

        gm2d.setCreateTime(get_time());
        gm2d.setUid(uuid);
        gm2dMapper.insert(gm2d);
        return gm2d;
    }

    public void deleteGM2d(int id) {
        gm2dMapper.deleteById(id);
    }

    public GM2d updateGM2d(GM2d update, int id) throws Exception{
        QueryWrapper<GM2d> gm2dQueryWrapper = new QueryWrapper<>();
        if(update.getContent() != null){
            update.setContentKeys(String.join("`", update.getContent()));
        }
        update.setUpdateTime(get_time());
        gm2dQueryWrapper.eq("id", id);
        gm2dMapper.update(update, gm2dQueryWrapper);
        return getGM2dById(id);
    }

    public GM2d getGM2dById(int id) throws Exception{
        GM2d gm2d = gm2dMapper.selectById(id);
        User user = userMapper.selectById(gm2d.getAuthor());

        if(gm2d == null)
            throw new NotFoundException("gm2d");
        setUrl(gm2d, user);
        return gm2d;
    }

    private void setUrl(GM2d gm2d, User user) throws Exception {
        if(gm2d.getContentKeys() != null){
            List<String> Content = List.of(gm2d.getContentKeys().split("`"));
            List<String> url = new ArrayList<>();
            for(String key:Content){
                url.add(minioUtil.getPresignedObjectUrl(user.getUid(), key, 7));
            }
            gm2d.setContent(url);
        }
    }

    public List<GM2d> getGM2dByAuthor(int aid) throws Exception {
        QueryWrapper<GM2d> gm2dQueryWrapper = new QueryWrapper<>();
        gm2dQueryWrapper.eq("author", aid);
        gm2dQueryWrapper.ne("delete", true);
        List<GM2d> gm2ds = gm2dMapper.selectList(gm2dQueryWrapper);
        User user = userMapper.selectById(aid);
        for (GM2d gm2d : gm2ds) {
            setUrl(gm2d, user);
        }
        return gm2ds;
    }

//    public List<GM2d> filterByType(String type, int aid) {
//        QueryWrapper<GM2d> gm2dQueryWrapper = new QueryWrapper<>();
//        gm2dQueryWrapper.eq("type", type);
//        gm2dQueryWrapper.eq("author", aid);
//        List<GM2d> gm2ds = gm2dMapper.selectList(gm2dQueryWrapper);
//        for (GM2d gm2d : gm2ds) {
//            if(gm2d.getContentKeys()!=null)
//                setUrl(gm2d, user);
//        }
//        return gm2ds;
//    }

    public GM2d getGM2dByUuid(String uuid) throws Exception {
        QueryWrapper<GM2d> gm2dQueryWrapper = new QueryWrapper<>();
        gm2dQueryWrapper.eq("uid", uuid);
        gm2dQueryWrapper.ne("delete", true);
        GM2d gm2d = gm2dMapper.selectOne(gm2dQueryWrapper);
        if (gm2d == null)
            throw new NotFoundException("gm2d");

        return getGM2dById(gm2d.getId());
    }
}
