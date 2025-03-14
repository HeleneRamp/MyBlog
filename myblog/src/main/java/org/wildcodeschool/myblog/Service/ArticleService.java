package org.wildcodeschool.myblog.Service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorContributionDTO;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.*;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, CategoryRepository categoryRepository, ImageRepository imageRepository, AuthorRepository authorRepository, ArticleAuthorRepository articleAuthorRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.userRepository = userRepository;
    }

    //DTO for get all articles
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return  articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    //DTO for get article by id
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé :("));
        return articleMapper.convertToDTO(article);
    }

    //DTO for create an article
    public ArticleDTO createArticle(@Valid ArticleCreateDTO articleCreateDTO) {
        Article article = articleMapper.convertToEntity(articleCreateDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        //add category
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(()-> new CategoryNotFoundException("La catégorie avec l'id " + article.getCategory().getId() +" n'existe pas :("));
            article.setCategory(category);

        //add images
            List<Image> validImages = new ArrayList<>();
            for (ImageDTO image : articleCreateDTO.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId())
                            .orElseThrow(()-> new ImageNotFoundException("L'image avec l'id " + image.getId() + " n'existe pas :("));
                        validImages.add(existingImage);
                } else {
                    Image newImage = new Image();
                    newImage.setUrl(image.getUrl());
                    validImages.add(imageRepository.save(newImage));
                }
            }
            article.setImages(validImages);

        Article savedArticle = articleRepository.save(article);

        //add authors
            List<ArticleAuthor> articleAuthors = new ArrayList<>();
            for (AuthorContributionDTO authorDTO : articleCreateDTO.getAuthors()) {

                Author author = authorRepository.findById(authorDTO.getAuthorId())
                        .orElseThrow(()-> new AuthorNotFoundException("L'auteur avec l'id " + authorDTO.getAuthorId() + " n'existe pas :("));

                ArticleAuthor articleAuthor = new ArticleAuthor();
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(authorDTO.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
            article.setArticleAuthors(articleAuthors);

        return articleMapper.convertToDTO(savedArticle);
    }

    //DTO for update an article
    public ArticleDTO updateArticle(Long id, Article articleDetails, String userEmail){
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé :("));

        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundException("user not found"));

        boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");
        boolean isAuthor = user.getRoles().contains("ROLE_AUTHOR");

        if (!isAdmin && !isAuthor) {
            throw new AccessDeniedException("");
        }

        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        //update category
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(()-> new CategoryNotFoundException("La catégorie avec l'id " + article.getCategory().getId() +" n'existe pas :("));
            article.setCategory(category);
        }

        if(articleDetails.getTitle().length() > 50) {
            throw new ExceededMaxLengthException("Le titre ne peux pas dépasser 50 caractères");
        }
        //update images
        if (article.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId())
                            .orElseThrow(()-> new ImageNotFoundException("L'image avec l'id " + image.getId() + " n'existe pas :("));
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        //update authors
        if (articleDetails.getArticleAuthors() != null) {
            articleAuthorRepository.deleteAll(article.getArticleAuthors());

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
               Author author = authorRepository.findById(articleAuthorDetails.getAuthor().getId())
                        .orElseThrow(()-> new AuthorNotFoundException("L'auteur avec l'id " + articleAuthorDetails.getAuthor().getId() + " n'existe pas :("));

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            articleAuthorRepository.saveAll(updatedArticleAuthors);

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    //DTO for delete article/author
    public void deleteArticle(Long id, String userEmail) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'existe pas :("));

        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundException("user not found"));

        boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");
        boolean isAuthor = user.getRoles().contains("ROLE_AUTHOR");

        if (!isAdmin && !isAuthor) {
            throw new AccessDeniedException("");
        }
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
    }
}