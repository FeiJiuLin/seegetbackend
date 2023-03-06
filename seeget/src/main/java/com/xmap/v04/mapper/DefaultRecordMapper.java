package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.DefaultRecord;
import org.apache.ibatis.annotations.Options;

public interface DefaultRecordMapper extends BaseMapper<DefaultRecord> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(DefaultRecord defaultRecord);
}
