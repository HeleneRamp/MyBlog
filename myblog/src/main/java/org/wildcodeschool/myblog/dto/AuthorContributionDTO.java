package org.wildcodeschool.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AuthorContributionDTO {

    @NotNull(message = "L'Id de l'auteur ne dois pas être null")
    @Positive(message = "L'ID de l'auteur doit être un nombre positif")
    private Long authorId;

    @NotBlank(message = "La contribution de l'auteur ne doit pas être vide")
    private String contribution;

    //Getters & Setters


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }
}
