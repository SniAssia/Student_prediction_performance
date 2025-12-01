package com.studentperformance.studentsdb;

import java.sql.*;
import java.util.ArrayList;

public class Student_database {

    // ---------------- GET ALL STUDENTS ----------------
    public static ArrayList<Student> getall_students() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM students");

        while (rs.next()) {
            students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("sex"),
                    rs.getString("class"),
                    rs.getDouble("final_grade"),
                    rs.getDouble("final_attendance"),
                    rs.getDouble("final_activity"),
                    rs.getString("category"),
                    rs.getDouble("final_score")
            ));
        }

        rs.close();
        stm.close();
        conn.close();

        return students;
    }

    public static void updateAttendance(int studentId, int moduleId,
                                        int absences, int late, int sanctions)
            throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        String sql =
                "INSERT INTO attendance (student_id, module_id, absences, late_arrivals, sanctions) " +
                        "VALUES (?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "absences = VALUES(absences), " +
                        "late_arrivals = VALUES(late_arrivals), " +
                        "sanctions = VALUES(sanctions)";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, studentId);
        pst.setInt(2, moduleId);
        pst.setInt(3, absences);
        pst.setInt(4, late);
        pst.setInt(5, sanctions);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }


    public static void updateFinalAttendance(int studentId, double finalAttendance)
            throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        String sql = "UPDATE students SET final_attendance=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setDouble(1, finalAttendance);
        pst.setInt(2, studentId);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    public static void updateGrade(
            int studentId,
            int moduleId,
            float exam1, float exam2, float exam3,
            float quiz1, float quiz2,
            float groupProject
    ) throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        String sql =
                "INSERT INTO grades (student_id, module_id, exam1, exam2, exam3, quiz1, quiz2, group_project) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "exam1 = VALUES(exam1), " +
                        "exam2 = VALUES(exam2), " +
                        "exam3 = VALUES(exam3), " +
                        "quiz1 = VALUES(quiz1), " +
                        "quiz2 = VALUES(quiz2), " +
                        "group_project = VALUES(group_project)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, studentId);
        ps.setInt(2, moduleId);
        ps.setFloat(3, exam1);
        ps.setFloat(4, exam2);
        ps.setFloat(5, exam3);
        ps.setFloat(6, quiz1);
        ps.setFloat(7, quiz2);
        ps.setFloat(8, groupProject);

        ps.executeUpdate();
        ps.close();
        conn.close();
    }




    public static void updateFinalGrade(int studentId, double finalGrade)
            throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        String sql = "UPDATE students SET final_grade=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setDouble(1, finalGrade);
        pst.setInt(2, studentId);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }





    public static void updateFinalActivity(int studentId, double finalScore) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        String sql = "UPDATE students SET final_activity=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setDouble(1, finalScore);
        pst.setInt(2, studentId);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }



    public static void updateActivity(int studentId, int moduleId,
                                      float devoir, float participation,
                                      float labs, float assiduite, float bonus)
            throws SQLException {

        Connection conn = DatabaseConnection.getConnection();

        String sql =
                "INSERT INTO activities (student_id, module_id, devoir_libre, participation, Labs, assiduite, bonus) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "devoir_libre = VALUES(devoir_libre), " +
                        "participation = VALUES(participation), " +
                        "Labs = VALUES(Labs), " +
                        "assiduite = VALUES(assiduite), " +
                        "bonus = VALUES(bonus)";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, studentId);
        pst.setInt(2, moduleId);
        pst.setFloat(3, devoir);
        pst.setFloat(4, participation);
        pst.setFloat(5, labs);
        pst.setFloat(6, assiduite);
        pst.setFloat(7, bonus);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }



    // ---------------- UPDATE CATEGORY AFTER PREDICTION ----------------
    public static void updateStudentCategory(int id, String category) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        String sql = "UPDATE students SET category=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, category);
        pst.setInt(2, id);
        pst.executeUpdate();

        pst.close();
        conn.close();
    }

    // ---------------- CREATE STUDENT ----------------
    public static void create_new_student(Connection conn, int id, String name,
                                          int age, String sexe, String classe_level)
            throws SQLException {

        String query = "INSERT INTO students (id, name, age, sex, class) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setInt(1, id);
        pst.setString(2, name);
        pst.setInt(3, age);
        pst.setString(4, sexe);
        pst.setString(5, classe_level);

        pst.executeUpdate();
        pst.close();
        conn.close();
    }

    // ---------------- GET STUDENTS BY CLASS ----------------
    public static ArrayList<Student> get_students_by_level(Connection conn, String level)
            throws SQLException {

        ArrayList<Student> students = new ArrayList<>();
        Statement stm = conn.createStatement();

        ResultSet rs = stm.executeQuery("SELECT * FROM students WHERE class='" + level + "'");

        while (rs.next()) {
            students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("sex"),
                    rs.getString("class"),
                    rs.getDouble("final_grade"),
                    rs.getDouble("final_attendance"),
                    rs.getDouble("final_activity"),
                    rs.getString("category"),
                    rs.getDouble("final_score")
            ));
        }

        rs.close();
        stm.close();
        conn.close();

        return students;
    }
}
