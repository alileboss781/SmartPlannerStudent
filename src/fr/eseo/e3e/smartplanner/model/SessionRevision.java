package fr.eseo.e3e.smartplanner.model;

import java.time.Duration;
import java.time.LocalDateTime;




public class SessionRevision {

    Matiere matiere;
    LocalDateTime date;
    Duration duree;
    boolean effectuee;

    public SessionRevision(Matiere matiere, LocalDateTime date, Duration duree, boolean effectuee) {
        this.matiere = matiere;
        this.date = date;
        this.duree = duree;
        this.effectuee = effectuee;
    }


    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Duration getDuree() {
        return duree;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public boolean isEffectuee() {
        return effectuee;
    }

    // Méthode pour marquer la session comme effectuée (selon votre UML)
    public void marquerEffectuee() {
        this.effectuee = true;
    }




    @Override
    public String toString() {
        return "Matière : " + matiere.getNom() +
                " | Début : " + date +
                " | Durée : " + duree.toHours() + "h" +
                " | Effectuée : " + (effectuee ? "Oui" : "Non");
    }
}
