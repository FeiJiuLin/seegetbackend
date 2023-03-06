package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Recycle;
import org.apache.ibatis.annotations.Options;

public interface RecycleMapper extends BaseMapper<Recycle> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Recycle entity);
}
