package fr.eseo.e3e.smartplanner.model;

import java.time.LocalDate;

public class Crenau {

    LocalDate debut;
    LocalDate fin;


    public Crenau(LocalDate debut, LocalDate fin) {
        this.debut = debut;
        this.fin = fin;
    }


    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }





}
