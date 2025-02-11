package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;

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
}
