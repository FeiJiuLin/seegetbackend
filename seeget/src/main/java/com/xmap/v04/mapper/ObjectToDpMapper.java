package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.ObjectToDp;
import org.apache.ibatis.annotations.Options;

public interface ObjectToDpMapper extends BaseMapper<ObjectToDp> {
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(ObjectToDp objectToDp);
}
