package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Bucket;
import org.apache.ibatis.annotations.Options;

public interface BucketMapper extends BaseMapper<Bucket> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Bucket entity);
}
