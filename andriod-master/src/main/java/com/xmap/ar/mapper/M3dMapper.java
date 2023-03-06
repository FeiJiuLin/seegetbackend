package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.M3d;
import org.apache.ibatis.annotations.Options;

public interface M3dMapper extends BaseMapper<M3d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(M3d entity);
}
