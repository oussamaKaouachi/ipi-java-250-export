package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * Classe permettant d'insérer des données dans l'application.
 */
@Service
@Transactional
public class InitData implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EntityManager em;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        insertTestData();
    }

    private void insertTestData() {
        Client client1 = newClient("PETRILLO", "Alexandre");
        em.persist(client1);

        Client client2 = newClient("Dupont", "Jérome");
        em.persist(client2);

        Client client3 = newClient("KAOUACHI", "Oussama");
        em.persist(client3);

        Article article1 = newArticle("TV", 500.00);
        em.persist(article1);

        Article article2 = newArticle("SmartPhone", 700.00);
        em.persist(article2);

        Article article3 = newArticle("Ordi", 850.00);
        em.persist(article3);

        Facture facture1 = newFacture(client3);
        em.persist(facture1);

        LigneFacture ligneFacture1 = newLigneFacture(article1, facture1, 2);
        em.persist(ligneFacture1);

        LigneFacture ligneFacture2 = newLigneFacture(article2, facture1, 1);
        em.persist(ligneFacture2);
    }

    private Client newClient(String nom, String prenom) {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        return client;
    }

    private Article newArticle(String libelle, Double prix){
        Article article = new Article();
        article.setLibelle(libelle);
        article.setPrix(prix);
        return article;
    }

    private LigneFacture newLigneFacture(Article article, Facture facture, Integer qt){
        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setArticle(article);
        ligneFacture.setFacture(facture);
        ligneFacture.setQuantite(qt);
        return ligneFacture;
    }

    private Facture newFacture(Client client){
        Facture facture = new Facture();
        facture.setClient(client);
        return facture;
    }
}
