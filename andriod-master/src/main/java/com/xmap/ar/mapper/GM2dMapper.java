package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.GM2d;
import org.apache.ibatis.annotations.Options;

public interface GM2dMapper extends BaseMapper<GM2d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(GM2d entity);
}

