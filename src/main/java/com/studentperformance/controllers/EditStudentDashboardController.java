package com.studentperformance.controllers;

import com.studentperformance.studentsdb.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EditStudentDashboardController {

    @FXML private Label titleLabel;
    private Student selectedStudent;

    public void setStudent(Student student) {
        this.selectedStudent = student;
        titleLabel.setText("Edit Student: " + student.getName());
    }

    private void openWindow(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);

            // pass student to the opened sub-form
            Object controller = loader.getController();

            if (controller instanceof EditGradesController)
                ((EditGradesController) controller).setStudent(selectedStudent);

            if (controller instanceof EditAttendanceController)
                ((EditAttendanceController) controller).setStudent(selectedStudent);

            if (controller instanceof EditActivitiesController)
                ((EditActivitiesController) controller).setStudent(selectedStudent);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void onEditGrades() {
        openWindow("/ui/EditGrades.fxml", "Edit Grades");
    }

    @FXML private void onEditAttendance() {
        openWindow("/ui/EditAttendance.fxml", "Edit Attendance");
    }

    @FXML private void onEditActivities() {
        openWindow("/ui/EditActivities.fxml", "Edit Activities");
    }
}
