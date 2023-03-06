package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.MM2d;
import org.apache.ibatis.annotations.Options;

public interface MM2dMapper extends BaseMapper<MM2d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(MM2d entity);
}
