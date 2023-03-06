package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Folder;
import org.apache.ibatis.annotations.Options;

public interface FolderMapper extends BaseMapper<Folder> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Folder entity);
}
