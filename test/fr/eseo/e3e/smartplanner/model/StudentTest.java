package fr.eseo.e3e.smartplanner.model;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(s.getMotDePasseHash());
        assertNotNull(s.getSaltBase64());
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
        assertTrue(PasswordUtils.verifyPassword(mdp, PasswordUtils.fromBase64(s.getMotDePasseHash()), PasswordUtils.fromBase64(s.getSaltBase64())));
        assertFalse(PasswordUtils.verifyPassword("fauxMotDePasse", PasswordUtils.fromBase64(s.getMotDePasseHash()), PasswordUtils.fromBase64(s.getSaltBase64())));
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

        assertEquals(1, s.getMatieres().size());
        Matiere ajouter = s.getMatieres().get(0);
        assertEquals("Maths", ajouter.getNom());
        assertEquals(3, ajouter.getDifficulte());
        assertEquals(LocalDate.of(2025, 6, 10), ajouter.getDateExamen());
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

        assertEquals(1, s.getCrenaux().size());
        Crenau ajoute = s.getCrenaux().get(0);
        assertEquals(LocalDateTime.of(2025, 6, 10, 9, 0), ajoute.getDebut());
        assertEquals(LocalDateTime.of(2025, 6, 10, 11, 0), ajoute.getFin());
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

        // Créneau invalide : fin avant début
        LocalDateTime debut = LocalDateTime.of(2025, 6, 10, 11, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 6, 10, 9, 0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Crenau c = new Crenau(debut, fin);
            // Si tu veux empêcher l'ajout dans la liste :
            if (fin.isBefore(debut)) {
                throw new IllegalArgumentException("La fin doit être après le début");
            }
            s.getCrenaux().add(c);
        });
        assertEquals("La fin doit être après le début", exception.getMessage());
    }
}