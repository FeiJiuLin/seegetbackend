package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.SceneToDP;
import org.apache.ibatis.annotations.Options;

public interface SceneToDPMapper extends BaseMapper<SceneToDP> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(SceneToDP entity);
}
