package com.xmap.v04.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("article_comment_record")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(serialize = false)
    private Integer id;
    private Integer article;
    private Integer creator;
    @TableField(value = "create_time")
    private Timestamp createTime;
    private String content;

}

