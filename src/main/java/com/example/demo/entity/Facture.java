package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Oussama :D on 22/02/2019.
 */
@Entity
@Table(name = "facture")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "facture")
    private Set<LigneFacture> ligneFactures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<LigneFacture> getLigneFactures() {
        return ligneFactures;
    }

    public void setLigneFactures(Set<LigneFacture> ligneFactures) {
        this.ligneFactures = ligneFactures;
    }
}
