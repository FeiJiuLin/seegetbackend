package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Tag;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Tag entity);

    @Select("SELECT * FROM tags WHERE uuid= #{uuid}")
    Tag findTagByUuid(@Param("uuid") String uuid);

    @Select("SELECT * FROM tags")
    List<Tag> selectAll();
}
