package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Source;
import org.apache.ibatis.annotations.Options;

public interface SourceMapper extends BaseMapper<Source> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Source entity);
}
