package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.SceneToTag;
import org.apache.ibatis.annotations.Options;

public interface SceneToTagMapper extends BaseMapper<SceneToTag> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(SceneToTag entity);
}
