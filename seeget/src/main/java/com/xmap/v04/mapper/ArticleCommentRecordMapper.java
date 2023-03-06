package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.ArticleCommentRecord;
import org.apache.ibatis.annotations.Options;

public interface ArticleCommentRecordMapper extends BaseMapper<ArticleCommentRecord> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(ArticleCommentRecord entity);

}
