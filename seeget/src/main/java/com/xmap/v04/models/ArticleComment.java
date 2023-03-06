package com.xmap.v04.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ArticleComment {
    String content;
    Integer article;
}
