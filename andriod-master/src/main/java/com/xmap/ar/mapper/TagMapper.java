package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Tag;
import org.apache.ibatis.annotations.Options;

public interface TagMapper extends BaseMapper<Tag> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Tag entity);
}
