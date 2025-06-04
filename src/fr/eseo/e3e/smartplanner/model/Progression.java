package fr.eseo.e3e.smartplanner.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Progression {
    // Pourcentage global de sessions effectuées
    public double calculerProgressionGloble(List<SessionRevision> sessions){
        if (sessions == null || sessions.isEmpty()) return 0.0;
        long effectuees = sessions.stream().filter(SessionRevision::isEffectuee).count();
        return (effectuees * 100.0) / sessions.size();
    }

    // Pourcentage moyen de progression par matière
    public double calculerProgressionParMatiere(List<SessionRevision> sessions){
        Map<Matiere, Double> progressionParMatiere = progressionParMatiere(sessions);
        if (progressionParMatiere.isEmpty()) return 0.0;
        double somme = 0.0;
        for (double val : progressionParMatiere.values()) {
            somme += val;
        }
        return somme / progressionParMatiere.size();
    }

    // Pourcentage de progression pour chaque matière
    public Map<Matiere,Double> progressionParMatiere(List<SessionRevision> sessions){
        Map<Matiere, Integer> totalParMatiere = new HashMap<>();
        Map<Matiere, Integer> effectueesParMatiere = new HashMap<>();
        for (SessionRevision s : sessions) {
            Matiere m = s.getManiere();
            totalParMatiere.put(m, totalParMatiere.getOrDefault(m, 0) + 1);
            if (s.isEffectuee()) {
                effectueesParMatiere.put(m, effectueesParMatiere.getOrDefault(m, 0) + 1);
            }
        }
        Map<Matiere, Double> progression = new HashMap<>();
        for (Matiere m : totalParMatiere.keySet()) {
            int total = totalParMatiere.get(m);
            int fait = effectueesParMatiere.getOrDefault(m, 0);
            progression.put(m, (fait * 100.0) / total);
        }
        return progression;
    }

    // Affiche un résumé de la progression
    public void afficherResume(List<SessionRevision> sessions){
        System.out.println("Progression globale : " + calculerProgressionGloble(sessions) + "%");
        Map<Matiere, Double> progression = progressionParMatiere(sessions);
        for (Map.Entry<Matiere, Double> entry : progression.entrySet()) {
            System.out.println("Matière : " + entry.getKey().getNom() + " - Progression : " + entry.getValue() + "%");
        }
    }
}