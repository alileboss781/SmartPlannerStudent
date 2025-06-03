package fr.eseo.e3e.smartplanner.model;

import fr.eseo.e3e.smartplanner.SessionRevision;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;

public class Planificateur {
    List<Matiere> matieres;
    List<SessionRevision> sessions;

    public Planificateur(List<Matiere> matieres, List<SessionRevision> session) {
        this.matieres = matieres;
        this.sessions = session;
    }
    public void ajouterMatiere(Matiere m){
        matieres.add(m);
    }

    public List<SessionRevision> getSessions() {
        return sessions;
    }
    public void planifier(List<Crenau> crenaux) {
        sessions.clear();
        // On fait une copie de la liste pour retirer les créneaux déjà utilisés
        List<Crenau> crenauxDispo = new java.util.ArrayList<>(crenaux);

        for (Matiere matiere : matieres) {
            int nbSessions = matiere.getDifficulte();
            for (int i = 0; i < nbSessions; i++) {
                if (crenauxDispo.isEmpty()) {
                    System.out.println("Plus de créneaux disponibles pour planifier toutes les sessions.");
                    return;
                }
                // On prend et retire le premier créneau disponible
                Crenau c = crenauxDispo.remove(0);
                LocalDateTime sessionDebut = c.getDebut();
                Duration duree = Duration.between(c.getDebut(), c.getFin());
                if (duree.isZero() || duree.isNegative()) {
                    duree = Duration.ofHours(2); // Valeur par défaut si problème
                }
                SessionRevision session = new SessionRevision(matiere, sessionDebut, duree, false);
                sessions.add(session);
            }
        }
    }
}
