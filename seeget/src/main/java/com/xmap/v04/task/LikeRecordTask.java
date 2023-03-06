package com.xmap.v04.task;

import com.xmap.v04.service.ArticleLikeRecordService;
import com.xmap.v04.utils.Util;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class LikeRecordTask extends QuartzJobBean{
    Logger logger = LoggerFactory.getLogger(QuartzJobBean.class);

    @Autowired
    ArticleLikeRecordService articleLikeRecordService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("Save likeRecord-------- {}", Util.getTime());

        //将 Redis 里的点赞信息同步到数据库里
        articleLikeRecordService.loadRedisToDatabase();
    }
}
