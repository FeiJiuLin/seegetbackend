package com.xmap.v04.models;

import com.xmap.v04.entity.Article;
import com.xmap.v04.entity.Bucket;
import com.xmap.v04.entity.User;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Data@NoArgsConstructor
public class ArticleResponse implements Serializable {
    private Integer id;
    private User creator;
    private Bucket bucketId;
    private String title;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
    private List<String> images;
}
