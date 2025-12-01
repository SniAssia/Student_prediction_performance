package com.studentperformance.controllers;

import com.studentperformance.studentsdb.DatabaseConnection;
import com.studentperformance.studentsdb.Student_database;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class AddStudentController {

    @FXML private TextField nameField;
    @FXML private TextField sexField;
    @FXML private TextField ageField;

    @FXML private ComboBox<String> classLevelBox;

    @FXML
    private void initialize() {
        classLevelBox.getItems().addAll("CI1", "CI2", "CI3", "CPI1", "CPI2");
    }

    @FXML
    private void onSaveClicked() {

        String name = nameField.getText().trim();
        String sex = sexField.getText().trim().toUpperCase();
        String ageText = ageField.getText().trim();
        String classLevel = classLevelBox.getValue();

        // --- Validation ---
        if (name.isEmpty() || sex.isEmpty() || ageText.isEmpty() || classLevel == null) {
            showError("All fields must be filled.");
            return;
        }

        if (!sex.equals("M") && !sex.equals("F")) {
            showError("Sex must be M or F.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 1 || age > 120) {
                showError("Age must be between 1 and 120.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Age must be a valid number.");
            return;
        }

        // === INSERT INTO DATABASE (using the OLD 6-arg method) ===
        try {
            Connection conn = DatabaseConnection.getConnection();

            Student_database.create_new_student(
                    conn,
                    0,          // ID = 0 (database auto-increment will override)
                    name,
                    age,
                    sex,
                    classLevel
            );

            showSuccess("Student added successfully!");

            if (dashboardController != null) {
                dashboardController.refreshDashboard();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Database error: " + ex.getMessage());
        }

        closeWindow();
    }


    private MainDashboardController dashboardController;

    public void setDashboardController(MainDashboardController controller) {
        this.dashboardController = controller;
    }


    @FXML
    private void onCancelClicked() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
