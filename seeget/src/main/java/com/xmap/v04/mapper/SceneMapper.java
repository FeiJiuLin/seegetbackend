package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Scene;
import org.apache.ibatis.annotations.Options;

public interface SceneMapper extends BaseMapper<Scene> {
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Scene scene);
}
