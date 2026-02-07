package fx.controller;

import bl.IBLFacade;
import fx.Main;
import fx.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Settings View
 */
public class SettingsController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private ToggleButton darkModeToggle;
    @FXML private ComboBox<String> fontSizeCombo;
    @FXML private ComboBox<String> defaultLanguageCombo;

    private IBLFacade facade;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Apply gradient header
        applyHeaderGradient();
        
        // Setup controls
        setupControls();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#64748B")),
            new Stop(1, Color.web("#475569"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(0), Insets.EMPTY
        )));
    }

    private void setupControls() {
        // Dark mode toggle
        boolean isDark = ThemeManager.getInstance().isDarkMode();
        darkModeToggle.setSelected(isDark);
        darkModeToggle.setText(isDark ? "On" : "Off");
        
        // Font size options
        fontSizeCombo.getItems().addAll("Small", "Medium", "Large");
        fontSizeCombo.getSelectionModel().select("Medium");
        
        // Language options
        defaultLanguageCombo.getItems().addAll("Arabic", "Persian", "Urdu");
        defaultLanguageCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleBack() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fx/fxml/MainView.fxml")
            );
            javafx.scene.Parent root = loader.load();
            Main.getPrimaryStage().getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDarkModeToggle() {
        ThemeManager manager = ThemeManager.getInstance();
        manager.toggleTheme();
        
        boolean isDark = manager.isDarkMode();
        darkModeToggle.setText(isDark ? "On" : "Off");
    }

    @FXML
    private void handleClearHistory() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear History");
        confirm.setHeaderText("Clear all search history?");
        confirm.setContentText("This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                facade.clearSearchHistory();
                showInfo("Success", "Search history cleared successfully.");
            }
        });
    }

    @FXML
    private void handleClearFavorites() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear Favourites");
        confirm.setHeaderText("Remove all favourite words?");
        confirm.setContentText("This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                facade.clearFavorites();
                showInfo("Success", "All favourites cleared successfully.");
            }
        });
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Dictionary");
        fileChooser.setInitialFileName("dictionary_export.csv");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
        if (file != null) {
            try {
                String[][] data = facade.getWordsWithMeanings();
                FileWriter writer = new FileWriter(file);
                
                writer.write("Arabic,Urdu,Persian\n");
                if (data != null) {
                    for (String[] row : data) {
                        writer.write(String.format("\"%s\",\"%s\",\"%s\"\n",
                            row.length > 0 ? row[0] : "",
                            row.length > 1 ? row[1] : "",
                            row.length > 2 ? row[2] : ""
                        ));
                    }
                }
                
                writer.close();
                showInfo("Success", "Dictionary exported to:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showError("Export Failed", "Could not export dictionary: " + e.getMessage());
            }
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
