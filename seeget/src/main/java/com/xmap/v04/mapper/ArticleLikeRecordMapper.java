package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.ArticleLikeRecord;
import org.apache.ibatis.annotations.Options;

public interface ArticleLikeRecordMapper extends BaseMapper<ArticleLikeRecord> {
    @Override
    @Options(useGeneratedKeys=true, keyProperty = "id", keyColumn = "id")
    int insert(ArticleLikeRecord entity);
}
