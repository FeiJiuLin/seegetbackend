package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.ObjectToTag;
import org.apache.ibatis.annotations.Options;

public interface ObjectToTagMapper extends BaseMapper<ObjectToTag> {
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(ObjectToTag objectToTag);
}
