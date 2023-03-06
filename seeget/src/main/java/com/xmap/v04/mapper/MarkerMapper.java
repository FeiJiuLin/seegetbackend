package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.Marker;
import org.apache.ibatis.annotations.Options;

import java.util.List;

public interface MarkerMapper extends BaseMapper<Marker> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Marker entity);

}
