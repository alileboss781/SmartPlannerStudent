package fr.eseo.e3e.smartplanner.UI;

import fr.eseo.e3e.smartplanner.model.*;
import fr.eseo.e3e.smartplanner.model.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UI extends JFrame {

    private final List<Student> students = new ArrayList<>();
    private Student studentConnected = null;

    private JTextArea displayArea = new JTextArea(15, 40);

    private JButton btnCreateAccount = new JButton("Créer un compte");
    private JButton btnLogin = new JButton("Connexion");
    private JButton btnLogout = new JButton("Déconnexion");
    private JButton btnAddMatiere = new JButton("Ajouter matière");
    private JButton btnAddCrenau = new JButton("Ajouter créneau");
    private JButton btnPlanifier = new JButton("Planifier");
    private JButton btnShowSessions = new JButton("Afficher sessions");
    private JButton btnMarkDone = new JButton("Marquer comme faite");
    private JButton btnAddComment = new JButton("Ajouter commentaire");
    private JButton btnProgression = new JButton("Voir progression");

    public UI() {
        setTitle("Smart Revision Planner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 500));

        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(245, 245, 245));
        styleButton(btnCreateAccount);
        styleButton(btnLogin);
        styleButton(btnLogout);
        topPanel.add(btnCreateAccount);
        topPanel.add(btnLogin);
        topPanel.add(btnLogout);

        // Display area
        displayArea.setEditable(false);
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Matières & Créneaux", buildPanel(btnAddMatiere, btnAddCrenau));
        tabbedPane.add("Planification", buildPanel(btnPlanifier, btnShowSessions));
        tabbedPane.add("Sessions", buildPanel(btnMarkDone, btnAddComment));
        tabbedPane.add("Suivi", buildPanel(btnProgression));

        // Layout
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.SOUTH);

        // Actions
        btnCreateAccount.addActionListener(e -> creerCompte());
        btnLogin.addActionListener(e -> connexion());
        btnLogout.addActionListener(e -> {
            studentConnected = null;
            display("Déconnecté !");
        });
        btnAddMatiere.addActionListener(e -> ajouterMatiere());
        btnAddCrenau.addActionListener(e -> ajouterCrenau());
        btnPlanifier.addActionListener(e -> planifier());
        btnShowSessions.addActionListener(e -> afficherSessions());
        btnMarkDone.addActionListener(e -> marquerSession());
        btnAddComment.addActionListener(e -> ajouterCommentaire());
        btnProgression.addActionListener(e -> voirProgression());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private JPanel buildPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 40));
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        return panel;
    }

    public static void setUIFont(Font f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    private void display(String message) {
        displayArea.append(message + "\n");
    }

    private void creerCompte() {
        String nom = JOptionPane.showInputDialog(this, "Nom :");
        String prenom = JOptionPane.showInputDialog(this, "Prénom :");
        String mdp = JOptionPane.showInputDialog(this, "Mot de passe :");
        if (nom == null || prenom == null || mdp == null) return;
        try {
            byte[] salt = PasswordUtils.generateSalt();
            byte[] hash = PasswordUtils.hashPassword(mdp, salt);
            String hashBase64 = PasswordUtils.toBase64(hash);
            String saltBase64 = PasswordUtils.toBase64(salt);
            Student s = new Student(nom, new ArrayList<>(), Profession.Student, prenom, hashBase64, saltBase64);
            students.add(s);
            display("Compte créé !");
        } catch (Exception e) {
            display("Erreur lors du hachage du mot de passe.");
        }
    }

    private void connexion() {
        String nom = JOptionPane.showInputDialog(this, "Nom :");
        String prenom = JOptionPane.showInputDialog(this, "Prénom :");
        String mdp = JOptionPane.showInputDialog(this, "Mot de passe :");
        if (nom == null || prenom == null || mdp == null) return;
        for (Student s : students) {
            if (s.getSurname().equals(nom) && s.getFirsname().equals(prenom)) {
                try {
                    byte[] storedHash = PasswordUtils.fromBase64(s.getMotDePasseHash());
                    byte[] salt = PasswordUtils.fromBase64(s.getSaltBase64());
                    if (PasswordUtils.verifyPassword(mdp, storedHash, salt)) {
                        studentConnected = s;
                        display("Connecté !");
                        return;
                    }
                } catch (Exception e) {
                    display("Erreur lors de la vérification du mot de passe.");
                    return;
                }
            }
        }
        display("Identifiants incorrects.");
    }

    private void ajouterMatiere() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        String nom = JOptionPane.showInputDialog(this, "Nom de la matière :");
        String diffStr = JOptionPane.showInputDialog(this, "Difficulté (1-5) :");
        int difficulte;
        try { difficulte = Integer.parseInt(diffStr); }
        catch (Exception e) { display("Difficulté invalide."); return; }
        String dateStr = JOptionPane.showInputDialog(this, "Date d'examen (AAAA-MM-JJ) :");
        LocalDate date;
        try { date = LocalDate.parse(dateStr); }
        catch (Exception e) { display("Date invalide."); return; }
        studentConnected.ajouterMatiere(new Matiere(nom, date, difficulte));
        display("Matière ajoutée !");
    }

    private void ajouterCrenau() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        String debutStr = JOptionPane.showInputDialog(this, "Début (AAAA-MM-JJTHH:MM) :");
        String finStr = JOptionPane.showInputDialog(this, "Fin (AAAA-MM-JJTHH:MM) :");
        try {
            LocalDateTime debut = LocalDateTime.parse(debutStr);
            LocalDateTime fin = LocalDateTime.parse(finStr);
            studentConnected.getCrenaux().add(new Crenau(debut, fin));
            display("Créneau ajouté !");
        } catch (Exception e) {
            display("Format de date/heure invalide.");
        }
    }

    private void planifier() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        planificateur.planifier(studentConnected.getCrenaux());
        display("Planification terminée !");
    }

    private void afficherSessions() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        planificateur.planifier(studentConnected.getCrenaux());
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (SessionRevision s : planificateur.getSessions()) {
            sb.append(i).append(". ").append(s).append("\n");
            i++;
        }
        display(sb.toString());
    }

    private void marquerSession() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        planificateur.planifier(studentConnected.getCrenaux());
        if (sessions.isEmpty()) { display("Aucune session."); return; }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (SessionRevision s : sessions) {
            sb.append(i).append(". ").append(s).append("\n");
            i++;
        }
        String numStr = JOptionPane.showInputDialog(this, sb + "\nNuméro de la session à marquer comme effectuée :");
        int num;
        try { num = Integer.parseInt(numStr); }
        catch (Exception e) { display("Numéro invalide."); return; }
        if (num < 1 || num > sessions.size()) { display("Numéro invalide."); return; }
        sessions.get(num - 1).marquerEffectuee();
        display("Session marquée comme effectuée !");
    }

    private void ajouterCommentaire() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        planificateur.planifier(studentConnected.getCrenaux());
        if (sessions.isEmpty()) { display("Aucune session."); return; }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (SessionRevision s : sessions) {
            sb.append(i).append(". ").append(s).append("\n");
            i++;
        }
        String numStr = JOptionPane.showInputDialog(this, sb + "\nNuméro de la session à commenter :");
        int num;
        try { num = Integer.parseInt(numStr); }
        catch (Exception e) { display("Numéro invalide."); return; }
        if (num < 1 || num > sessions.size()) { display("Numéro invalide."); return; }
        String commentaire = JOptionPane.showInputDialog(this, "Votre commentaire :");
        sessions.get(num - 1).setCommentaire(commentaire);
        display("Commentaire ajouté !");
    }

    private void voirProgression() {
        if (studentConnected == null) { display("Connecte-toi d'abord."); return; }
        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(studentConnected.getMatieres(), sessions);
        planificateur.planifier(studentConnected.getCrenaux());
        Progression progression = new Progression();
        double prog = progression.calculerProgressionGloble(sessions);
        StringBuilder sb = new StringBuilder();
        sb.append("Progression globale : ").append(String.format("%.2f", prog)).append("%\n");
        for (var entry : progression.progressionParMatiere(sessions).entrySet()) {
            sb.append("Matière : ").append(entry.getKey().getNom())
                    .append(" - Progression : ").append(String.format("%.2f", entry.getValue())).append("%\n");
        }
        display(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UI::new);
    }
}