package com.studentperformance.studentsdb;

public class Grade {
    private int id;
    private int studentId;
    private int moduleId;
    private float exam1;
    private float exam2;
    private float exam3;
    private float quiz1;
    private float quiz2;
    private float groupProject;
    private float finalGrade; // correspond à final_grade

    // Constructeur par défaut
    public Grade() {}

    // Constructeur complet
    public Grade(int id, int studentId, int moduleId, float exam1, float exam2, float exam3,
                 float quiz1, float quiz2, float groupProject) {
        this.id = id;
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.exam1=exam1;
        this.exam2=exam2;
        this.exam3 = exam3;
        this.quiz1 = quiz1;
        this.quiz2 = quiz2;
        this.groupProject = groupProject;
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

    public float getExam1() {
        return exam1;
    }

    public void setExam1(float exam1) {
        this.exam1 = exam1;
    }

    public float getExam2() {
        return exam2;
    }

    public void setExam2(float exam2) {
        this.exam2 = exam2;
    }

    public float getExam3() {
        return exam3;
    }

    public void setExam3(float exam3) {
        this.exam3 = exam3;
    }

    public float getQuiz1() {
        return quiz1;
    }

    public void setQuiz1(float quiz1) {
        this.quiz1 = quiz1;
    }

    public float getQuiz2() {
        return quiz2;
    }

    public void setQuiz2(float quiz2) {
        this.quiz2 = quiz2;
    }

    public float getGroupProject() {
        return groupProject;
    }

    public void setGroupProject(float groupProject) {
        this.groupProject = groupProject;
    }

    public float getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(float finalGrade) {
        this.finalGrade = finalGrade;
    }

    /**
     * Calcul de la note finale selon la formule SQL :
     * final_grade = 30% exam1 + 30% exam2 + 20% exam3 + 10% quiz1 + 5% quiz2 + 5% groupProject
     */
    public void calculateFinalGrade() {
        this.finalGrade = (exam1 * 0.30f) + (exam2 * 0.30f) + (exam3 * 0.20f) + (quiz1 * 0.10f) + (quiz2 * 0.05f) + (groupProject * 0.05f);
        // Limiter entre 0 et 100
        if (finalGrade < 0) finalGrade = 0;
        if (finalGrade > 100) finalGrade = 100;
    }

    @Override
    public String toString() {
        return "Grade{" + "id=" + id + ", studentId=" + studentId + ", moduleId=" + moduleId + ", exam1=" + exam1 + ", exam2=" + exam2 + ", exam3=" + exam3 + ", quiz1=" + quiz1 + ", quiz2=" + quiz2 + ", groupProject=" + groupProject +
                ", finalGrade=" + finalGrade + '}';
    }
}
