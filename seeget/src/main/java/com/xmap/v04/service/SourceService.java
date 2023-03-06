package com.xmap.v04.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.v04.entity.Source;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.mapper.SourceMapper;
import com.xmap.v04.models.SourceModel;
import com.xmap.v04.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {
    @Autowired
    private SourceMapper sourceMapper;

    public Source addSource(SourceModel sourceModel, int creator) {
        Source source = new Source();
        source.setCreator(creator);
        source.setCreateTime(Util.getTime());
        source.setBucketId(sourceModel.getBucketId());
        source.setKey(sourceModel.getKey());
        sourceMapper.insert(source);
        return source;
    }

    public List<Source> getSourceByCreator(int creator) {
        QueryWrapper<Source> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", creator);
        return sourceMapper.selectList(queryWrapper);
    }

    public boolean deleteSourceById(int id) throws NotFoundException {
        Source source = sourceMapper.selectById(id);
        if(source == null) {
            throw new NotFoundException("source does not exits.");
        } else {
            sourceMapper.deleteById(id);
            return true;
        }
    }
}
