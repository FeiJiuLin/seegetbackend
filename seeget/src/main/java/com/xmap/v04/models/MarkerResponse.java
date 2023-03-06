package com.xmap.v04.models;

import com.xmap.v04.entity.Bucket;
import com.xmap.v04.entity.User;
import lombok.*;

import java.sql.Timestamp;

@Setter@Getter@NoArgsConstructor
public class MarkerResponse {
    private Integer id;
    private String title;
    private String description;
    private String key;
    private float height;
    private float width;
    private Boolean share;
    private Bucket bucket;
    private User creator;
    private Timestamp createTime;
    private Timestamp updateTime;
}