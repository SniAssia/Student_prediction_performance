package com.studentperformance.studentsdb;

public class Modules {
    private int id;
    private String moduleName;
    private int totalSessions;
    private String optionType;
    private String studentLevel;

    // Constructors
    public Modules() {
    }

    public Modules(int id, String moduleName, int totalSessions, String optionType, String studentLevel) {
        this.id = id;
        this.moduleName = moduleName;
        this.totalSessions = totalSessions;
        this.optionType = optionType;
        this.studentLevel = studentLevel;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getStudentLevel() {
        return studentLevel;
    }

    public void setStudentLevel(String studentLevel) {
        this.studentLevel = studentLevel;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", totalSessions=" + totalSessions +
                ", optionType='" + optionType + '\'' +
                ", studentLevel='" + studentLevel + '\'' +
                '}';
    }
}
