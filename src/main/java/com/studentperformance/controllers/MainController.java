package com.studentperformance.controllers;

import com.studentperformance.studentsdb.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MainController {

    @FXML private Button addStudentButton;
    @FXML private TableView<Student> studentsTable;

    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, Double> colModules;
    @FXML private TableColumn<Student, Double> colAttendance;
    @FXML private TableColumn<Student, Double> colActivity;
    @FXML private TableColumn<Student, String> colCategory;

    @FXML private TableColumn<Student, Void> colActions;

    @FXML
    private void initialize() {

        System.out.println("Dashboard Loaded Successfully");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colModules.setCellValueFactory(new PropertyValueFactory<>("scoreModules"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("scoreAttendance"));
        colActivity.setCellValueFactory(new PropertyValueFactory<>("scoreActivity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        addEditButtonsColumn();
        loadDummyData();
    }

    @FXML
    private void onAddStudentClicked() {
        openWindow("/ui/AddStudentView.fxml", "Add Student");
    }

    private void openWindow(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** NEW — Load Edit Dashboard */
    private void openEditDashboard(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/EditStudentDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Edit Student");

            EditStudentDashboardController controller = loader.getController();
            controller.setStudent(student);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEditButtonsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {

            private final Button editButton = new Button("Edit");

            {
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    openEditDashboard(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
    }

    private void loadDummyData() {
        studentsTable.getItems().addAll(
                new Student(1, "Alice", 20, "F", "CI1", 85, 70, 65, "At Risk"),
                new Student(2, "Bob", 22, "M", "CI2", 75, 80, 90, "Satisfactory"),
                new Student(3, "Charlie", 19, "M", "CI3", 95, 60, 88, "Excellent")
        );
    }
}
