package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.TagLink;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagLinkMapper extends BaseMapper<TagLink> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(TagLink entity);

    @Select("SELECT tag from tag_link WHERE project = #{project}")
    List<String> selectTag(@Param("project") String project);
}
