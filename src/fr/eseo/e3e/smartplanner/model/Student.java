package fr.eseo.e3e.smartplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends  Person {
    List<Matiere> matieres;
    private String motDePasseHash;
    private String saltBase64;

    public Student(String surname, List<Crenau> crenaus, Profession profession, String firsname, String motDePasseHash, String saltBase64) {
        super(surname, crenaus, profession, firsname);
        this.matieres = new ArrayList<>();
        this.motDePasseHash = motDePasseHash;
        this.saltBase64 = saltBase64;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    public String getSaltBase64() {
        return saltBase64;
    }

    public void setSaltBase64(String saltBase64) {
        this.saltBase64 = saltBase64;
    }

    /*public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }*/

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