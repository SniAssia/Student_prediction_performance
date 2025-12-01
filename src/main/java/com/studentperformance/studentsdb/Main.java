package com.studentperformance.studentsdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion réussie à la base de données !");

                // Créer un statement pour exécuter des requêtes
                Statement stmt = conn.createStatement();

                ArrayList<Student> student =  Student_database.get_students_by_level(conn,"CPI1");
                KNN tester = new KNN(10,student);
                Student new_student = new Student(2000,"assia",19,"F","CPI1",17,91,18,null,70.0);
                // Récupérer quelques étudiants
                String cat= tester.predict(new_student);
                System.out.println(cat);



                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
