package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String categoryName;
    private List<String> imageUrls;
    private List<AuthorDTO> authorDTOs;
    private String paragrOne;
    private String paragrTwo;
    private String paragrThree;
    private int likeCount;
    private boolean isFavorite;

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getParagrOne() {
        return paragrOne;
    }

    public void setParagrOne(String paragrOne) {
        this.paragrOne = paragrOne;
    }

    public String getParagrTwo() {
        return paragrTwo;
    }

    public void setParagrTwo(String paragrTwo) {
        this.paragrTwo = paragrTwo;
    }

    public String getParagrThree() {
        return paragrThree;
    }

    public void setParagrThree(String paragrThree) {
        this.paragrThree = paragrThree;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}



