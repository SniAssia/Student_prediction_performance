package com.studentperformance.studentsdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        // Remplace "nom_de_ta_db" par le nom de ta base
        String url = "jdbc:mysql://localhost:3306/school";
        String user = "root";              // Ton utilisateur MySQL
        String password = "_____"; // Ton mot de passe MySQL
        return DriverManager.getConnection(url, user, password);
    }
}
