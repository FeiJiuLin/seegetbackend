package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.v04.entity.DefaultRecord;
import com.xmap.v04.mapper.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultRecordService {
    @Autowired
    private DefaultRecordMapper defaultRecordMapper;

    public DefaultRecord getByOwner(String ownerUuid) {
        QueryWrapper<DefaultRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", ownerUuid);
        DefaultRecord defaultRecord = defaultRecordMapper.selectOne(queryWrapper);
        return defaultRecord;
    }

    public DefaultRecord addRecord(DefaultRecord defaultRecord) {
        defaultRecordMapper.insert(defaultRecord);
        return defaultRecord;
    }
}
