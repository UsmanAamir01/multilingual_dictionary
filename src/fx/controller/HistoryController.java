package fx.controller;

import bl.IBLFacade;
import dto.Word;
import fx.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for History View
 */
public class HistoryController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private Label countLabel;
    @FXML private ListView<Word> historyList;
    @FXML private VBox emptyState;

    private IBLFacade facade;
    private ObservableList<Word> historyData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Apply gradient header
        applyHeaderGradient();
        
        // Setup list cell factory
        setupListCellFactory();
        
        // Load history
        loadHistory();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#8B5CF6")),
            new Stop(1, Color.web("#7C3AED"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(0), Insets.EMPTY
        )));
    }

    private void setupListCellFactory() {
        historyList.setCellFactory(lv -> new ListCell<Word>() {
            @Override
            protected void updateItem(Word item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox cell = new HBox(12);
                    cell.setAlignment(Pos.CENTER_LEFT);
                    cell.setPadding(new Insets(10, 16, 10, 16));
                    
                    Label icon = new Label("🔍");
                    icon.setStyle("-fx-font-size: 16px;");
                    
                    VBox textBox = new VBox(2);
                    HBox.setHgrow(textBox, Priority.ALWAYS);
                    
                    String wordText = item.getArabicWord() != null ? item.getArabicWord() : 
                                     (item.getUrduMeaning() != null ? item.getUrduMeaning() : 
                                      item.getPersianMeaning());
                    
                    Label wordLabel = new Label(wordText);
                    wordLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 600;");
                    
                    String language = item.getArabicWord() != null ? "Arabic" :
                                     (item.getUrduMeaning() != null ? "Urdu" : "Persian");
                    Label langLabel = new Label(language);
                    langLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748B;");
                    
                    textBox.getChildren().addAll(wordLabel, langLabel);
                    
                    // Timestamp (simulated)
                    Label timeLabel = new Label("Recent");
                    timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #94A3B8;");
                    
                    cell.getChildren().addAll(icon, textBox, timeLabel);
                    
                    setGraphic(cell);
                    setText(null);
                }
            }
        });
    }

    private void loadHistory() {
        historyData = FXCollections.observableArrayList();
        
        List<Word> history = facade.getSearchHistory();
        if (history != null && !history.isEmpty()) {
            historyData.addAll(history);
            historyList.setItems(historyData);
            countLabel.setText(historyData.size() + " recent searches");
            
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            historyList.setVisible(true);
        } else {
            historyList.setVisible(false);
            historyList.setManaged(false);
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            countLabel.setText("0 recent searches");
        }
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
    private void handleClearAll() {
        if (historyData.isEmpty()) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear History");
        confirm.setHeaderText("Clear all search history?");
        confirm.setContentText("This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                facade.clearSearchHistory();
                historyData.clear();
                countLabel.setText("0 recent searches");
                
                historyList.setVisible(false);
                historyList.setManaged(false);
                emptyState.setVisible(true);
                emptyState.setManaged(true);
            }
        });
    }

    @FXML
    private void handleSearchAgain() {
        Word selected = historyList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to search again.");
            return;
        }

        String wordText = selected.getArabicWord() != null ? selected.getArabicWord() : 
                         (selected.getUrduMeaning() != null ? selected.getUrduMeaning() : 
                          selected.getPersianMeaning());
        
        String language = selected.getArabicWord() != null ? "Arabic" :
                         (selected.getUrduMeaning() != null ? "Urdu" : "Persian");
        
        String meaning = facade.getMeanings(wordText, language);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search Result");
        alert.setHeaderText(wordText);
        alert.setContentText(meaning != null ? meaning : "No meaning found.");
        alert.showAndWait();
    }

    @FXML
    private void handleAddToFavorites() {
        Word selected = historyList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to add to favourites.");
            return;
        }

        String wordText = selected.getArabicWord();
        if (wordText != null) {
            facade.markWordAsFavorite(wordText, true);
            showInfo("Success", "\"" + wordText + "\" added to favourites!");
        } else {
            showWarning("Cannot Add", "Only Arabic words can be added to favourites.");
        }
    }

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
}
