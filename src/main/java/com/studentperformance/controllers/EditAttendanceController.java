package com.studentperformance.controllers;

import com.studentperformance.studentsdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class EditAttendanceController {

    @FXML private Label titleLabel;
    @FXML private VBox modulesContainer;

    private Student student;

    // moduleName → {present, late, absent}
    private HashMap<String, CheckBox[]> attendanceInputs = new HashMap<>();

    // moduleName → moduleId
    private HashMap<String, Integer> moduleIds = new HashMap<>();


    public void setStudent(Student student) {
        this.student = student;
        titleLabel.setText("Edit Attendance — " + student.getName());
        loadModules();
    }


    // ===========================================
    //          LOAD MODULES + CHECKBOXES
    // ===========================================
    private void loadModules() {

        modulesContainer.getChildren().clear();
        attendanceInputs.clear();
        moduleIds.clear();

        try {
            Connection conn = DatabaseConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, module_name FROM modules WHERE student_level=?"
            );
            ps.setString(1, student.getClassLevel());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int moduleId = rs.getInt("id");
                String moduleName = rs.getString("module_name");

                moduleIds.put(moduleName, moduleId);

                Label title = new Label(moduleName);
                title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                CheckBox present = new CheckBox("Present");
                CheckBox late = new CheckBox("Late");
                CheckBox absent = new CheckBox("Absent");

                // Ensure only one selected
                present.setOnAction(e -> { late.setSelected(false); absent.setSelected(false); });
                late.setOnAction(e -> { present.setSelected(false); absent.setSelected(false); });
                absent.setOnAction(e -> { present.setSelected(false); late.setSelected(false); });

                VBox box = new VBox(5, title, present, late, absent);
                box.setStyle("-fx-padding: 10; -fx-border-color: #CCCCCC; -fx-border-radius: 5;");

                modulesContainer.getChildren().add(box);

                attendanceInputs.put(moduleName, new CheckBox[]{present, late, absent});
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===========================================
    //                SAVE ATTENDANCE
    // ===========================================
    @FXML
    private void onSaveAttendance() {

        try {

            // === 1. Save attendance per module ===
            for (String module : attendanceInputs.keySet()) {

                CheckBox[] boxes = attendanceInputs.get(module);

                int absences = boxes[2].isSelected() ? 1 : 0;
                int late = boxes[1].isSelected() ? 1 : 0;
                int present = boxes[0].isSelected() ? 1 : 0;

                // (You can add sanctions manually later)
                int sanctions = 0;

                int moduleId = moduleIds.get(module);

                // FIX: Works even if row does NOT exist
                Student_database.updateAttendance(
                        student.getId(),
                        moduleId,
                        absences,
                        late,
                        sanctions
                );
            }

            // === 2. Recompute global attendance ===
            Connection conn = DatabaseConnection.getConnection();
            double finalAttendance = student.computeFinalAttendance(conn);
            conn.close();

            // === 3. Save final attendance for student ===
            Student_database.updateFinalAttendance(student.getId(), finalAttendance);

            // === 4. Reset category (important for prediction) ===
            Student_database.updateStudentCategory(student.getId(), null);

            // === 5. Success Popup ===
            showSuccess("Attendance updated successfully!");

            // === 6. Close window ===
            ((Stage) titleLabel.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===========================================
    //                 SUCCESS ALERT
    // ===========================================
    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

