package fx.controller;

import bl.IBLFacade;
import dto.Word;
import fx.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Add Word Dialog
 */
public class AddWordController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private Button closeBtn;
    @FXML private TextField arabicField;
    @FXML private TextField urduField;
    @FXML private TextField persianField;

    private IBLFacade facade;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Apply gradient to header
        applyHeaderGradient();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366F1")),
            new Stop(1, Color.web("#8B5CF6"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(0, 0, 0, 0, false), Insets.EMPTY
        )));
    }

    @FXML
    private void handleAddWord() {
        String arabic = arabicField.getText().trim();
        String urdu = urduField.getText().trim();
        String persian = persianField.getText().trim();

        // Validation
        if (arabic.isEmpty() || urdu.isEmpty() || persian.isEmpty()) {
            showWarning("Validation Error", "Please fill in all fields.");
            return;
        }

        // Create and save word
        Word word = new Word(arabic, urdu, persian);
        boolean success = facade.addWord(word);

        if (success) {
            showInfo("Success", "Word added successfully!");
            handleClear();
        } else {
            showError("Error", "Failed to add the word. It may already exist.");
        }
    }

    @FXML
    private void handleClear() {
        arabicField.clear();
        urduField.clear();
        persianField.clear();
        arabicField.requestFocus();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    // ==================== ALERTS ====================

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
