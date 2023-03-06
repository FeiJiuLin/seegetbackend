package com.xmap.ar.service;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.ar.entity.Folder;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.mapper.FolderMapper;
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
public class FolderService {
    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtil;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Folder addFolder(Folder folder) {
        String uuid = "FOLDER" + UUID.randomUUID().toString().replace("-", "");
        if  (folder.getContent()!=null)
            folder.setContentKeys(String.join("`", folder.getContent()));

        folder.setCreateTime(get_time());
        folder.setUid(uuid);
        folderMapper.insert(folder);
        return folder;
    }

    public void deleteFolder(int id) {
        folderMapper.deleteById(id);
    }

    public Folder updateFolder(Folder update, int id) throws Exception{
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        if(update.getContent() != null){
            update.setContentKeys(String.join("`", update.getContent()));
        }
        update.setUpdateTime(get_time());
        folderQueryWrapper.eq("id", id);
        folderMapper.update(update, folderQueryWrapper);
        return getFolderById(id);
    }

    public Folder getFolderById(int id) throws Exception{
        Folder folder = folderMapper.selectById(id);
        User user = userMapper.selectById(folder.getAuthor());

        if(folder == null)
            throw new NotFoundException("folder");
        setUrl(folder, user);
        return folder;
    }

    private void setUrl(Folder folder, User user) throws Exception {
        if(folder.getContentKeys() != null){
            List<String> Content = List.of(folder.getContentKeys().split("`"));
            List<String> url = new ArrayList<>();
            for(String key:Content){
                url.add(minioUtil.getPresignedObjectUrl(user.getUid(), key, 7));
            }
            folder.setContent(url);
        }
    }

    public List<Folder> getFolderByAuthor(int aid) throws Exception {
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        folderQueryWrapper.eq("author", aid);
        folderQueryWrapper.ne("delete", true);
        List<Folder> folders = folderMapper.selectList(folderQueryWrapper);
        User user = userMapper.selectById(aid);
        for (Folder folder : folders) {
            setUrl(folder, user);
        }
        return folders;
    }

//    public List<Folder> filterByType(String type, int aid) {
//        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
//        folderQueryWrapper.eq("type", type);
//        folderQueryWrapper.eq("author", aid);
//        List<Folder> folders = folderMapper.selectList(folderQueryWrapper);
//        for (Folder folder : folders) {
//            if(folder.getContentKeys()!=null)
//                setUrl(folder, user);
//        }
//        return folders;
//    }

    public Folder getFolderByUuid(String uuid) throws Exception {
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        folderQueryWrapper.eq("uid", uuid);
        folderQueryWrapper.ne("delete", true);
        Folder folder = folderMapper.selectOne(folderQueryWrapper);
        if(folder == null)
            throw new NotFoundException("folder");
        return getFolderById(folder.getId());
    }
}
