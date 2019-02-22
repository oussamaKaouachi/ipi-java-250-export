package com.example.demo.entity;

import javax.persistence.*;

/**
 * Created by Oussama :D on 22/02/2019.
 */
@Entity
@Table(name = "ligneFacture")
public class LigneFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Facture facture;

    @Column
    private Integer quantite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
}
