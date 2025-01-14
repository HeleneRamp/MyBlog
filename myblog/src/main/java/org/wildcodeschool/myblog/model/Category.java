package org.wildcodeschool.myblog.model;

import jakarta.persistence.*;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

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
}
