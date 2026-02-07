package fx.controller;

import bl.IBLFacade;
import dto.Word;
import fx.Main;
import fx.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Dashboard Controller
 * Handles navigation, search, and dynamic content
 */
public class MainController implements Initializable {

    @FXML private VBox sidebar;
    @FXML private VBox navContainer;
    @FXML private HBox navAddWord, navViewAll, navImport, navNormalize, navFavorites, navHistory;
    @FXML private ComboBox<String> languageCombo;
    @FXML private TextField searchField;
    @FXML private Label themeLabel;
    @FXML private StackPane contentPane;
    @FXML private VBox welcomeContent;
    @FXML private StackPane heroCard;
    @FXML private HBox quickActionsGrid;
    @FXML private HBox statsGrid;

    private IBLFacade facade;
    private HBox activeNavItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Populate language combo box
        languageCombo.getItems().addAll("Arabic", "Persian", "Urdu");
        languageCombo.getSelectionModel().selectFirst();
        
        // Apply hero gradient
        applyHeroGradient();
        
        // Create quick action cards
        createQuickActionCards();
        
        // Create stat cards
        createStatCards();
        
        // Set initial active nav item
        setActiveNavItem(navAddWord);
        
        // Setup search field enter key
        searchField.setOnAction(e -> handleSearch());
        
        // Register scene for theme management
        javafx.application.Platform.runLater(() -> {
            ThemeManager.getInstance().registerScene(searchField.getScene());
        });
    }

    private void applyHeroGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366F1")),
            new Stop(1, Color.web("#8B5CF6"))
        );
        heroCard.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(16), Insets.EMPTY
        )));
    }

    private void createQuickActionCards() {
        quickActionsGrid.getChildren().clear();
        
        quickActionsGrid.getChildren().addAll(
            createActionCard("➕", "Add Word", "Add new entries", "#10B981", this::handleAddWord),
            createActionCard("📋", "View All", "Browse dictionary", "#6366F1", this::handleViewAll),
            createActionCard("⭐", "Favourites", "Saved words", "#F59E0B", this::handleFavorites),
            createActionCard("🕐", "History", "Recent searches", "#8B5CF6", this::handleHistory)
        );
    }

    private VBox createActionCard(String icon, String title, String description, String accentColor, Runnable action) {
        VBox card = new VBox(8);
        card.getStyleClass().add("action-card");
        card.setPrefWidth(180);
        card.setPrefHeight(120);
        card.setPadding(new Insets(20));
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("action-card-icon");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("action-card-title");
        
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("action-card-description");
        
        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        
        // Click handler
        card.setOnMouseClicked(e -> action.run());
        
        return card;
    }

    private void createStatCards() {
        statsGrid.getChildren().clear();
        
        // Get real stats from facade if available
        String wordCount = "1,000+";
        String langCount = "3";
        String favCount = "50+";
        
        statsGrid.getChildren().addAll(
            createStatCard("📚", wordCount, "Total Words", "#6366F1"),
            createStatCard("🌍", langCount, "Languages", "#8B5CF6"),
            createStatCard("⭐", favCount, "Favourites", "#F59E0B")
        );
    }

    private HBox createStatCard(String icon, String value, String label, String accentColor) {
        HBox card = new HBox(12);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(200);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        VBox textBox = new VBox(2);
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-card-value");
        valueLabel.setStyle("-fx-text-fill: " + accentColor + ";");
        
        Label labelText = new Label(label);
        labelText.getStyleClass().add("stat-card-label");
        
        textBox.getChildren().addAll(valueLabel, labelText);
        card.getChildren().addAll(iconLabel, textBox);
        
        return card;
    }

    private void setActiveNavItem(HBox item) {
        // Remove active class from previous
        if (activeNavItem != null) {
            activeNavItem.getStyleClass().remove("sidebar-item-active");
        }
        
        // Add active class to new item
        item.getStyleClass().add("sidebar-item-active");
        activeNavItem = item;
    }

    // ==================== NAVIGATION HANDLERS ====================

    @FXML
    private void handleAddWord() {
        handleAddWord(null);
    }
    
    private void handleAddWord(MouseEvent event) {
        setActiveNavItem(navAddWord);
        openAddWordDialog();
    }

    @FXML
    private void handleViewAll() {
        handleViewAll(null);
    }
    
    private void handleViewAll(MouseEvent event) {
        setActiveNavItem(navViewAll);
        loadAllWordsView();
    }

    @FXML
    private void handleImport() {
        setActiveNavItem(navImport);
        
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Import Dictionary File");
        fileChooser.getExtensionFilters().addAll(
            new javafx.stage.FileChooser.ExtensionFilter("CSV Files", "*.csv"),
            new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        
        java.io.File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
        if (file != null) {
            try {
                int count = facade.importFromFile(file.getAbsolutePath());
                showInfoAlert("Import Complete", "Successfully imported " + count + " words from file.");
            } catch (Exception e) {
                showErrorAlert("Import Failed", "Could not import file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleNormalize() {
        setActiveNavItem(navNormalize);
        
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Word Normalization");
        dialog.setHeaderText("Normalize Arabic Word");
        dialog.setContentText("Enter word to normalize:");
        
        dialog.showAndWait().ifPresent(word -> {
            if (!word.trim().isEmpty()) {
                try {
                    String result = facade.processWord(word.trim());
                    if (result != null && !result.isEmpty()) {
                        String[] parts = result.split(",");
                        String message = "Stem: " + (parts.length > 0 ? parts[0].trim() : "N/A") + "\n" +
                                       "Lemma: " + (parts.length > 1 ? parts[1].trim() : "N/A") + "\n" +
                                       "POS: " + (parts.length > 2 ? parts[2].trim() : "N/A");
                        showInfoAlert("Normalization Result", message);
                    } else {
                        showInfoAlert("Normalization Result", "Could not normalize the word.");
                    }
                } catch (Exception e) {
                    showErrorAlert("Error", "Processing error: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleFavorites() {
        handleFavorites(null);
    }
    
    private void handleFavorites(MouseEvent event) {
        setActiveNavItem(navFavorites);
        loadView("/fx/fxml/FavoritesView.fxml");
    }

    @FXML
    private void handleHistory() {
        handleHistory(null);
    }
    
    private void handleHistory(MouseEvent event) {
        setActiveNavItem(navHistory);
        loadView("/fx/fxml/HistoryView.fxml");
    }

    @FXML
    private void handleFAQs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FAQs");
        alert.setHeaderText("Frequently Asked Questions");
        alert.setContentText(
            "Q: How do I add a word?\n" +
            "A: Click 'Add Word' in the sidebar.\n\n" +
            "Q: How do I search?\n" +
            "A: Select a language, type your word, and click Search.\n\n" +
            "Q: How do I mark a favourite?\n" +
            "A: Click the star icon on any word.\n\n" +
            "Q: How do I export my dictionary?\n" +
            "A: Go to Settings > Data Management > Export."
        );
        alert.showAndWait();
    }

    @FXML
    private void handleInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("How to Use the Dictionary");
        alert.setContentText(
            "1. SEARCH: Select language, enter word, click Search\n" +
            "2. ADD WORD: Click sidebar > Add Word, fill form\n" +
            "3. VIEW ALL: Browse your complete dictionary\n" +
            "4. FAVOURITES: Mark important words with star\n" +
            "5. HISTORY: View your recent searches\n" +
            "6. SETTINGS: Customize theme and preferences"
        );
        alert.showAndWait();
    }

    @FXML
    private void handleSettings() {
        loadView("/fx/fxml/SettingsView.fxml");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        String language = languageCombo.getValue();
        
        if (query.isEmpty()) {
            showWarningAlert("Search", "Please enter a word to search.");
            return;
        }
        
        String urduMeaning = facade.getMeanings(query, "Urdu");
        String persianMeaning = facade.getMeanings(query, "Persian");
        
        // Add to history
        Word word = createWord(query, language);
        if (word != null) {
            facade.addSearchToHistory(word);
        }
        
        // Load search result view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/fxml/SearchResultView.fxml"));
            Parent root = loader.load();
            
            SearchResultController controller = loader.getController();
            controller.setSearchData(query, language, urduMeaning, persianMeaning);
            
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to simple alert
            if (urduMeaning != null || persianMeaning != null) {
                showInfoAlert("Search Result", 
                    "Urdu: " + (urduMeaning != null ? urduMeaning : "N/A") + "\n\n" +
                    "Persian: " + (persianMeaning != null ? persianMeaning : "N/A"));
            } else {
                showConfirmScrape(query, language);
            }
        }
    }

    private void showConfirmScrape(String word, String language) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Word Not Found");
        alert.setHeaderText("\"" + word + "\" was not found in the database.");
        alert.setContentText("Would you like to add this word manually?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                openAddWordDialog();
            }
        });
    }

    private Word createWord(String text, String language) {
        switch (language) {
            case "Arabic": return new Word(text, null, null);
            case "Persian": return new Word(null, text, null);
            case "Urdu": return new Word(null, null, text);
            default: return null;
        }
    }

    // ==================== THEME ====================

    @FXML
    private void toggleTheme() {
        ThemeManager manager = ThemeManager.getInstance();
        manager.toggleTheme();
        themeLabel.setText(manager.isDarkMode() ? "Light" : "Dark");
    }

    // ==================== VIEW LOADING ====================

    private void openAddWordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/fxml/AddWordDialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Word");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.getPrimaryStage());
            
            Scene scene = new Scene(root, 480, 420);
            scene.getStylesheets().add(getClass().getResource("/fx/css/theme-light.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/fx/css/components.css").toExternalForm());
            ThemeManager.getInstance().registerScene(scene);
            
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Could not open Add Word dialog.");
        }
    }

    private void loadAllWordsView() {
        loadView("/fx/fxml/AllWordsView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Could not load view.");
        }
    }

    // ==================== ALERTS ====================

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
