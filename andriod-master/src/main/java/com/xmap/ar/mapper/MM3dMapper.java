package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.MM3d;
import org.apache.ibatis.annotations.Options;

public interface MM3dMapper extends BaseMapper<MM3d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(MM3d entity);
}
