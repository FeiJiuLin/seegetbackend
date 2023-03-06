package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.v04.entity.Folder;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FolderService {
    @Autowired
    private FolderMapper folderMapper;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Folder addFolder(Folder folder) {
        String uuid = "FOLDER" + UUID.randomUUID().toString().replace("-", "");

        if  (folder.getKeylists()!=null)
            folder.setKeys(String.join("`", folder.getKeylists()));
        folder.setUuid(uuid);
        folderMapper.insert(folder);
        return folder;
    }

    public void deleteFolder(int id) {
        folderMapper.deleteById(id);
    }

    public Folder updateFolder(Folder update, int id) throws Exception{
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        if(update.getKeylists() != null){
            update.setKeys(String.join("`", update.getKeylists()));
        }
        update.setUpdateTime(get_time());
        folderQueryWrapper.eq("id", id);
        folderMapper.update(update, folderQueryWrapper);
        return getFolderById(id);
    }

    public Folder getFolderById(int id) throws Exception{
        Folder folder = folderMapper.selectById(id);
        if(folder == null)
            throw new NotFoundException("folder");
        if(folder.getKeys() != null)
            folder.setKeylists(List.of(folder.getKeys().split("`")));
        return folder;
    }

    public List<Folder> getFolderByAuthor(int aid) {
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        folderQueryWrapper.eq("author", aid);
        List<Folder> folders = folderMapper.selectList(folderQueryWrapper);
        for (Folder folder : folders) {
            if(folder.getKeys()!=null)
                folder.setKeylists(List.of(folder.getKeys().split("`")));
        }
        return folders;
    }

    public List<Folder> filterByType(String type, int aid) {
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        folderQueryWrapper.eq("type", type);
        folderQueryWrapper.eq("author", aid);
        List<Folder> folders = folderMapper.selectList(folderQueryWrapper);
        for (Folder folder : folders) {
            if(folder.getKeys()!=null)
                folder.setKeylists(List.of(folder.getKeys().split("`")));
        }
        return folders;
    }

    public Folder getFolderByUuid(String uuid) throws Exception {
        QueryWrapper<Folder> folderQueryWrapper = new QueryWrapper<>();
        folderQueryWrapper.eq("uuid", uuid);
        Folder folder = folderMapper.selectOne(folderQueryWrapper);
        return getFolderById(folder.getId());
    }
}
