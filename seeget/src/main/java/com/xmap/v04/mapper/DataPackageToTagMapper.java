package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.DataPackageToTag;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface DataPackageToTagMapper extends BaseMapper<DataPackageToTag> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(DataPackageToTag entity);

}
