package fr.eseo.e3e.smartplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends  Person {
    List<Matiere> matieres;
    public Student(String surname, List<Crenau> crenaus, Profession profession, String firsname) {
        super(surname, crenaus, profession, firsname);
        this.matieres= new ArrayList<>();

    }

    public List<Matiere> getMatieres() {
        return matieres;
    }
    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }
    public void ajouterMatiere(Matiere matiere) {
        matieres.add(matiere);
    }
    public void supprimerMatiere(Matiere matiere){
        matieres.remove(matiere);
    }

}
