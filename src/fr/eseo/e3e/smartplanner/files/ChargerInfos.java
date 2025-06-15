package fr.eseo.e3e.smartplanner.files;

import fr.eseo.e3e.smartplanner.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChargerInfos {

    private static final String FICHIER_ETUDIANTS = "src/fr/eseo/e3e/smartplanner/files/data_etudiants.txt";

    // 🔹 1. Charger les étudiants pour la connexion (nom, prénom, mot de passe)
    public static List<Student> chargerEtudiantsConnexion() {
        List<Student> etudiants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_ETUDIANTS))) {
            String ligne;
            Student student = null;

            while ((ligne = reader.readLine()) != null) {
                if (ligne.equals("--- Matiere ---")) {
                    break; // On arrête de lire le fichier dès qu'on arrive aux matières
                }
                System.out.println("Lecture ligne : " + ligne);
                if (ligne.equals("=== Etudiant ===")) {
                    student = new Student(); // <== ATTENTION : constructeur vide requis
                } else if (ligne.startsWith("Nom:")) {
                    student.setSurname(ligne.substring(4));
                } else if (ligne.startsWith("Prenom:")) {
                    student.setFirsname(ligne.substring(7));
                } else if (ligne.startsWith("Mot de passe hash:")) {
                    student.setMotDePasseHash(ligne.substring("Mot de passe hash:".length()));
                } else if (ligne.startsWith("Mot de passe salt:")) {
                    student.setSaltBase64(ligne.substring("Mot de passe salt:".length()));
                }else if (ligne.isEmpty() && student != null) {
                    etudiants.add(student);
                    student = null;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur chargement étudiants : " + e.getMessage());
        }
        return etudiants;
    }

    public static void chargerMatieresPour(Student etudiant) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_ETUDIANTS))) {
            String ligne;
            Matiere m = null;
            boolean correspond = false;
            String nomComplet = etudiant.getSurname() + "_" + etudiant.getFirsname();

            while ((ligne = reader.readLine()) != null) {
                if (ligne.equals("--- Matiere ---")) {
                    // Début d'une nouvelle matière
                    correspond = false;
                    m = new Matiere(); // Prépare une nouvelle matière
                } else if (ligne.startsWith("Etudiant:")) {
                    String ligneNomComplet = ligne.substring("Etudiant:".length()).trim();
                    correspond = ligneNomComplet.equals(nomComplet); // Vérifie si c'est la matière de l'étudiant courant
                } else if (correspond && ligne.startsWith("Nom:")) {
                    m.setNom(ligne.substring("Nom:".length()).trim());
                } else if (correspond && ligne.startsWith("DateExamen:")) {
                    m.setDateExamen(LocalDate.parse(ligne.substring("DateExamen:".length()).trim()));
                } else if (correspond && ligne.startsWith("Difficulte:")) {
                    m.setDifficulte(Integer.parseInt(ligne.substring("Difficulte:".length()).trim()));
                    etudiant.getMatieres().add(m); // ajoute une matière complète une fois qu'on a tout
                    // Ne pas réinitialiser m ici car une autre matière arrive avec "--- Matiere ---"
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement matières : " + e.getMessage());
        }
    }


}


