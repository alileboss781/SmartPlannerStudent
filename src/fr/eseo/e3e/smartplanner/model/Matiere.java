package fr.eseo.e3e.smartplanner.model;

import java.time.LocalDate;

public class Matiere {
    String nom;
    LocalDate dateExamen;
    int difficulte;


    public Matiere(String nom, LocalDate dateExamen, int difficulte) {
        this.nom = nom;
        this.dateExamen = dateExamen;
        this.difficulte = difficulte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateExamen() {
        return dateExamen;
    }

    public void setDateExamen(LocalDate dateExamen) {
        this.dateExamen = dateExamen;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }






}
