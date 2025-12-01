package com.studentperformance.studentsdb;

public class Attendance {
    private int id;
    private int studentId;
    private int moduleId;
    private int absences;
    private int lateArrivals;
    private float sanctions;
    private float finalAttendance; // correspond à final_attendance

    // Constructeur par défaut
    public Attendance() {}

    // Constructeur complet
    public Attendance(int id, int studentId, int moduleId, int absences, int lateArrivals, float sanctions) {
        this.id = id;
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.absences = absences;
        this.lateArrivals = lateArrivals;
        this.sanctions = sanctions;
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

    public int getAbsences() {
        return absences;
    }

    public void setAbsences(int absences) {
        this.absences = absences;
    }

    public int getLateArrivals() {
        return lateArrivals;
    }

    public void setLateArrivals(int lateArrivals) {
        this.lateArrivals = lateArrivals;
    }

    public float getSanctions() {
        return sanctions;
    }

    public void setSanctions(float sanctions) {
        this.sanctions = sanctions;
    }

    public float getFinalAttendance() {
        return finalAttendance;
    }

    public void setFinalAttendance(float finalAttendance) {
        this.finalAttendance = finalAttendance;
    }

    /**
     * Calcul de l'attendance finale selon la formule SQL :
     * 100 * (1 - (absences / totalSessions)) - (lateArrivals * 2) - sanctions
     */
    public void calculateFinalAttendance(int totalSessions) {
        if (totalSessions <= 0) {
            throw new IllegalArgumentException("Le nombre total de séances doit être supérieur à 0.");
        }
        this.finalAttendance = (100f * (1f - ((float) absences / totalSessions))) - (lateArrivals * 2) - sanctions;
        // Limiter entre 0 et 100
        if (finalAttendance < 0) finalAttendance = 0;
        if (finalAttendance > 100) finalAttendance = 100;

    }

    @Override
    public String toString() {
        return "Attendance{" + "id=" + id + ", studentId=" + studentId + ", moduleId=" + moduleId + ", absences=" + absences + ", lateArrivals=" + lateArrivals + ", sanctions=" + sanctions +
                ", finalAttendance=" + finalAttendance + '}';
    }
}
