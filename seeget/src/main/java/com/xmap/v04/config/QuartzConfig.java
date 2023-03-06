package com.xmap.v04.config;

import com.xmap.v04.task.CommentRecordTask;
import com.xmap.v04.task.LikeRecordTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";
    private static final String COMMENT_TASK_IDENTITY = "CommentTaskQuartz";

    @Bean
    public JobDetail LikeDetail(){
        return JobBuilder.newJob(LikeRecordTask.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public JobDetail CommentDetail(){
        return JobBuilder.newJob(CommentRecordTask.class).withIdentity(COMMENT_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger likerQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(10)  //设置时间周期单位秒
                .withIntervalInHours(24)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(LikeDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public Trigger commentQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(10)  //设置时间周期单位秒
                .withIntervalInHours(24)  //两天执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(CommentDetail())
                .withIdentity(COMMENT_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
