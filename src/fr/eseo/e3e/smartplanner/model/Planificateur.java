package fr.eseo.e3e.smartplanner.model;

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
        List<Crenau> crenauxDispo = new java.util.ArrayList<>(crenaux);

        int[] sessionsRestantes = new int[matieres.size()];
        for (int i = 0; i < matieres.size(); i++) {
            sessionsRestantes[i] = matieres.get(i).getDifficulte();
        }

        int totalSessions = 0;
        for (int n : sessionsRestantes) totalSessions += n;

        int cIndex = 0;
        while (cIndex < crenauxDispo.size() && totalSessions > 0) {
            for (int m = 0; m < matieres.size(); m++) {
                if (sessionsRestantes[m] > 0) {
                    // Cherche le prochain créneau avant la date d'examen de la matière
                    int foundIndex = -1;
                    for (int j = cIndex; j < crenauxDispo.size(); j++) {
                        if (crenauxDispo.get(j).getDebut().toLocalDate().isBefore(matieres.get(m).getDateExamen())) {
                            foundIndex = j;
                            break;
                        }
                    }
                    if (foundIndex != -1) {
                        Crenau c = crenauxDispo.remove(foundIndex);
                        LocalDateTime sessionDebut = c.getDebut();
                        Duration duree = Duration.between(c.getDebut(), c.getFin());
                        if (duree.isZero() || duree.isNegative()) {
                            duree = Duration.ofHours(2);
                        }
                        SessionRevision session = new SessionRevision(matieres.get(m), sessionDebut, duree, false);
                        sessions.add(session);
                        sessionsRestantes[m]--;
                        totalSessions--;
                    }
                    // Si aucun créneau avant la date d'examen, on ne planifie plus pour cette matière
                }
            }
        }
        if (totalSessions > 0) {
            System.out.println("Plus de créneaux disponibles pour planifier toutes les sessions avant les examens.");
        }
    }
}
