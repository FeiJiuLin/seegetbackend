package com.xmap.v04.service;

import com.xmap.v04.config.cache.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public abstract class RedisService {
    public Object add(int creator, int objectId){
        return null;
    }

    public Object addWithContent(int creator, int objectId, String content){
        return null;
    }

    public List<Object> selectById(int objectId) {
        return new ArrayList<Object>();
    }

    public void delete(int creator, int objectId){}

    public Object selectByCreatorAndId(int creator, int objectId) {
        return null;
    }

    public void loadRedisToDatabase(){
    }
}