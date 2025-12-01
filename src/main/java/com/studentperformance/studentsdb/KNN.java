package com.studentperformance.studentsdb;

import java.util.*;

public class KNN {
    private int k;
    private ArrayList<Student> dataset ;
    public KNN(int k , ArrayList<Student> data){
        this.k= k;
        this.dataset = data;
    }
    public String predict(Student newstudent){
        // trie automatique dans l'ordre croissant
        PriorityQueue<StudentDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(sd ->sd.distance));
        for (Student s : dataset){
            double distance_calcule = Math.sqrt(Math.pow(s.getScoreModules() - newstudent.getScoreModules(), 2) +
                    Math.pow(s.getScoreAttendance() - newstudent.getScoreAttendance(), 2) +
                    Math.pow(s.getScoreActivity() - newstudent.getScoreActivity(), 2)
            );
            pq.add(new StudentDistance(s, distance_calcule));
        }
        // Récupérer k plus proches
        Map<String, Integer> votes = new HashMap<>();
        for (int i = 0; i < k && !pq.isEmpty(); i++) {
            StudentDistance sd = pq.poll();   // récupération de l'objet complet
            Student s = sd.student;

            // DEBUG : afficher la distance et la catégorie choisie
            System.out.println("K voisin #" + (i+1) +
                    " → catégorie = " + s.getCategory() +
                    ", distance = " + sd.distance);

            // DEBUG : afficher l'état des votes AVANT mise à jour
            System.out.println("Votes avant: " + votes);

            // voter pour la catégorie
            votes.put(s.getCategory(), votes.getOrDefault(s.getCategory(), 0) + 1);

            // DEBUG : afficher l'état des votes APRÈS mise à jour
            System.out.println("Votes après: " + votes);
        }
        // Définir la priorité des catégories (plus tôt = plus prioritaire)
        List<String> priority = Arrays.asList("honor", "average", "fail");

        // Trouver la catégorie avec le plus de votes
        int maxVotes = Collections.max(votes.values());

        // Construire une liste des catégories avec ce nombre max de votes
        List<String> topCategories = new ArrayList<>();
        for (String category : votes.keySet()) {
            if (votes.get(category) == maxVotes) {
                topCategories.add(category);
            }
        }

        // Si un seul top, on le retourne
        if (topCategories.size() == 1) {
            return topCategories.get(0);
        }

        // Sinon, tie-break avec priorité
        for (String p : priority) {
            if (topCategories.contains(p)) {
                return p; // retourne la catégorie prioritaire
            }
        }

        // sécurité : retourner le premier si aucun match (rare)
        return topCategories.get(0);


    }
    private static class StudentDistance{
        Student student;
        Double distance;
        StudentDistance(Student student, Double distance){
            this.student = student;
            this.distance= distance;
        }
    }
}

