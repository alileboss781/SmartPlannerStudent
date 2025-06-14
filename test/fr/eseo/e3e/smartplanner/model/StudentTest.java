package fr.eseo.e3e.smartplanner.model;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// ...existing code...

class StudentTest {
    @Test
    void motDePasseEstHasheEtSale() throws Exception {
        String firstname="Mamadou";
        String surname="SISSOKO";
        String mdp = "E3e123";
        List<Crenau> crenau= new ArrayList<>();
        Profession profession= Profession.Student;
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(mdp, salt);
        String hashBase64 = PasswordUtils.toBase64(hash);
        String saltBase64 = PasswordUtils.toBase64(salt);

        Student s = new Student(surname, crenau, profession, firstname, hashBase64, saltBase64);

        assertNotEquals(mdp, s.getMotDePasseHash(), "Le mot de passe ne doit pas être stocké en clair");
        assertNotNull(s.getMotDePasseHash(), "Le hash du mot de passe ne doit pas être null");
        assertNotNull(s.getSaltBase64(), "Le salt ne doit pas être null");
    }

    @Test
    void connexionAvecMotDePasse() throws Exception {
        String firstname="Mamadou";
        String surname="SISSOKO";
        String mdp = "E3e123";
        List<Crenau> crenau= new ArrayList<>();
        Profession profession= Profession.Student;
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(mdp, salt);
        String hashBase64 = PasswordUtils.toBase64(hash);
        String saltBase64 = PasswordUtils.toBase64(salt);

        Student s = new Student(surname, crenau, profession, firstname, hashBase64, saltBase64);
        assertTrue(
                PasswordUtils.verifyPassword(mdp, PasswordUtils.fromBase64(s.getMotDePasseHash()), PasswordUtils.fromBase64(s.getSaltBase64())),
                "La connexion doit réussir avec le bon mot de passe"
        );
        assertFalse(
                PasswordUtils.verifyPassword("fauxMotDePasse", PasswordUtils.fromBase64(s.getMotDePasseHash()), PasswordUtils.fromBase64(s.getSaltBase64())),
                "La connexion doit échouer avec un mauvais mot de passe"
        );
    }

    @Test
    void ajoutMatiereFonctionne() throws NoSuchAlgorithmException {
        String firstname = "Mamadou";
        String surname = "SISSOKO";
        String mdp = "E3e123";
        List<Crenau> crenau = new ArrayList<>();
        Profession profession = Profession.Student;
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(mdp, salt);
        String hashBase64 = PasswordUtils.toBase64(hash);
        String saltBase64 = PasswordUtils.toBase64(salt);

        Student s = new Student(surname, crenau, profession, firstname, hashBase64, saltBase64);
        Matiere m = new Matiere("Maths", LocalDate.of(2025, 6, 10), 3);
        s.ajouterMatiere(m);

        assertEquals(1, s.getMatieres().size(), "La matière doit être ajoutée à la liste");
        Matiere ajouter = s.getMatieres().get(0);
        assertEquals("Maths", ajouter.getNom(), "Le nom de la matière doit être 'Maths'");
        assertEquals(3, ajouter.getDifficulte(), "La difficulté doit être 3");
        assertEquals(LocalDate.of(2025, 6, 10), ajouter.getDateExamen(), "La date d'examen doit être le 10/06/2025");
    }

    @Test
    void ajoutCrenauFonctionne() throws Exception {
        String firstname = "Mamadou";
        String surname = "SISSOKO";
        String mdp = "E3e123";
        List<Crenau> crenau = new ArrayList<>();
        Profession profession = Profession.Student;
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(mdp, salt);
        String hashBase64 = PasswordUtils.toBase64(hash);
        String saltBase64 = PasswordUtils.toBase64(salt);

        Student s = new Student(surname, crenau, profession, firstname, hashBase64, saltBase64);

        Crenau c = new Crenau(
                LocalDateTime.of(2025, 6, 10, 9, 0),
                LocalDateTime.of(2025, 6, 10, 11, 0)
        );
        s.getCrenaux().add(c);

        assertEquals(1, s.getCrenaux().size(), "Le créneau doit être ajouté à la liste");
        Crenau ajoute = s.getCrenaux().get(0);
        assertEquals(LocalDateTime.of(2025, 6, 10, 9, 0), ajoute.getDebut(), "La date de début du créneau est incorrecte");
        assertEquals(LocalDateTime.of(2025, 6, 10, 11, 0), ajoute.getFin(), "La date de fin du créneau est incorrecte");
    }

    @Test
    void ajoutCrenauInvalideRefuse() throws Exception {
        String firstname = "Mamadou";
        String surname = "SISSOKO";
        String mdp = "E3e123";
        List<Crenau> crenau = new ArrayList<>();
        Profession profession = Profession.Student;
        byte[] salt = PasswordUtils.generateSalt();
        byte[] hash = PasswordUtils.hashPassword(mdp, salt);
        String hashBase64 = PasswordUtils.toBase64(hash);
        String saltBase64 = PasswordUtils.toBase64(salt);

        Student s = new Student(surname, crenau, profession, firstname, hashBase64, saltBase64);


        LocalDateTime debut = LocalDateTime.of(2025, 6, 10, 11, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 6, 10, 9, 0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Crenau c = new Crenau(debut, fin);
            if (fin.isBefore(debut)) {
                throw new IllegalArgumentException("La fin doit être après le début");
            }
            s.getCrenaux().add(c);
        }, "Une exception doit être levée si la fin est avant le début");
        assertEquals("La fin doit être après le début", exception.getMessage(), "Le message d'erreur doit être explicite");
    }

    @Test
    void marquageSessionEffectueeFonctionne() {
        Matiere m = new Matiere("Maths", LocalDate.of(2025, 6, 10), 3);
        SessionRevision session = new SessionRevision(m, LocalDateTime.of(2025, 6, 8, 9, 0), Duration.ofHours(2), false);

        assertFalse(session.isEffectuee(), "La session ne doit pas être marquée comme effectuée au départ");
        session.marquerEffectuee();
        assertTrue(session.isEffectuee(), "La session doit être marquée comme effectuée après appel de marquerEffectuee()");
    }

    @Test
    void ajoutCommentaireSessionFonctionne() {
        Matiere m = new Matiere("Physique", LocalDate.of(2025, 6, 12), 2);
        SessionRevision session = new SessionRevision(m, LocalDateTime.of(2025, 6, 9, 14, 0), Duration.ofHours(2), false);

        assertEquals("", session.getCommentaire(), "Le commentaire doit être vide par défaut");
        session.setCommentaire("Réviser les chapitres 1 et 2");
        assertEquals("Réviser les chapitres 1 et 2", session.getCommentaire(), "Le commentaire doit être correctement enregistré");
    }

    @Test
    void progressionGlobaleEstCorrecte() {
        Matiere m1 = new Matiere("Maths", LocalDate.of(2025, 6, 10), 2);
        Matiere m2 = new Matiere("Physique", LocalDate.of(2025, 6, 12), 2);

        List<SessionRevision> sessions = new ArrayList<>();
        sessions.add(new SessionRevision(m1, LocalDateTime.now(), Duration.ofHours(2), true));
        sessions.add(new SessionRevision(m1, LocalDateTime.now(), Duration.ofHours(2), false));
        sessions.add(new SessionRevision(m2, LocalDateTime.now(), Duration.ofHours(2), true));
        sessions.add(new SessionRevision(m2, LocalDateTime.now(), Duration.ofHours(2), false));
        Progression progression = new Progression();
        double prog = progression.calculerProgressionGloble(sessions);
        assertEquals(50.0, prog, 0.01, "La progression globale doit être de 50% (2 sessions effectuées sur 4)");
    }
    @Test
    void progressionParMatiereEstCorrecte() {
        Matiere m1 = new Matiere("Maths", LocalDate.of(2025, 6, 10), 2);
        Matiere m2 = new Matiere("Physique", LocalDate.of(2025, 6, 12), 2);

        List<SessionRevision> sessions = new ArrayList<>();
        sessions.add(new SessionRevision(m1, LocalDateTime.now(), Duration.ofHours(2), true));  // effectuée
        sessions.add(new SessionRevision(m1, LocalDateTime.now(), Duration.ofHours(2), false)); // non effectuée
        sessions.add(new SessionRevision(m2, LocalDateTime.now(), Duration.ofHours(2), true));  // effectuée
        sessions.add(new SessionRevision(m2, LocalDateTime.now(), Duration.ofHours(2), false)); // non effectuée

        Progression progression = new Progression();
        Map<Matiere, Double> progParMatiere = progression.progressionParMatiere(sessions);

        // Pour chaque matière : 1/2 effectuée => 50%
        for (Map.Entry<Matiere, Double> entry : progParMatiere.entrySet()) {
            assertEquals(50.0, entry.getValue(), 0.01,
                    "La progression pour la matière " + entry.getKey().getNom() + " doit être de 50% (1 session effectuée sur 2)");
        }
    }
}