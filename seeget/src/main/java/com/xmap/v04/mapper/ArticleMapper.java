package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Article;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Article article);

    @Select("select * from article where title like #{keyword} OR description like #{keyword} UNION select * from article where creator in (select id " +
            "from users where username like #{keyword})")
    List<Article> queryByKeyword(@Param("keyword")String keyword);
}
