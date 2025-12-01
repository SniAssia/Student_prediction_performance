package com.studentperformance.studentsdb;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Student {

    private int id;
    private String name;
    private int age;
    private String sex;

    private String classLevel;  // ⬅ VERY IMPORTANT (CI1, CI2, CI3)

    private double scoreModules;
    private double scoreAttendance;
    private double scoreActivity;

    private String category; // NOT DOUBLE → categories are strings (“honor”, …)
    private double final_score;
    // Empty constructor
    public Student() {}

    // Full constructor
    public Student(int id, String name, int age, String sex,
                   String classLevel,
                   double scoreModules, double scoreAttendance,
                   double scoreActivity, String category,double final_score) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.classLevel = classLevel;
        this.scoreModules = scoreModules;
        this.scoreAttendance = scoreAttendance;
        this.scoreActivity = scoreActivity;
        this.category = category;
        this.final_score= final_score;

    }
    public Student(int id, String name, int age, String sex,
                   String classLevel,
                   double scoreModules, double scoreAttendance,
                   double scoreActivity, String category) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.classLevel = classLevel;
        this.scoreModules = scoreModules;
        this.scoreAttendance = scoreAttendance;
        this.scoreActivity = scoreActivity;
        this.category = category;


    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSex() { return sex; }

    public String getClassLevel() { return classLevel; }

    public double getScoreModules() { return scoreModules; }
    public double getScoreAttendance() { return scoreAttendance; }
    public double getScoreActivity() { return scoreActivity; }

    public String getCategory() { return category; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setSex(String sex) { this.sex = sex; }

    public void setClassLevel(String classLevel) { this.classLevel = classLevel; }

    public void setScoreModules(double scoreModules) { this.scoreModules = scoreModules; }
    public void setScoreAttendance(double scoreAttendance) { this.scoreAttendance = scoreAttendance; }
    public void setScoreActivity(double scoreActivity) { this.scoreActivity = scoreActivity; }

    public void setCategory(String category) { this.category = category; }
    public double computeFinalgrade(Connection conn) throws SQLException {

        // 1️⃣ Load all modules for this student's level
        ArrayList<Modules> modules = new ArrayList<>();
        String prepquery = "SELECT * FROM modules WHERE student_level = ?";
        try (PreparedStatement ps = conn.prepareStatement(prepquery)) {
            ps.setString(1, this.classLevel);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modules.add(new Modules(
                        rs.getInt("id"),
                        rs.getString("module_name"),
                        rs.getInt("total_sessions"),
                        rs.getString("option_type"),
                        rs.getString("student_level")
                ));
            }
        }

        // 2️⃣ Load grades for this student
        ArrayList<Grade> grades = new ArrayList<>();
        String query2 = "SELECT * FROM grades WHERE student_id = ?";
        try (PreparedStatement pw = conn.prepareStatement(query2)) {
            pw.setInt(1, this.id);
            ResultSet rs1 = pw.executeQuery();
            while (rs1.next()) {
                grades.add(new Grade(
                        rs1.getInt("id"),
                        rs1.getInt("student_id"),
                        rs1.getInt("module_id"),
                        rs1.getFloat("exam1"),
                        rs1.getFloat("exam2"),
                        rs1.getFloat("exam3"),
                        rs1.getFloat("quiz1"),
                        rs1.getFloat("quiz2"),
                        rs1.getFloat("group_project")   // ✅ FIX: removed the space
                ));
            }
        }

        // 3️⃣ Compute final grade across modules
        double totalGrade = 0;
        int count = 0;

        for (Modules module : modules) {

            // find the grade record for this module
            Grade g = grades.stream()
                    .filter(gr -> gr.getModuleId() == module.getId())
                    .findFirst()
                    .orElse(null);

            if (g != null) {
                g.calculateFinalGrade();
                System.out.println(
                        "Module: " + module.getModuleName() +
                                " | Grade: " + g.getFinalGrade() + "%"
                );

                totalGrade += g.getFinalGrade();
                count++;
            }
        }

        if (count > 0) {
            return totalGrade / count;
        } else {
            System.out.println("No grade records found for student " + this.id);
            return 0;
        }
    }

    //final grade for all modules
    public double computeFinalAttendance(Connection conn) throws SQLException {
        // Load all modules for this student's level
        ArrayList<Modules> modules = new ArrayList<>();
        String moduleQuery = "SELECT * FROM modules WHERE student_level = ?";
        try (PreparedStatement psModules = conn.prepareStatement(moduleQuery)) {
            psModules.setString(1, this.classLevel);
            ResultSet rsModules = psModules.executeQuery();
            while (rsModules.next()) {
                modules.add(new Modules(
                        rsModules.getInt("id"),
                        rsModules.getString("module_name"),
                        rsModules.getInt("total_sessions"),
                        rsModules.getString("option_type"),
                        rsModules.getString("student_level")));
            }
        }

        // Load all attendance records for this student
        ArrayList<Attendance> attendance = new ArrayList<>();
        String attendanceQuery = "SELECT * FROM attendance WHERE student_id = ?";
        try (PreparedStatement psAttendance = conn.prepareStatement(attendanceQuery)) {
            psAttendance.setInt(1, this.id);
            ResultSet rsAttendance = psAttendance.executeQuery();
            while (rsAttendance.next()) {
                attendance.add(new Attendance(
                        rsAttendance.getInt("id"),
                        this.id,
                        rsAttendance.getInt("module_id"),
                        rsAttendance.getInt("absences"),
                        rsAttendance.getInt("late_arrivals"),
                        rsAttendance.getInt("sanctions")));
            }
        }
        // Compute final attendance for all modules
        double totalPercentage = 0;
        int count = 0;
        for (Modules module : modules) {
            // Find attendance record for this module
            Attendance a = attendance.stream()
                    .filter(att -> att.getModuleId() == module.getId())
                    .findFirst()
                    .orElse(null);
            if (a != null) {
                a.calculateFinalAttendance(module.getTotalSessions());
                System.out.println("Module: " + module.getModuleName() + " | Attendance: " + a.getFinalAttendance() + "%");
                totalPercentage += a.getFinalAttendance();
                count++;
            }
        }

        // Compute final average attendance across all modules
        if (count > 0) {
            double finalAttendance = totalPercentage / count;
            System.out.println("Final average attendance for student " + this.id + ": " + finalAttendance + "%");
            return finalAttendance;
        } else {
            System.out.println("No attendance records found for student " + this.id);
            return 0;
        }

    }
    public double computeFinalActiviites(Connection conn) throws SQLException {
        // Load all modules for this student's level
        ArrayList<Modules> modules = new ArrayList<>();
        String moduleQuery = "SELECT * FROM modules WHERE student_level = ?";
        try (PreparedStatement psModules = conn.prepareStatement(moduleQuery)) {
            psModules.setString(1, this.classLevel);
            ResultSet rsModules = psModules.executeQuery();
            while (rsModules.next()) {
                modules.add(new Modules(
                        rsModules.getInt("id"),
                        rsModules.getString("module_name"),
                        rsModules.getInt("total_sessions"),
                        rsModules.getString("option_type"),
                        rsModules.getString("student_level")));
            }
        }

        // Load all attendance records for this student
        ArrayList<Activity> activities = new ArrayList<>();
        String attendanceQuery = "SELECT * FROM activities WHERE student_id = ?";
        try (PreparedStatement psAttendance = conn.prepareStatement(attendanceQuery)) {
            psAttendance.setInt(1, this.id);
            ResultSet rsAttendance = psAttendance.executeQuery();
            while (rsAttendance.next()) {
                activities.add(new Activity(
                        rsAttendance.getInt("id"),
                        this.id,
                        rsAttendance.getInt("module_id"),
                        rsAttendance.getFloat("devoir_libre"),
                        rsAttendance.getFloat("participation"),
                        rsAttendance.getFloat("Labs"),
                        rsAttendance.getFloat("assiduite"),
                        rsAttendance.getFloat("bonus")));
            }
        }
        // Compute final attendance for all modules
        double totalPercentage = 0;
        int count = 0;
        for (Modules module : modules) {
            // Find attendance record for this module
            Activity a = activities.stream()
                    .filter(att -> att.getModuleId() == module.getId())
                    .findFirst()
                    .orElse(null);
            if (a != null) {
                a.calculateActivityFinal();
                System.out.println("Module: " + module.getModuleName() + " | Attendance: " + a.getActivityFinal() + "%");
                totalPercentage += a.getActivityFinal();
                count++;
            }
        }

        // Compute final average attendance across all modules
        if (count > 0) {
            double finalAttendance = totalPercentage / count;
            System.out.println("Final average attendance for student " + this.id + ": " + finalAttendance + "%");
            return finalAttendance;
        } else {
            System.out.println("No attendance records found for student " + this.id);
            return 0;
        }

    }

}
