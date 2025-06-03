package fr.eseo.e3e.smartplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends  Person {
    List<Matiere> matieres;
    private String motDePasse;
    public Student(String surname, List<Crenau> crenaus, Profession profession, String firsname, String motDePasse) {
        super(surname, crenaus, profession, firsname);
        this.matieres= new ArrayList<>();
        this.motDePasse=motDePasse;

    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
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
