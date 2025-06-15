package fr.eseo.e3e.smartplanner.files;

import fr.eseo.e3e.smartplanner.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SauvegardeInfos {

    private static final String FICHIER_ETUDIANTS = "src/fr/eseo/e3e/smartplanner/files/data_etudiants.txt";

    public static void sauvegarderEtudiant(Student e) {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_ETUDIANTS, true))) {
            writer.write("=== Etudiant ===");
            writer.newLine();
            writer.write("Nom:" + e.getSurname());
            writer.newLine();
            writer.write("Prenom:" + e.getFirsname());
            writer.newLine();
            writer.write("Mot de passe hash:" + e.getMotDePasseHash());
            writer.newLine();
            writer.write("Mot de passe salt:" + e.getSaltBase64());
            writer.newLine();
            writer.newLine(); // Séparation entre étudiants

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



    /*public static void sauvegarderSessionsplanifier(Student e, SessionRevision s) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_ETUDIANTS, true))) {
            writer.write("--- Sessions Planifier ---\n");
            writer.write("Etudiant:" + e.getSurname() + "_" + e.getFirsname() + "\n");

            writer.write("+++ SessionRevision +++\n");

            writer.write("Date:" + s.getDate().toString() + "\n");
            writer.write("Duree:" + s.getDuree() + "\n");
            writer.write("Effectuee:" + (s.isEffectuee() ? "Oui" : "Non") + "\n");

        } catch (IOException ex) {
            System.err.println("Erreur lors de la sauvegarde de la session : " + ex.getMessage());
        }
    } */

}
