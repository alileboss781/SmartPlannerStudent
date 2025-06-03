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
        Iterator<Crenau> itCrenau = crenaux.iterator();

        for (Matiere matiere : matieres) {
            int nbSessions = matiere.getDifficulte();
            for (int i = 0; i < nbSessions; i++) {
                if (!itCrenau.hasNext()) {
                    System.out.println("Plus de créneaux disponibles pour planifier toutes les sessions.");
                    return;
                }
                Crenau c = itCrenau.next();
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
