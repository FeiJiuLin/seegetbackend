package com.xmap.v04.models;

import com.xmap.v04.entity.Bucket;
import lombok.*;

@Setter@Getter@NoArgsConstructor
public class MarkerModel {
    private String title;
    private String description;
    private String key;
    private float height;
    private float width;
    private Boolean share;
    private Integer bucketId;
}
