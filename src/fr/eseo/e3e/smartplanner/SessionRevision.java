package fr.eseo.e3e.smartplanner;

import fr.eseo.e3e.smartplanner.model.Matiere;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SessionRevision {

    Matiere maniere;
    LocalDateTime date;
    Duration duree;
    boolean effectuee;

    public SessionRevision(Matiere maniere, LocalDateTime date, Duration duree, boolean effectuee) {
        this.maniere = maniere;
        this.date = date;
        this.duree = duree;
        this.effectuee = effectuee;
    }


    public Matiere getManiere() {
        return maniere;
    }

    public void setManiere(Matiere maniere) {
        this.maniere = maniere;
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
        return "SessionRevision{" +
                "maniere=" + maniere +
                ", date=" + date +
                ", duree=" + duree +
                ", effectuee=" + effectuee +
                '}';
    }
}
