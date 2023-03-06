package com.xmap.v04.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class AddArticleModel {
    private Integer creator;
    private Integer bucketId;
    private List<String> images;
    private String title;
    private String description;
    private Timestamp createTime;
}
