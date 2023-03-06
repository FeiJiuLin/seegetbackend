package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Scene;
import org.apache.ibatis.annotations.Options;

public interface SceneMapper extends BaseMapper<Scene> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Scene entity);
}
