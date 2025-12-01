package com.studentperformance.controllers;

import com.studentperformance.studentsdb.Student;
import com.studentperformance.studentsdb.Student_database;
import com.studentperformance.studentsdb.KNN;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class MainDashboardController {

    @FXML private VBox container;
    @FXML private Button addStudentButton;

    @FXML
    public void initialize() {
        refreshDashboard();
    }

    // ---------------- REFRESH FULL DASHBOARD ----------------
    public void refreshDashboard() {
        try {
            List<Student> students = Student_database.getall_students();
            Map<String, List<Student>> grouped = groupByClass(students);
            displayClassCards(grouped);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- GROUP STUDENTS BY CLASS ----------------
    private Map<String, List<Student>> groupByClass(List<Student> students) {
        Map<String, List<Student>> map = new LinkedHashMap<>();
        for (Student s : students) {
            map.computeIfAbsent(s.getClassLevel(), k -> new ArrayList<>()).add(s);
        }
        return map;
    }

    // ---------------- CATEGORY COUNT FOR PIE CHART -------------
    private Map<String, Integer> countByCategory(List<Student> students) {
        Map<String, Integer> count = new LinkedHashMap<>();
        for (Student s : students) {
            String cat = s.getCategory();
            count.put(cat, count.getOrDefault(cat, 0) + 1);
        }
        return count;
    }

    // ---------------- BUILD CLASS CARDS ------------------------
    private void displayClassCards(Map<String, List<Student>> grouped) {

        container.getChildren().removeIf(node -> !(node instanceof HBox));
        // keeps ONLY the Add Student button

        for (String classe : grouped.keySet()) {

            VBox card = new VBox(15);
            card.setStyle("""
                -fx-background-color: #ffffff; 
                -fx-background-radius: 10;
                -fx-padding: 20;
                -fx-border-radius: 10; 
                -fx-border-color: #dddddd; 
                -fx-border-width: 1;
            """);

            Label classLabel = new Label(classe);
            classLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

            // ------------ PIE CHART -----------
            PieChart pie = new PieChart();
            pie.setTitle("Category Distribution");

            Map<String, Integer> data = countByCategory(grouped.get(classe));
            for (String category : data.keySet()) {
                pie.getData().add(new PieChart.Data(category, data.get(category)));
            }

            pie.setPrefHeight(250);
            pie.setPrefWidth(350);

            // ------------ TABLE ------------
            TableView<Student> table = new TableView<>();
            table.setPrefHeight(220);

            TableColumn<Student, String> colName = new TableColumn<>("Name");
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colName.setPrefWidth(130);

            TableColumn<Student, Double> colModules = new TableColumn<>("Modules");
            colModules.setCellValueFactory(new PropertyValueFactory<>("scoreModules"));

            TableColumn<Student, Double> colAttendance = new TableColumn<>("Attendance");
            colAttendance.setCellValueFactory(new PropertyValueFactory<>("scoreAttendance"));

            TableColumn<Student, Double> colActivity = new TableColumn<>("Activity");
            colActivity.setCellValueFactory(new PropertyValueFactory<>("scoreActivity"));

            TableColumn<Student, String> colCategory = new TableColumn<>("Category");
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colCategory.setPrefWidth(120);

            // ------------ EDIT BUTTON ------------
            TableColumn<Student, Void> colEdit = new TableColumn<>("Edit");
            colEdit.setCellFactory(param -> new TableCell<>() {
                private final Button editBtn = new Button("Edit");

                {
                    editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    editBtn.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        openEditDashboard(student);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : editBtn);
                }
            });

            // ------------ PREDICT BUTTON ------------
            TableColumn<Student, Void> colPredict = new TableColumn<>("Predict");
            colPredict.setCellFactory(param -> new TableCell<>() {

                private final Button predictBtn = new Button("Predict");

                {
                    predictBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");
                    predictBtn.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());

                        try {
                            ArrayList<Student> allStudents = Student_database.getall_students();
                            KNN knn = new KNN(3, allStudents);

                            String predictedCategory = knn.predict(student);
                            Student_database.updateStudentCategory(student.getId(), predictedCategory);

                            refreshDashboard();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        return;
                    }

                    Student student = getTableView().getItems().get(getIndex());

                    if (student.getCategory() == null || student.getCategory().isBlank()) {
                        setGraphic(predictBtn);   // show predict button
                    } else {
                        setGraphic(null);         // hide predict button
                    }
                }
            });

            table.getColumns().addAll(
                    colName, colModules, colAttendance, colActivity,
                    colCategory, colEdit, colPredict
            );

            table.getItems().addAll(grouped.get(classe));

            card.getChildren().addAll(classLabel, pie, table);
            container.getChildren().add(card);
        }
    }

    // ---------------- OPEN WINDOWS -------------------

    @FXML
    private void onAddStudentClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddStudentView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add Student");

            // 🔥 Get AddStudentController
            AddStudentController controller = loader.getController();
            controller.setDashboardController(this);  // give reference to dashboard

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openWindow(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void openEditDashboard(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/EditStudentDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            EditStudentDashboardController controller = loader.getController();
            controller.setStudent(student);

            // 🔥 When edit window CLOSES → refresh dashboard
            stage.setOnHidden(event -> refreshDashboard());

            stage.setTitle("Edit Student");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
