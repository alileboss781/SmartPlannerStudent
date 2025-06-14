package fr.eseo.e3e.smartplanner.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanificationtTest {

    @Test
    void planificationSimpleRespecteDifficulteEtCreneaux() throws Exception {
        // Préparation d'un étudiant avec 2 matières et 3 créneaux
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

        Matiere m1 = new Matiere("Maths", LocalDate.of(2025, 6, 20), 2);
        Matiere m2 = new Matiere("Physique", LocalDate.of(2025, 6, 20), 1);
        s.ajouterMatiere(m1);
        s.ajouterMatiere(m2);

        List<Crenau> creneaux = List.of(
                new Crenau(LocalDateTime.of(2025, 6, 10, 9, 0), LocalDateTime.of(2025, 6, 10, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 12, 9, 0), LocalDateTime.of(2025, 6, 12, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 18, 9, 0), LocalDateTime.of(2025, 6, 18, 11, 0))
        );

        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(s.getMatieres(), sessions);
        planificateur.planifier(creneaux);
        assertEquals(3, sessions.size());
        long mathsCount = sessions.stream().filter(sr -> sr.getManiere().getNom().equals("Maths")).count();
        long physiqueCount = sessions.stream().filter(sr -> sr.getManiere().getNom().equals("Physique")).count();
        assertEquals(2, mathsCount);
        assertEquals(1, physiqueCount);
    }

    @Test
    void aucuneSessionApresDateExamen() throws Exception {
        // Préparation d'une matière avec une date d'examen avant le dernier créneau
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

        Matiere m = new Matiere("Chimie", LocalDate.of(2025, 6, 12), 2);
        s.ajouterMatiere(m);

        List<Crenau> creneaux = List.of(
                new Crenau(LocalDateTime.of(2025, 6, 10, 9, 0), LocalDateTime.of(2025, 6, 10, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 13, 9, 0), LocalDateTime.of(2025, 6, 13, 11, 0))
        );

        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(s.getMatieres(), sessions);
        planificateur.planifier(creneaux);


        assertEquals(1, sessions.size());
        assertTrue(sessions.get(0).getDate().toLocalDate().isBefore(m.getDateExamen()));
    }

    @Test
    void roundRobinRepartitLesSessions() throws Exception {
        // 2 matières, 4 créneaux, difficulté 2 pour chaque
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

        Matiere m1 = new Matiere("Maths", LocalDate.of(2025, 6, 20), 2);
        Matiere m2 = new Matiere("Physique", LocalDate.of(2025, 6, 20), 2);
        s.ajouterMatiere(m1);
        s.ajouterMatiere(m2);

        List<Crenau> creneaux = List.of(
                new Crenau(LocalDateTime.of(2025, 6, 10, 9, 0), LocalDateTime.of(2025, 6, 10, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 11, 9, 0), LocalDateTime.of(2025, 6, 11, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 12, 9, 0), LocalDateTime.of(2025, 6, 12, 11, 0)),
                new Crenau(LocalDateTime.of(2025, 6, 13, 9, 0), LocalDateTime.of(2025, 6, 13, 11, 0))
        );

        List<SessionRevision> sessions = new ArrayList<>();
        Planificateur planificateur = new Planificateur(s.getMatieres(), sessions);
        planificateur.planifier(creneaux);

        // Les sessions doivent alterner entre Maths et Physique
        assertEquals(4, sessions.size());
        assertEquals("Maths", sessions.get(0).getManiere().getNom());
        assertEquals("Physique", sessions.get(1).getManiere().getNom());
        assertEquals("Maths", sessions.get(2).getManiere().getNom());
        assertEquals("Physique", sessions.get(3).getManiere().getNom());
    }
}
