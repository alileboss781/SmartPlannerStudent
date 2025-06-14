package fr.eseo.e3e.smartplanner.console;
import fr.eseo.e3e.smartplanner.files.SauvegardeInfos;
import fr.eseo.e3e.smartplanner.model.*;
import fr.eseo.e3e.smartplanner.model.SessionRevision;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

        Profession profession = Profession.Student;
        List<Crenau> crenaus = new ArrayList<>();
        Student newStudent = new Student(surname, crenaus, profession, firstname, mdp);
        students.add(newStudent);

        // 🔽 SAUVEGARDE dans le fichier partagé
        SauvegardeInfos.sauvegarderEtudiant(newStudent);

        System.out.println("Compte créé avec succès !");
    }

    private void studentMenu() {
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        Progression progression = new Progression();

        while (true) {
            System.out.println("\n--- Menu étudiant ---");
            System.out.println("1. Ajouter une matière");
            System.out.println("2. Afficher mes matières");
            System.out.println("3. Ajouter un créneau de disponibilité");
            System.out.println("4. Afficher mes créneaux");
            System.out.println("5. Planifier mes révisions");
            System.out.println("6. Afficher mes sessions de révision");
            System.out.println("7. Marquer une session comme effectuée");
            System.out.println("8. Voir ma progression");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> addMatiere();
                case "2" -> showMatieres();
                case "3" -> addCrenau();
                case "4" -> showCrenaux();
                case "5" ->  {
                    if (studentConnected.getCrenaux().isEmpty()) {
                        System.out.println("Aucun créneau disponible, veuillez en ajouter avant de planifier !");
                    } else {
                        planificateur.planifier(studentConnected.getCrenaux());
                        System.out.println("Planification terminée !");
                    }
                }
                case "6" -> showSessions(planificateur.getSessions());
                case "7" -> markSessionDone(planificateur.getSessions());
                case "8" -> progression.afficherResume(planificateur.getSessions());
                case "0" -> {
                    SauvegardeInfos.sauvegarderEtudiant(studentConnected); // sauvegarde avant déconnexion
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


        // 🔽 Sauvegarde de la matière dans le fichier texte
        SauvegardeInfos.sauvegarderMatiere(studentConnected, matiere);

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

    private void addCrenau() {
        System.out.print("Début du créneau (AAAA-MM-JJTHH:MM, ex: 2025-06-10T14:00) : ");
        LocalDateTime debut = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Fin du créneau (AAAA-MM-JJTHH:MM, ex: 2025-06-10T16:00) : ");
        LocalDateTime fin = LocalDateTime.parse(scanner.nextLine());
        Crenau c = new Crenau(debut, fin);
        studentConnected.getCrenaux().add(c);
        System.out.println("Créneau ajouté !");
    }

    private void showCrenaux() {
        List<Crenau> crenaux = studentConnected.getCrenaux();
        if (crenaux.isEmpty()) {
            System.out.println("Aucun créneau enregistré.");
        } else {
            for (Crenau c : crenaux) {
                System.out.println("Début : " + c.getDebut() + " | Fin : " + c.getFin());
            }
        }
    }

    private void showSessions(List<SessionRevision> sessions) {
        if (sessions.isEmpty()) {
            System.out.println("Aucune session planifiée.");
        } else {
            int i = 1;
            for (SessionRevision s : sessions) {
                System.out.println(i + ". " + s);
                i++;
            }
        }
    }

    private void markSessionDone(List<SessionRevision> sessions) {
        showSessions(sessions);
        if (sessions.isEmpty()) return;
        System.out.print("Numéro de la session à marquer comme effectuée : ");
        int num = Integer.parseInt(scanner.nextLine());
        if (num < 1 || num > sessions.size()) {
            System.out.println("Numéro invalide.");
            return;
        }
        sessions.get(num - 1).marquerEffectuee();
        System.out.println("Session marquée comme effectuée !");
    }

    public static void main(String[] args) {
        new MenuConsole().start();
    }
}