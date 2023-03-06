package com.xmap.ar.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.MM3d;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.MM3dMapper;
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
public class MM3dService {
    @Autowired
    private MM3dMapper mm3dMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtil;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public MM3d addMM3d(MM3d mm3d) {
        String uuid = "MM3D" + UUID.randomUUID().toString().replace("-", "");
        if  (mm3d.getContent()!=null)
            mm3d.setContentKeys(String.join("`", mm3d.getContent()));

        mm3d.setCreateTime(get_time());
        mm3d.setUid(uuid);
        mm3dMapper.insert(mm3d);
        return mm3d;
    }

    public void deleteMM3d(int id) {
        mm3dMapper.deleteById(id);
    }

    public MM3d updateMM3d(MM3d update, int id) throws Exception{
        QueryWrapper<MM3d> mm3dQueryWrapper = new QueryWrapper<>();
        if(update.getContent() != null){
            update.setContentKeys(String.join("`", update.getContent()));
        }
        update.setUpdateTime(get_time());
        mm3dQueryWrapper.eq("id", id);
        mm3dMapper.update(update, mm3dQueryWrapper);
        return getMM3dById(id);
    }

    public MM3d getMM3dById(int id) throws Exception{
        MM3d mm3d = mm3dMapper.selectById(id);
        User user = userMapper.selectById(mm3d.getAuthor());

        if(mm3d == null)
            throw new NotFoundException("mm3d");
        setUrl(mm3d, user);
        return mm3d;
    }

    private void setUrl(MM3d mm3d, User user) throws Exception {
        if(mm3d.getContentKeys() != null){
            List<String> Content = List.of(mm3d.getContentKeys().split("`"));
            List<String> url = new ArrayList<>();
            for(String key:Content){
                url.add(minioUtil.getPresignedObjectUrl(user.getUid(), key, 7));
            }
            mm3d.setContent(url);
        }
    }

    public List<MM3d> getMM3dByAuthor(int aid) throws Exception {
        QueryWrapper<MM3d> mm3dQueryWrapper = new QueryWrapper<>();
        mm3dQueryWrapper.eq("author", aid);
        mm3dQueryWrapper.ne("delete", true);
        List<MM3d> mm3ds = mm3dMapper.selectList(mm3dQueryWrapper);
        User user = userMapper.selectById(aid);
        for (MM3d mm3d : mm3ds) {
            setUrl(mm3d, user);
        }
        return mm3ds;
    }
//    public List<MM3d> filterByType(String type, int aid) {
//        QueryWrapper<MM3d> mm3dQueryWrapper = new QueryWrapper<>();
//        mm3dQueryWrapper.eq("type", type);
//        mm3dQueryWrapper.eq("author", aid);
//        List<MM3d> mm3ds = mm3dMapper.selectList(mm3dQueryWrapper);
//        for (MM3d mm3d : mm3ds) {
//            if(mm3d.getContentKeys()!=null)
//                setUrl(mm3d, user);
//        }
//        return mm3ds;
//    }

    public MM3d getMM3dByUuid(String uuid) throws Exception {
        QueryWrapper<MM3d> mm3dQueryWrapper = new QueryWrapper<>();
        mm3dQueryWrapper.eq("uid", uuid);
        mm3dQueryWrapper.ne("delete", true);
        MM3d mm3d = mm3dMapper.selectOne(mm3dQueryWrapper);
        if (mm3d == null)
            throw new NotFoundException("mm3d");
        return getMM3dById(mm3d.getId());
    }
}
