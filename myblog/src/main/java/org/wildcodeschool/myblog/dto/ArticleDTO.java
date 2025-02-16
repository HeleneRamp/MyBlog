package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updateAt;
    private String categoryName;
    private List<String> imageUrls;
    private  List<AuthorDTO> authorDTOs;

//Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<AuthorDTO> getAuthorDTOs() {
        return authorDTOs;
    }

    public void setAuthorDTOs(List<AuthorDTO> authorDTOs) {
        this.authorDTOs = authorDTOs;
    }
}



