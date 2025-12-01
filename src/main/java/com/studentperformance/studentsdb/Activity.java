package com.studentperformance.studentsdb;


public class Activity {
    private int id;
    private int studentId;
    private int moduleId;
    private float devoirLibre;    // devoirs/labs
    private float participation;
    private float labs;
    private float assiduite;      // présence pour activités
    private float bonus;          // points bonus
    private float activityFinal;  // correspond à activity_final

    // Constructeur par défaut
    public Activity() {}

    // Constructeur complet
    public Activity(int id, int studentId, int moduleId, float devoirLibre, float participation,
                    float labs, float assiduite, float bonus) {
        this.id = id;
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.devoirLibre = devoirLibre;
        this.participation = participation;
        this.labs = labs;
        this.assiduite = assiduite;
        this.bonus = bonus;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public float getDevoirLibre() {
        return devoirLibre;
    }

    public void setDevoirLibre(float devoirLibre) {
        this.devoirLibre = devoirLibre;
    }

    public float getParticipation() {
        return participation;
    }

    public void setParticipation(float participation) {
        this.participation = participation;
    }

    public float getLabs() {
        return labs;
    }

    public void setLabs(float labs) {
        this.labs = labs;
    }

    public float getAssiduite() {
        return assiduite;
    }

    public void setAssiduite(float assiduite) {
        this.assiduite = assiduite;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public float getActivityFinal() {
        return activityFinal;
    }

    public void setActivityFinal(float activityFinal) {
        this.activityFinal = activityFinal;
    }

    /**
     * Calcul de la note finale pour les activités selon la formule SQL :
     * activity_final = 40% devoir_libre + 20% participation + 30% labs + 10% bonus
     */
    public void calculateActivityFinal() {
        this.activityFinal = (devoirLibre * 0.40f) + (participation * 0.20f) + (labs * 0.30f) + (bonus * 0.10f);
        // Limiter entre 0 et 100
        if (activityFinal < 0) activityFinal = 0;
        if (activityFinal > 100) activityFinal = 100;
    }

    @Override
    public String toString() {
        return "Activity{" + "id=" + id + ", studentId=" + studentId + ", moduleId=" + moduleId +  ", devoirLibre=" + devoirLibre + ", participation=" + participation + ", labs=" + labs + ", assiduite=" + assiduite + ", bonus=" + bonus +
                ", activityFinal=" + activityFinal + '}';
    }
}
