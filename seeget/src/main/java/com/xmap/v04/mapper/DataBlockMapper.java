package com.xmap.v04.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.xmap.v04.entity.DataBlock;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;


public interface DataBlockMapper extends BaseMapper<DataBlock> {
    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(DataBlock entity);

    @Update("UPDATE datablock set upload_progress = #{progressJson} WHERE datablock.id = #{id}")
    void updateState(@Param("progressJson")String progressJson, @Param("id")Integer id);

    @Update("UPDATE datablock set upload_status = #{status} WHERE datablock.id = #{id}")
    void updateStatus(@Param("status")Integer status, @Param("id")Integer id);


//    @Select("SELECT * FROM datablock WHERE data_package_id = #{id}")
//    List<DataBlock> selectByDpId(@Param("id") int id);
}
