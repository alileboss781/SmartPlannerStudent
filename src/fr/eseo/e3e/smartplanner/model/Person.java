package fr.eseo.e3e.smartplanner.model;

import java.util.List;

public class Person {
    String surname;
    String firsname;
    Profession profession;
    List<Crenau> crenaux;

    public Person(String surname, List<Crenau> crenaux, Profession profession, String firsname) {
        this.surname = surname;
        this.crenaux = crenaux;
        this.profession = profession;
        this.firsname = firsname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Crenau> getCrenaus() {
        return crenaux;
    }

    public void setCrenaus(List<Crenau> crenaus) {
        this.crenaux = crenaus;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public String getFirsname() {
        return firsname;
    }

    public void setFirsname(String firsname) {
        this.firsname = firsname;
    }
}
