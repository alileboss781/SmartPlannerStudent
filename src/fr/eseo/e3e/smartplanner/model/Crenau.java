package fr.eseo.e3e.smartplanner.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Crenau {

    LocalDateTime debut;
    LocalDateTime fin;

    public Crenau(LocalDateTime debut, LocalDateTime fin) {
        this.debut = debut;
        this.fin = fin;
    }


    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }



}
