package fr.eseo.e3e.smartplanner.model;

import fr.eseo.e3e.smartplanner.files.SauvegardeInfos;

import java.util.ArrayList;
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


    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

    public void setSessions(List<SessionRevision> sessions) {
        this.sessions = sessions;
    }

    public List<SessionRevision> getSessions() {
        return sessions;
    }
    // ...existing code...
    public void planifier(List<Crenau> crenaux) {
        sessions.clear();
        List<Crenau> crenauxDispo = new ArrayList<>(crenaux);

        int[] sessionsRestantes = new int[matieres.size()];
        for (int i = 0; i < matieres.size(); i++) {
            sessionsRestantes[i] = matieres.get(i).getDifficulte();
        }

        int totalSessions = 0;
        for (int n : sessionsRestantes) totalSessions += n;

        while (!crenauxDispo.isEmpty() && totalSessions > 0) {
            boolean sessionPlanifiee = false;
            for (int m = 0; m < matieres.size(); m++) {
                if (sessionsRestantes[m] > 0) {
                    int foundIndex = -1;
                    for (int j = 0; j < crenauxDispo.size(); j++) {
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
                        sessionPlanifiee = true;
                    } else {
                        sessionsRestantes[m] = 0;
                    }
                }
            }
            // Si aucune session n'a été planifiée lors de ce tour, on sort pour éviter une boucle infinie
            if (!sessionPlanifiee) {
                break;
            }
        }
        if (totalSessions > 0) {
            System.out.println("Plus de créneaux disponibles pour planifier toutes les sessions avant les examens.");
        }
    }
}
