package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Collection;
import org.apache.ibatis.annotations.Options;

public interface CollectionMapper  extends BaseMapper<Collection> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Collection entity);
}
