package com.xmap.v04.service;

import com.xmap.v04.config.cache.RedisUtils;
import com.xmap.v04.entity.ArticleCommentRecord;
import com.xmap.v04.mapper.ArticleCommentRecordMapper;
import com.xmap.v04.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleCommentRecordService extends RedisService{
    @Autowired
    private ArticleCommentRecordMapper articleCommentRecordMapper;

    @Autowired
    private RedisUtils redisUtils;

    private final String objectName = "articleCommentRecord";

    @Override
    public ArticleCommentRecord addWithContent(int creator, int objectId, String content) {
        ArticleCommentRecord articleCommentRecord = new ArticleCommentRecord();
        articleCommentRecord.setArticle(objectId);
        articleCommentRecord.setCreator(creator);
        articleCommentRecord.setContent(content);
        articleCommentRecord.setCreateTime(Util.getTime());
        redisUtils.lPush(String.format("%s::%s", objectName, objectId), articleCommentRecord);
        return articleCommentRecord;
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
    public List<ArticleCommentRecord> selectByCreatorAndId(int creator, int objectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("creator", creator);
        map.put("article", objectId);
        return articleCommentRecordMapper.selectByMap(map);
    }

    public void loadRedisToDatabase() {
        Set keys = redisUtils.getKeys(objectName);
        for (Object key : keys) {
            List<Object> objectList = redisUtils.lRange((String) key, 0, -1);
            for (Object r : objectList) {
                ArticleCommentRecord tmp = (ArticleCommentRecord) r;

                // too simple
                List<ArticleCommentRecord> res = selectByCreatorAndId(tmp.getCreator(), tmp.getArticle());
                if(res.size() == 0)
                    articleCommentRecordMapper.insert(tmp);
            }
        }
    }
}
