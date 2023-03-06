package com.xmap.v04.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmap.v04.entity.Article;
import com.xmap.v04.mapper.ArticleMapper;
import com.xmap.v04.models.AddArticleModel;
import com.xmap.v04.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public Article addArticle(AddArticleModel addArticleModel) {
        Article article = new Article();
        if(addArticleModel.getImages() != null) {
            String images = String.join("#", addArticleModel.getImages());
            article.setImages(images);
        }
        article.setBucketId(addArticleModel.getBucketId());
        article.setCreator(addArticleModel.getCreator());
        article.setCreateTime(Util.getTime());

        article.setDescription(addArticleModel.getDescription());
        article.setTitle(addArticleModel.getTitle());

        articleMapper.insert(article);
        return article;
    }

    public List<Article> getAllArticles() {
        QueryWrapper<Article> wrapper= new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return articleMapper.selectList(wrapper);
    }

    public List<Article> requestArticles(String keyword) {
        String keywords = "%" + keyword + "%";
        return articleMapper.queryByKeyword(keywords);
    }

    @Cacheable(value = "article", key = "#id")
    public Article getArticleById(int id) {
        return articleMapper.selectById(id);
    }

    public List<Article> getArticleByCreator(int creator) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("creator", creator);
        return articleMapper.selectList(wrapper);
    }

    public void deleteById(int id) {
        articleMapper.deleteById(id);
    }
}
