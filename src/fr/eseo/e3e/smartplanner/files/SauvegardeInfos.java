package fr.eseo.e3e.smartplanner.files;

import fr.eseo.e3e.smartplanner.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SauvegardeInfos {

    private static final String FICHIER_ETUDIANTS = "src/fr/eseo/e3e/smartplanner/files/data_etudiants.txt";

    public static void sauvegarderEtudiant(Student e) {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_ETUDIANTS, true))) {
            writer.write("=== Etudiant ===\n");

            // Connexion
            writer.write("Nom:" + e.getSurname() + "\n");
            writer.write("Prenom:" + e.getFirsname() + "\n");
            writer.write("MotDePasse:" + e.getMotDePasseHash() + "\n");
            writer.write("\n"); // Saut de ligne entre étudiants

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void sauvegarderMatiere(Student e, Matiere m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_ETUDIANTS, true))) {
            writer.write("--- Matiere ---\n");
            writer.write("Etudiant:" + e.getSurname() + "_" + e.getFirsname() + "\n");
            writer.write("Nom:" + m.getNom() + "\n");
            writer.write("DateExamen:" + m.getDateExamen().toString() + "\n");
            writer.write("Difficulte:" + m.getDifficulte() + "\n");
        } catch (IOException ex) {
            System.err.println("Erreur lors de la sauvegarde de la matière : " + ex.getMessage());
        }
    }



    /*public static void sauvegarderSessions(Student e, Matiere m, SessionRevision s) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_ETUDIANTS, true))) {
            writer.write("--- Sessions de révision ---\n");
            writer.write("Etudiant:" + e.getSurname() + "_" + e.getFirsname() + "\n");
            writer.write("Matiere:" + m.getNom() + "\n");
            writer.write("DateExamen:" + m.getDateExamen().toString() + "\n");
            writer.write("Difficulte:" + m.getDifficulte() + "\n");
        } catch (IOException ex) {
            System.err.println("Erreur lors de la sauvegarde de la matière : " + ex.getMessage());
        }
    }

     */




}
