package com.xmap.v04.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmap.v04.entity.DataPackage;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

public interface DataPackageMapper extends BaseMapper<DataPackage> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(DataPackage entity);



    @Update("UPDATE datapackage set synchronization = #{state}, update_time = #{time} WHERE datapackage.id = #{id}")
    void updateSyn(Boolean state, @Param("time") Timestamp time, int id);
}
