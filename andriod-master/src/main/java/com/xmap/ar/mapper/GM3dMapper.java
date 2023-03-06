package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.GM3d;
import org.apache.ibatis.annotations.Options;

public interface GM3dMapper extends BaseMapper<GM3d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(GM3d entity);
}
