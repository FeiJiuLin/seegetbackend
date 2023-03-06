package com.xmap.ar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.ar.entity.Obj;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface ObjMapper extends BaseMapper<Obj> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Obj entity);
}
