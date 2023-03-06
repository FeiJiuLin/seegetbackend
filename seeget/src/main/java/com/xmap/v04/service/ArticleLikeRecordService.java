package com.xmap.v04.service;

import com.xmap.v04.config.cache.RedisUtils;
import com.xmap.v04.entity.ArticleLikeRecord;
import com.xmap.v04.mapper.ArticleLikeRecordMapper;
import com.xmap.v04.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@EnableScheduling
public class ArticleLikeRecordService extends RedisService{
    @Autowired
    ArticleLikeRecordMapper mapper;

    @Autowired
    private RedisUtils redisUtils;

    private final String objectName = "articleLikeRecord";

    @Override
    public ArticleLikeRecord add(int creator, int objectId) {
        ArticleLikeRecord articleLikeRecord = new ArticleLikeRecord();
        articleLikeRecord.setArticle(objectId);
        articleLikeRecord.setCreator(creator);
        articleLikeRecord.setCreateTime(Util.getTime());
        redisUtils.lPush(String.format("%s::%s", objectName, objectId), articleLikeRecord);
        return articleLikeRecord;
    }

    @Override
    public List<Object> selectById(int objectId) {
        List<Object> likeList = redisUtils.lRange(String.format("%s::%s", objectName, objectId), 0, -1);
        redisUtils.getKeys(objectName);
        return likeList;
    }

    @Override
    public void delete(int creator, int objectId){

    }

    @Override
    public List<ArticleLikeRecord> selectByCreatorAndId(int creator, int objectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("creator", creator);
        map.put("article", objectId);
        return mapper.selectByMap(map);
    }

    public void loadRedisToDatabase() {
        Set keys = redisUtils.getKeys(objectName);
        for (Object key : keys) {
            List<Object> objectList = redisUtils.lRange((String) key, 0, -1);
            for (Object r : objectList) {
                ArticleLikeRecord tmp = (ArticleLikeRecord) r;

                // too simple
                List<ArticleLikeRecord> res = selectByCreatorAndId(tmp.getCreator(), tmp.getArticle());
                if(res.size() == 0)
                    mapper.insert(tmp);
            }
        }
    }
}
