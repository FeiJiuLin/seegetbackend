package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.M2d;
import org.apache.ibatis.annotations.Options;

public interface M2dMapper extends BaseMapper<M2d> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(M2d entity);
}
