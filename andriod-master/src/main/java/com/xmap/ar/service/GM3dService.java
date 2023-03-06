package com.xmap.ar.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.GM3d;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.GM3dMapper;
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
public class GM3dService {
    @Autowired
    private GM3dMapper gm3dMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtil;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public GM3d addGM3d(GM3d gm3d) {
        String uuid = "GM3D" + UUID.randomUUID().toString().replace("-", "");
        if  (gm3d.getContent()!=null)
            gm3d.setContentKeys(String.join("`", gm3d.getContent()));

        gm3d.setCreateTime(get_time());
        gm3d.setUid(uuid);
        gm3dMapper.insert(gm3d);
        return gm3d;
    }

    public void deleteGM3d(int id) {
        gm3dMapper.deleteById(id);
    }

    public GM3d updateGM3d(GM3d update, int id) throws Exception{
        QueryWrapper<GM3d> gm3dQueryWrapper = new QueryWrapper<>();
        if(update.getContent() != null){
            update.setContentKeys(String.join("`", update.getContent()));
        }
        update.setUpdateTime(get_time());
        gm3dQueryWrapper.eq("id", id);
        gm3dMapper.update(update, gm3dQueryWrapper);
        return getGM3dById(id);
    }

    public GM3d getGM3dById(int id) throws Exception{
        GM3d gm3d = gm3dMapper.selectById(id);
        User user = userMapper.selectById(gm3d.getAuthor());

        if(gm3d == null)
            throw new NotFoundException("gm3d");
        setUrl(gm3d, user);
        return gm3d;
    }

    private void setUrl(GM3d gm3d, User user) throws Exception {
        if(gm3d.getContentKeys() != null){
            List<String> Content = List.of(gm3d.getContentKeys().split("`"));
            List<String> url = new ArrayList<>();
            for(String key:Content){
                url.add(minioUtil.getPresignedObjectUrl(user.getUid(), key, 7));
            }
            gm3d.setContent(url);
        }
    }

    public List<GM3d> getGM3dByAuthor(int aid) throws Exception {
        QueryWrapper<GM3d> gm3dQueryWrapper = new QueryWrapper<>();
        gm3dQueryWrapper.eq("author", aid);
        gm3dQueryWrapper.ne("delete", true);
        List<GM3d> gm3ds = gm3dMapper.selectList(gm3dQueryWrapper);
        User user = userMapper.selectById(aid);
        for (GM3d gm3d : gm3ds) {
            setUrl(gm3d, user);
        }
        return gm3ds;
    }
//    public List<GM3d> filterByType(String type, int aid) {
//        QueryWrapper<GM3d> gm3dQueryWrapper = new QueryWrapper<>();
//        gm3dQueryWrapper.eq("type", type);
//        gm3dQueryWrapper.eq("author", aid);
//        List<GM3d> gm3ds = gm3dMapper.selectList(gm3dQueryWrapper);
//        for (GM3d gm3d : gm3ds) {
//            if(gm3d.getContentKeys()!=null)
//                setUrl(gm3d, user);
//        }
//        return gm3ds;
//    }

    public GM3d getGM3dByUuid(String uuid) throws Exception {
        QueryWrapper<GM3d> gm3dQueryWrapper = new QueryWrapper<>();
        gm3dQueryWrapper.eq("uid", uuid);
        gm3dQueryWrapper.ne("delete", true);
        GM3d gm3d = gm3dMapper.selectOne(gm3dQueryWrapper);
        if (gm3d == null)
            throw new NotFoundException("gm3d");

        return getGM3dById(gm3d.getId());
    }
}
