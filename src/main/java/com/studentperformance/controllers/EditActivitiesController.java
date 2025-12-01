package com.studentperformance.controllers;

import com.studentperformance.studentsdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.HashMap;

public class EditActivitiesController {

    @FXML private Label titleLabel;
    @FXML private VBox modulesContainer;

    private Student student;

    private HashMap<String, TextField[]> activitiesInputs = new HashMap<>();
    private HashMap<String, Integer> moduleIds = new HashMap<>();


    // -----------------------------------------
    // SET STUDENT
    // -----------------------------------------
    public void setStudent(Student student) {
        this.student = student;
        titleLabel.setText("Edit Activities — " + student.getName());
        loadModules();
    }


    // -----------------------------------------
    // LOAD MODULES & CREATE INPUT FIELDS
    // -----------------------------------------
    private void loadModules() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            var ps = conn.prepareStatement(
                    "SELECT id, module_name FROM modules WHERE student_level=?"
            );
            ps.setString(1, student.getClassLevel());
            var rs = ps.executeQuery();

            while (rs.next()) {

                int moduleId = rs.getInt("id");
                String moduleName = rs.getString("module_name");

                moduleIds.put(moduleName, moduleId);

                Label moduleTitle = new Label(moduleName);
                moduleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                TextField devoir = new TextField(); devoir.setPromptText("Devoir Libre");
                TextField participation = new TextField(); participation.setPromptText("Participation");
                TextField labs = new TextField(); labs.setPromptText("Labs");
                TextField assiduite = new TextField(); assiduite.setPromptText("Assiduité");
                TextField bonus = new TextField(); bonus.setPromptText("Bonus");

                VBox box = new VBox(5, moduleTitle, devoir, participation, labs, assiduite, bonus);
                box.setStyle("-fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");

                modulesContainer.getChildren().add(box);

                activitiesInputs.put(moduleName, new TextField[]{
                        devoir, participation, labs, assiduite, bonus
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -----------------------------------------
    // SAVE ACTIVITIES
    // -----------------------------------------
    @FXML
    private void onSaveActivities() {

        try {

            // 1️⃣ Save activity values
            for (String module : activitiesInputs.keySet()) {

                TextField[] f = activitiesInputs.get(module);
                int moduleId = moduleIds.get(module);

                float devoir = parseFloatSafe(f[0].getText());
                float participation = parseFloatSafe(f[1].getText());
                float labs = parseFloatSafe(f[2].getText());
                float assiduite = parseFloatSafe(f[3].getText());
                float bonus = parseFloatSafe(f[4].getText());

                Student_database.updateActivity(
                        student.getId(), moduleId,
                        devoir, participation, labs, assiduite, bonus
                );
            }

            // 2️⃣ Recompute final activity
            Connection conn = DatabaseConnection.getConnection();
            double finalActivity = student.computeFinalActiviites(conn);
            conn.close();

            // 3️⃣ Save final activity to students table
            Student_database.updateFinalActivity(student.getId(), finalActivity);

            // 4️⃣ Reset category
            Student_database.updateStudentCategory(student.getId(), null);


            // 5️⃣ SUCCESS MESSAGE
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Activities updated successfully!");
            alert.showAndWait();

            // 6️⃣ Close the window → dashboard auto-refreshes
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -----------------------------------------
    // SAFE PARSING
    // -----------------------------------------
    private float parseFloatSafe(String txt) {
        try {
            return Float.parseFloat(txt);
        } catch (Exception e) {
            return 0;
        }
    }
}
