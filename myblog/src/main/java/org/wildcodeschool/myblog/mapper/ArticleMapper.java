package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.*;
import org.wildcodeschool.myblog.model.*;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    //DTO for Article
    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDto = new ArticleDTO();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setUpdateAt(article.getUpdatedAt());
        //Category
        if(article.getCategory() != null) {
            articleDto.setCategoryName(article.getCategory().getName());
        }
        //Images
        if(article.getImages() != null) {
            articleDto.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }
        //Authors
        if(article.getArticleAuthors() != null) {
            articleDto.setAuthorDTOs(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        AuthorDTO authorDto = new AuthorDTO();
                        authorDto.setId(articleAuthor.getAuthor().getId());
                        authorDto.setFirstname(articleAuthor.getAuthor().getFirstname());
                        authorDto.setLastname(articleAuthor.getAuthor().getLastname());
                        return authorDto;
                    }).collect(Collectors.toList()));
        }
        return articleDto;
    }

    //convert to Entity when we create an article
    public Article convertToEntity(ArticleCreateDTO articleCreateDTO) {
        Article article = new Article();
        article.setTitle(articleCreateDTO.getTitle());
        article.setContent(articleCreateDTO.getContent());

        // Category
        if (articleCreateDTO.getCategoryId() != null) {
            Category category = new Category();
            category.setId(articleCreateDTO.getCategoryId());
            article.setCategory(category);
        }

        // Images
        if (articleCreateDTO.getImages() != null) {
            article.setImages(articleCreateDTO.getImages().stream()
                    .map(imageDTO -> {
                        Image image = new Image();
                        image.setUrl(imageDTO.getUrl());
                        return image;
                    }).collect(Collectors.toList()));
        }
        // Authors

        if (articleCreateDTO.getAuthors() != null) {
            article.setArticleAuthors(articleCreateDTO.getAuthors().stream()
                    .map(authorContributionDTO -> {
                        ArticleAuthor articleAuthor = new ArticleAuthor();
                        Author author = new Author();
                        author.setId(authorContributionDTO.getAuthorId());
                        articleAuthor.setAuthor(author);
                        articleAuthor.setContribution(authorContributionDTO.getContribution());
                        return articleAuthor;
                    }).collect(Collectors.toList()));
        }

        return article;
    }
}
