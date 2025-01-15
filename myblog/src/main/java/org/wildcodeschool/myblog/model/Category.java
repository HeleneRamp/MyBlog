package org.wildcodeschool.myblog.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Article> articles;

    //Getters & Setters

  public long getId() {
      return id;
  }

  public void setId(long id) {
      this.id = id;
  }

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   public List<Article> getArticles() {
      return articles;
   }

   public void setArticles(List<Article> articles) {
      this.articles = articles;
   }
}