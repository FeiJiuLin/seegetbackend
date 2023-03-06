package com.xmap.v04.controller;

import com.xmap.v04.entity.Article;
import com.xmap.v04.entity.ArticleCommentRecord;
import com.xmap.v04.entity.ArticleLikeRecord;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.BaseException;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.models.AddArticleModel;
import com.xmap.v04.models.ArticleComment;
import com.xmap.v04.models.ArticleResponse;
import com.xmap.v04.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/article")
public class ArticleController {
    Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleLikeRecordService articleLikeRecordService;

    @Autowired
    private ArticleCommentRecordService articleCommentRecordMService;

    @PostMapping()
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody AddArticleModel addArticleModel) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        try{
            addArticleModel.setCreator(user.getId());
            Article article = articleService.addArticle(addArticleModel);
            logger.info("create article with title " + article.getTitle());
            return new ResponseEntity<>(transformOne(article), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BaseException(e.toString());
        }

    }

    @GetMapping("")
    public ResponseEntity<List<ArticleResponse>> getAllArticle() throws Exception{
       List<Article> articles = articleService.getAllArticles();
       return new ResponseEntity<>(transformList(articles), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable int id) throws Exception{
        Article article = articleService.getArticleById(id);
        if(article == null)
            throw new NotFoundException("article " + id);
        return new ResponseEntity<>(transformOne(article), HttpStatus.OK);
    }

    @GetMapping("/request")
    public ResponseEntity<List<ArticleResponse>> requestArticleInfo(@RequestParam(required = false) String keyword) {
        List<Article> articles = articleService.requestArticles(keyword);
        logger.info("query: #keyword:" + keyword);
        return new ResponseEntity<>(transformList(articles), HttpStatus.OK);
    }

    @GetMapping("/myself")
    public ResponseEntity<List<ArticleResponse>> getSelfArticle() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Article> articles = articleService.getArticleByCreator(user.getId());
        return new ResponseEntity<>(transformList(articles), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteArticleById(@RequestParam int id) throws Exception{
        Article article = articleService.getArticleById(id);
        if (article == null)
            throw new NotFoundException(String.format("article with id %s", id));
        else {
            articleService.deleteById(id);
            return new ResponseEntity<>(String.format("delete article with id %s successfully", id), HttpStatus.OK);
        }
    }

    @GetMapping("/like/{id}")
    public ResponseEntity<ArticleLikeRecord> likeArticle(@PathVariable int id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArticleLikeRecord ar = articleLikeRecordService.add(user.getId(), id);
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @GetMapping("/liker/{article}")
    public ResponseEntity<Integer> getArticleLiker(@PathVariable int article) {
        List<Object> ar = articleLikeRecordService.selectById(article);
        return new ResponseEntity<>(ar.size(), HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<ArticleCommentRecord> commentArticle(@RequestBody ArticleComment articleComment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArticleCommentRecord ar = articleCommentRecordMService.addWithContent(user.getId(), articleComment.getArticle(), articleComment.getContent());
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    @GetMapping("/comment/{article}")
    public ResponseEntity<List<Object>> getArticleComment(@PathVariable Integer article) {
        List<Object> ar = articleCommentRecordMService.selectById(article);
        return new ResponseEntity<>(ar, HttpStatus.OK);
    }

    private List<ArticleResponse> transformList(List<Article> articles) {
        List<ArticleResponse> res = new ArrayList<>();
        for(Article article : articles) {
            res.add(transformOne(article));
        }
        return res;
    }

    public ArticleResponse transformOne(Article article) {
        ArticleResponse res = new ArticleResponse();
        res.setId(article.getId());
        res.setCreator(userService.findUserById(article.getCreator()));
        res.setBucketId(bucketService.getBucketById(article.getBucketId()));
        res.setTitle(article.getTitle());
        res.setDescription(article.getDescription());
        res.setCreateTime(article.getCreateTime());
        res.setUpdateTime(article.getUpdateTime());
        if(article.getImages() != null){
            List<String> Images = Arrays.asList(article.getImages().split("#"));
            res.setImages(Images);
        }
        return res;
    }
}
