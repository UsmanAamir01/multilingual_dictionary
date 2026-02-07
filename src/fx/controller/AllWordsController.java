package fx.controller;

import bl.IBLFacade;
import dto.Word;
import fx.Main;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for All Words View
 * Displays dictionary entries in a TableView with search, update, and delete functionality
 */
public class AllWordsController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private TextField searchField;
    @FXML private TableView<WordEntry> wordsTable;
    @FXML private TableColumn<WordEntry, String> arabicCol;
    @FXML private TableColumn<WordEntry, String> urduCol;
    @FXML private TableColumn<WordEntry, String> persianCol;
    @FXML private TableColumn<WordEntry, String> favoriteCol;
    @FXML private Label posLabel, stemLabel, lemmaLabel;

    private IBLFacade facade;
    private ObservableList<WordEntry> wordData;
    private FilteredList<WordEntry> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Apply header gradient
        applyHeaderGradient();
        
        // Setup table columns
        setupTableColumns();
        
        // Load data
        loadTableData();
        
        // Setup search filter
        setupSearchFilter();
        
        // Setup selection listener
        setupSelectionListener();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366F1")),
            new Stop(1, Color.web("#8B5CF6"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(16), Insets.EMPTY
        )));
    }

    private void setupTableColumns() {
        arabicCol.setCellValueFactory(data -> data.getValue().arabicWordProperty());
        urduCol.setCellValueFactory(data -> data.getValue().urduMeaningProperty());
        persianCol.setCellValueFactory(data -> data.getValue().persianMeaningProperty());
        
        // Favorite column with star icon
        favoriteCol.setCellValueFactory(data -> data.getValue().favoriteIconProperty());
        favoriteCol.setStyle("-fx-alignment: CENTER;");
        
        // Make favorite column clickable
        favoriteCol.setCellFactory(col -> new TableCell<WordEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-size: 18px; -fx-cursor: hand;");
                    setOnMouseClicked(e -> {
                        WordEntry entry = getTableView().getItems().get(getIndex());
                        toggleFavorite(entry);
                    });
                }
            }
        });
    }

    private void loadTableData() {
        wordData = FXCollections.observableArrayList();
        
        String[][] rawData = facade.getWordsWithMeanings();
        if (rawData != null) {
            for (String[] row : rawData) {
                String arabic = row.length > 0 ? row[0] : "";
                String urdu = row.length > 1 ? row[1] : "";
                String persian = row.length > 2 ? row[2] : "";
                boolean isFav = facade.isWordFavorite(arabic);
                
                wordData.add(new WordEntry(arabic, urdu, persian, isFav));
            }
        }
        
        filteredData = new FilteredList<>(wordData, p -> true);
        wordsTable.setItems(filteredData);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(entry -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String filter = newVal.toLowerCase();
                return entry.getArabicWord().toLowerCase().contains(filter)
                    || entry.getUrduMeaning().toLowerCase().contains(filter)
                    || entry.getPersianMeaning().toLowerCase().contains(filter);
            });
        });
    }

    private void setupSelectionListener() {
        wordsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                updateWordInfo(newSel.getArabicWord());
            }
        });
    }

    private void updateWordInfo(String word) {
        try {
            String result = facade.processWord(word);
            if (result != null && !result.isEmpty()) {
                String[] parts = result.split(",");
                stemLabel.setText("Stem: " + (parts.length > 0 ? parts[0].trim() : "--"));
                lemmaLabel.setText("Lemma: " + (parts.length > 1 ? parts[1].trim() : "--"));
                posLabel.setText("Part of Speech: " + (parts.length > 2 ? parts[2].trim() : "--"));
            }
        } catch (Exception e) {
            // Silently handle
        }
    }

    private void toggleFavorite(WordEntry entry) {
        boolean newStatus = !entry.isFavorite();
        facade.markWordAsFavorite(entry.getArabicWord(), newStatus);
        entry.setFavorite(newStatus);
        wordsTable.refresh();
    }

    // ==================== ACTIONS ====================

    @FXML
    private void handleBack() {
        // Reload main view
        try {
            StackPane contentPane = (StackPane) headerPane.getParent().getParent();
            contentPane.getChildren().clear();
            
            // Reload welcome content
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fx/fxml/MainView.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            // This replaces the scene
            Main.getPrimaryStage().getScene().setRoot(root);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        WordEntry selected = wordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to update.");
            return;
        }

        // Show input dialogs for new meanings
        TextInputDialog urduDialog = new TextInputDialog(selected.getUrduMeaning());
        urduDialog.setTitle("Update Word");
        urduDialog.setHeaderText("Update Urdu Meaning");
        urduDialog.setContentText("Enter new Urdu meaning:");
        
        Optional<String> urduResult = urduDialog.showAndWait();
        if (!urduResult.isPresent()) return;

        TextInputDialog persianDialog = new TextInputDialog(selected.getPersianMeaning());
        persianDialog.setTitle("Update Word");
        persianDialog.setHeaderText("Update Persian Meaning");
        persianDialog.setContentText("Enter new Persian meaning:");
        
        Optional<String> persianResult = persianDialog.showAndWait();
        if (!persianResult.isPresent()) return;

        // Update word
        Word updatedWord = new Word(selected.getArabicWord(), urduResult.get(), persianResult.get());
        if (facade.updateWord(updatedWord)) {
            selected.setUrduMeaning(urduResult.get());
            selected.setPersianMeaning(persianResult.get());
            wordsTable.refresh();
            showInfo("Success", "Word updated successfully!");
        } else {
            showError("Error", "Failed to update word.");
        }
    }

    @FXML
    private void handleRemove() {
        WordEntry selected = wordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to remove.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Remove");
        confirm.setHeaderText("Remove \"" + selected.getArabicWord() + "\"?");
        confirm.setContentText("This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (facade.removeWord(selected.getArabicWord())) {
                    wordData.remove(selected);
                    showInfo("Success", "Word removed successfully!");
                } else {
                    showError("Error", "Failed to remove word.");
                }
            }
        });
    }

    @FXML
    private void handleToggleFavorite() {
        WordEntry selected = wordsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word.");
            return;
        }
        toggleFavorite(selected);
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

    // ==================== INNER CLASS ====================

    /**
     * Table entry model with JavaFX properties
     */
    public static class WordEntry {
        private final SimpleStringProperty arabicWord;
        private final SimpleStringProperty urduMeaning;
        private final SimpleStringProperty persianMeaning;
        private final SimpleStringProperty favoriteIcon;
        private boolean favorite;

        public WordEntry(String arabic, String urdu, String persian, boolean favorite) {
            this.arabicWord = new SimpleStringProperty(arabic);
            this.urduMeaning = new SimpleStringProperty(urdu);
            this.persianMeaning = new SimpleStringProperty(persian);
            this.favorite = favorite;
            this.favoriteIcon = new SimpleStringProperty(favorite ? "⭐" : "☆");
        }

        public String getArabicWord() { return arabicWord.get(); }
        public SimpleStringProperty arabicWordProperty() { return arabicWord; }

        public String getUrduMeaning() { return urduMeaning.get(); }
        public void setUrduMeaning(String value) { urduMeaning.set(value); }
        public SimpleStringProperty urduMeaningProperty() { return urduMeaning; }

        public String getPersianMeaning() { return persianMeaning.get(); }
        public void setPersianMeaning(String value) { persianMeaning.set(value); }
        public SimpleStringProperty persianMeaningProperty() { return persianMeaning; }

        public boolean isFavorite() { return favorite; }
        public void setFavorite(boolean value) { 
            this.favorite = value; 
            this.favoriteIcon.set(value ? "⭐" : "☆");
        }
        public SimpleStringProperty favoriteIconProperty() { return favoriteIcon; }
    }
}
