package com.studentperformance.controllers;

import com.studentperformance.studentsdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class EditGradesController {

    @FXML private Label titleLabel;
    @FXML private VBox modulesContainer;

    private Student selectedStudent;

    // moduleName → ALL grade fields
    private HashMap<String, TextField[]> gradeFields = new HashMap<>();

    // moduleName → moduleId
    private HashMap<String, Integer> moduleIds = new HashMap<>();


    // --------------------------------------------------
    // SET STUDENT
    // --------------------------------------------------
    public void setStudent(Student student) {
        this.selectedStudent = student;
        titleLabel.setText("Edit Grades — " + student.getName());
        loadModules();
    }


    // --------------------------------------------------
    // LOAD MODULES AND CREATE INPUT FIELDS
    // --------------------------------------------------
    private void loadModules() {

        gradeFields.clear();
        modulesContainer.getChildren().clear();

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, module_name FROM modules WHERE student_level=?"
            );
            ps.setString(1, selectedStudent.getClassLevel());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int moduleId = rs.getInt("id");
                String moduleName = rs.getString("module_name");

                moduleIds.put(moduleName, moduleId);

                Label lbl = new Label(moduleName);
                lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                // --- 6 input fields ---
                TextField exam1 = new TextField(); exam1.setPromptText("Exam 1");
                TextField exam2 = new TextField(); exam2.setPromptText("Exam 2");
                TextField exam3 = new TextField(); exam3.setPromptText("Exam 3");

                TextField quiz1 = new TextField(); quiz1.setPromptText("Quiz 1");
                TextField quiz2 = new TextField(); quiz2.setPromptText("Quiz 2");

                TextField project = new TextField(); project.setPromptText("Group Project");

                VBox box = new VBox(5, lbl,
                        exam1, exam2, exam3,
                        quiz1, quiz2, project
                );

                box.setStyle("-fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");

                modulesContainer.getChildren().add(box);

                gradeFields.put(moduleName, new TextField[]{
                        exam1, exam2, exam3,
                        quiz1, quiz2, project
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --------------------------------------------------
    // SAVE ALL GRADES
    // --------------------------------------------------
    @FXML
    private void onSaveGrades() {

        try {

            // 1️⃣ Save 6 grades for each module
            for (String module : gradeFields.keySet()) {

                TextField[] f = gradeFields.get(module);

                float exam1 = parseFloat(f[0].getText());
                float exam2 = parseFloat(f[1].getText());
                float exam3 = parseFloat(f[2].getText());
                float quiz1 = parseFloat(f[3].getText());
                float quiz2 = parseFloat(f[4].getText());
                float project = parseFloat(f[5].getText());

                int moduleId = moduleIds.get(module);

                Student_database.updateGrade(
                        selectedStudent.getId(),
                        moduleId,
                        exam1, exam2, exam3,
                        quiz1, quiz2,
                        project
                );
            }

            // 2️⃣ Compute final grade
            Connection conn = DatabaseConnection.getConnection();
            double finalGrade = selectedStudent.computeFinalgrade(conn);
            conn.close();

            // 3️⃣ Save final grade to DB
            Student_database.updateFinalGrade(selectedStudent.getId(), finalGrade);

            // 4️⃣ Clear category (force new prediction)
            Student_database.updateStudentCategory(selectedStudent.getId(), null);

            // 5️⃣ SUCCESS MESSAGE
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Grades updated successfully!\nFinal Grade: " + finalGrade);
            alert.showAndWait();

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    // --------------------------------------------------
    // HELPERS
    // --------------------------------------------------
    private float parseFloat(String txt) {
        try { return Float.parseFloat(txt); }
        catch (Exception e) { return 0; }
    }

    private void closeWindow() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
