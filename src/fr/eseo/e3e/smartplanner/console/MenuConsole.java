package fr.eseo.e3e.smartplanner.console;
import fr.eseo.e3e.smartplanner.model.Matiere;
import fr.eseo.e3e.smartplanner.model.Student;
import fr.eseo.e3e.smartplanner.model.Profession;
import fr.eseo.e3e.smartplanner.model.Crenau;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MenuConsole {
    private final List<Student> students = new ArrayList<>();
    private Student studentConnected = null;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n--- Smart Révision Planner ---");
            System.out.println("1. Connexion");
            System.out.println("2. Créer un compte");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> login();
                case "2" -> createAccount();
                case "0" -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void login() {
        System.out.print("Nom : ");
        String surname = scanner.nextLine();
        System.out.print("Prénom : ");
        String firstname = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        for (Student s : students) {
            if (s.getSurname().equals(surname) && s.getFirsname().equals(firstname) && s.getMotDePasse().equals(mdp)) {
                studentConnected = s;
                System.out.println("Connexion réussie !");
                studentMenu();
                return;
            }
        }
        System.out.println("Identifiants incorrects.");
    }

    private void createAccount() {
        System.out.print("Nom : ");
        String surname = scanner.nextLine();
        System.out.print("Prénom : ");
        String firstname = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        // Profession et créneaux sont obligatoires dans le constructeur
        Profession profession = Profession.Student;
        List<Crenau> crenaus = new ArrayList<>();
        Student newStudent = new Student(surname, crenaus, profession, firstname, mdp);
        students.add(newStudent);
        System.out.println("Compte créé avec succès !");
    }

    private void studentMenu() {
        while (true) {
            System.out.println("\n--- Menu étudiant ---");
            System.out.println("1. Ajouter une matière");
            System.out.println("2. Afficher mes matières");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> addMatiere();
                case "2" -> showMatieres();
                case "0" -> {
                    studentConnected = null;
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void addMatiere() {
        System.out.print("Nom de la matière : ");
        String nom = scanner.nextLine();
        System.out.print("Difficulté (1-5) : ");
        int difficulte = Integer.parseInt(scanner.nextLine());
        System.out.print("Date d'examen (AAAA-MM-JJ) : ");
        LocalDate dateExamen = LocalDate.parse(scanner.nextLine());

        Matiere matiere = new Matiere(nom, dateExamen, difficulte);
        studentConnected.ajouterMatiere(matiere);
        System.out.println("Matière ajoutée !");
    }

    private void showMatieres() {
        List<Matiere> matieres = studentConnected.getMatieres();
        if (matieres.isEmpty()) {
            System.out.println("Aucune matière enregistrée.");
        } else {
            for (Matiere m : matieres) {
                System.out.println(m);
            }
        }
    }

    public static void main(String[] args) {
        new MenuConsole().start();
    }
}
