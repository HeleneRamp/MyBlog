package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.exception.*;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;

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

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, CategoryRepository categoryRepository, ImageRepository imageRepository, AuthorRepository authorRepository, ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
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
    public ArticleDTO createArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if(article.getTitle().length() > 50) {
            throw new ExceededMaxLengthException("Le titre ne peux pas dépasser 50 caractères");
        }

        //add category
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(()-> new CategoryNotFoundException("La catégorie avec l'id " + article.getCategory().getId() +" n'existe pas :("));
            article.setCategory(category);
        }

        //add images
        if (article.getImages() != null && !article.getImages().isEmpty()) {
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
        }

        Article savedArticle = articleRepository.save(article);

        //add authors
        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {

                Author author = authorRepository.findById(articleAuthor.getAuthor().getId())
                        .orElseThrow(()-> new AuthorNotFoundException("L'auteur avec l'id " + articleAuthor.getAuthor().getId() + " n'existe pas :("));

                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    //DTO for update an article
    public ArticleDTO updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé :("));
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
    public boolean deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'existe pas :("));
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }
}