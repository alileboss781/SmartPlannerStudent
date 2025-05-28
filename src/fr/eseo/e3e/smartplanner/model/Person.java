package fr.eseo.e3e.smartplanner.model;

import java.util.List;

public class Person {
    String surname;
    String firsname;
    Profession profession;
    List<Crenau> crenaus;

    public Person(String surname, List<Crenau> crenaus, Profession profession, String firsname) {
        this.surname = surname;
        this.crenaus = crenaus;
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
        return crenaus;
    }

    public void setCrenaus(List<Crenau> crenaus) {
        this.crenaus = crenaus;
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
