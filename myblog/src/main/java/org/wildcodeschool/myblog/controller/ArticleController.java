package org.wildcodeschool.myblog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.Service.ArticleService;
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.model.*;

import java.util.List;


@RestController
@RequestMapping("/articles")
@Validated
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //CRUD
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleCreateDTO articleCreateDTO) {
     ArticleDTO savedArticle = articleService.createArticle(articleCreateDTO);
     return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        ArticleDTO article = articleService.updateArticle(id, articleDetails);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
       if (articleService.deleteArticle(id)) {
           return ResponseEntity.noContent().build();
       } else {
           return ResponseEntity.notFound().build();
       }
    }
}
