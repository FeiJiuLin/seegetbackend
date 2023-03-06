package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Objects;
import org.apache.ibatis.annotations.Options;

public interface ObjectMapper extends BaseMapper<Objects> {
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Objects objects);
}
