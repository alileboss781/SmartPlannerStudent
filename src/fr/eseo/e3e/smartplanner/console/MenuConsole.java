package fr.eseo.e3e.smartplanner.console;
import fr.eseo.e3e.smartplanner.files.SauvegardeInfos;
import fr.eseo.e3e.smartplanner.model.*;
import fr.eseo.e3e.smartplanner.model.SessionRevision;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MenuConsole {
    private final List<Student> students = new ArrayList<>();
    private Student studentConnected = null;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n--- Smart Revision Planner ---");
            System.out.println("1. Connexion");
            System.out.println("2. Creer un compte");
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
        System.out.print("Prenom : ");
        String firstname = scanner.nextLine();

        String mdp;
        java.io.Console console = System.console();
        if (console != null) {
            char[] pwdArray = console.readPassword("Mot de passe : ");
            mdp = new String(pwdArray);
        } else {
            System.out.print("Mot de passe : ");
            mdp = scanner.nextLine();
        }

        for (Student s : students) {
            if (s.getSurname().equals(surname) && s.getFirsname().equals(firstname)) {
                try {
                    byte[] storedHash = PasswordUtils.fromBase64(s.getMotDePasseHash());
                    byte[] salt = PasswordUtils.fromBase64(s.getSaltBase64());
                    if (PasswordUtils.verifyPassword(mdp, storedHash, salt)) {
                        studentConnected = s;
                        System.out.println("Connexion reussie !");
                        studentMenu();
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la verification du mot de passe.");
                }
            }
        }
        System.out.println("Identifiants incorrects.");
    }

    private void createAccount() {
        System.out.print("Nom : ");
        String surname = scanner.nextLine();
        System.out.print("Prenom : ");
        String firstname = scanner.nextLine();

<<<<<<< HEAD
        Profession profession = Profession.Student;
        List<Crenau> crenaus = new ArrayList<>();
        Student newStudent = new Student(surname, crenaus, profession, firstname, mdp);
        students.add(newStudent);

        // 🔽 SAUVEGARDE dans le fichier partagé
        SauvegardeInfos.sauvegarderEtudiant(newStudent);

        System.out.println("Compte créé avec succès !");
=======
        String mdp;
        java.io.Console console = System.console();
        if (console != null) {
            char[] pwdArray = console.readPassword("Mot de passe : ");
            mdp = new String(pwdArray);
        } else {
            System.out.print("Mot de passe : ");
            mdp = scanner.nextLine();
        }

        try {
            byte[] salt = PasswordUtils.generateSalt();
            byte[] hash = PasswordUtils.hashPassword(mdp, salt);
            String hashBase64 = PasswordUtils.toBase64(hash);
            String saltBase64 = PasswordUtils.toBase64(salt);

            Profession profession = Profession.Student;
            List<Crenau> crenaus = new ArrayList<>();
            Student newStudent = new Student(surname, crenaus, profession, firstname, hashBase64, saltBase64);
            students.add(newStudent);
            System.out.println("Compte cree avec succes !");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Erreur lors du hachage du mot de passe.");
        }
>>>>>>> 5c6ea0e6c91d4718c920c2d81e20682cfdbc3bde
    }

    private void studentMenu() {
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        Progression progression = new Progression();

        while (true) {
            System.out.println("\n--- Menu etudiant ---");
            System.out.println("1. Ajouter une matiere");
            System.out.println("2. Afficher mes matieres");
            System.out.println("3. Ajouter un creneau de disponibilite");
            System.out.println("4. Afficher mes creneaux");
            System.out.println("5. Planifier mes revisions");
            System.out.println("6. Afficher mes sessions de revision");
            System.out.println("7. Marquer une session comme effectuee");
            System.out.println("8. Voir ma progression");
            System.out.println("9. Ajouter un commentaire a une session");
            System.out.println("0. Deconnexion");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> addMatiere();
                case "2" -> showMatieres();
                case "3" -> addCrenau();
                case "4" -> showCrenaux();
                case "5" ->  {
                    if (studentConnected.getCrenaux().isEmpty()) {
                        System.out.println("Aucun creneau disponible, veuillez en ajouter avant de planifier !");
                    } else {
                        planificateur.planifier(studentConnected.getCrenaux());
                        System.out.println("Planification terminee !");
                    }
                }
                case "6" -> showSessions(planificateur.getSessions());
                case "7" -> markSessionDone(planificateur.getSessions());
                case "8" -> progression.afficherResume(planificateur.getSessions());
                case "9" -> addCommentToSession(planificateur.getSessions());
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
        System.out.print("Nom de la matiere : ");
        String nom = scanner.nextLine();
        System.out.print("Difficulte (1-5) : ");
        int difficulte = Integer.parseInt(scanner.nextLine());

        LocalDate dateExamen = null;
        while (dateExamen == null) {
            System.out.print("Date d examen (AAAA-MM-JJ) : ");
            String dateStr = scanner.nextLine();
            try {
                dateExamen = LocalDate.parse(dateStr);
            } catch (Exception e) {
                System.out.println("Format de date invalide. Veuillez saisir la date au format AAAA-MM-JJ (ex: 2025-06-09).");
            }
        }

        Matiere matiere = new Matiere(nom, dateExamen, difficulte);
        studentConnected.ajouterMatiere(matiere);
<<<<<<< HEAD


        // 🔽 Sauvegarde de la matière dans le fichier texte
        SauvegardeInfos.sauvegarderMatiere(studentConnected, matiere);

        System.out.println("Matière ajoutée !");
=======
        System.out.println("Matiere ajoutee !");
>>>>>>> 5c6ea0e6c91d4718c920c2d81e20682cfdbc3bde
    }

    private void showMatieres() {
        List<Matiere> matieres = studentConnected.getMatieres();
        if (matieres.isEmpty()) {
            System.out.println("Aucune matiere enregistree.");
        } else {
            for (Matiere m : matieres) {
                System.out.println(m);
            }
        }
    }

    private void addCrenau() {
        LocalDateTime debut = null;
        while (debut == null) {
            System.out.print("Debut du creneau (AAAA-MM-JJTHH:MM) : ");
            String debutStr = scanner.nextLine().trim();
            try {
                debut = LocalDateTime.parse(debutStr);
            } catch (Exception e) {
                System.out.println("Format invalide. Exemple attendu : 2025-06-10T09:00");
            }
        }

        LocalDateTime fin = null;
        while (fin == null) {
            System.out.print("Fin du creneau (AAAA-MM-JJTHH:MM) : ");
            String finStr = scanner.nextLine().trim();
            try {
                fin = LocalDateTime.parse(finStr);
            } catch (Exception e) {
                System.out.println("Format invalide. Exemple attendu : 2025-06-10T11:00");
            }
        }

        Crenau c = new Crenau(debut, fin);
        studentConnected.getCrenaux().add(c);
        System.out.println("Creneau ajoute !");
    }

    private void showCrenaux() {
        List<Crenau> crenaux = studentConnected.getCrenaux();
        if (crenaux.isEmpty()) {
            System.out.println("Aucun creneau enregistre.");
        } else {
            for (Crenau c : crenaux) {
                System.out.println("Debut : " + c.getDebut() + " | Fin : " + c.getFin());
            }
        }
    }

    private void showSessions(List<SessionRevision> sessions) {
        if (sessions.isEmpty()) {
            System.out.println("Aucune session planifiee.");
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
        System.out.print("Numero de la session a marquer comme effectuee : ");
        int num = Integer.parseInt(scanner.nextLine());
        if (num < 1 || num > sessions.size()) {
            System.out.println("Numero invalide.");
            return;
        }
        sessions.get(num - 1).marquerEffectuee();
        System.out.println("Session marquee comme effectuee !");
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        new MenuConsole().start();
    }

    private void addCommentToSession(List<SessionRevision> sessions) {
        showSessions(sessions);
        if (sessions.isEmpty()) return;
        System.out.print("Numero de la session a commenter : ");
        String numStr = scanner.nextLine();
        int num;
        try {
            num = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un numero valide.");
            return;
        }
        if (num < 1 || num > sessions.size()) {
            System.out.println("Numero invalide.");
            return;
        }
        System.out.print("Votre commentaire : ");
        String commentaire = scanner.nextLine();
        sessions.get(num - 1).setCommentaire(commentaire);
        System.out.println("Commentaire ajoute !");
    }
}