package fx.controller;

import bl.IBLFacade;
import dto.Word;
import fx.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Search Result View
 */
public class SearchResultController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private Label titleLabel, subtitleLabel;
    @FXML private VBox resultCard, notFoundState;
    @FXML private Label wordLabel, languageLabel;
    @FXML private Label urduMeaningLabel, persianMeaningLabel;
    @FXML private Label posLabel, stemLabel, lemmaLabel;
    @FXML private Label notFoundWord;
    @FXML private Button favoriteBtn;

    private IBLFacade facade;
    private String currentWord;
    private String currentLanguage;
    private boolean isFavorite;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        applyHeaderGradient();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366F1")),
            new Stop(1, Color.web("#8B5CF6"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(0), Insets.EMPTY
        )));
    }

    /**
     * Initialize with search data
     */
    public void setSearchData(String word, String language, String urduMeaning, String persianMeaning) {
        this.currentWord = word;
        this.currentLanguage = language;
        
        if (urduMeaning == null && persianMeaning == null) {
            // Word not found
            showNotFoundState(word);
        } else {
            showResultState(word, language, urduMeaning, persianMeaning);
        }
    }

    private void showResultState(String word, String language, String urduMeaning, String persianMeaning) {
        resultCard.setVisible(true);
        resultCard.setManaged(true);
        notFoundState.setVisible(false);
        notFoundState.setManaged(false);
        
        subtitleLabel.setText("Results for \"" + word + "\"");
        wordLabel.setText(word);
        languageLabel.setText(language);
        
        urduMeaningLabel.setText(urduMeaning != null ? urduMeaning : "Not available");
        persianMeaningLabel.setText(persianMeaning != null ? persianMeaning : "Not available");
        
        // Get linguistic analysis
        try {
            String analysis = facade.processWord(word);
            if (analysis != null && !analysis.isEmpty()) {
                String[] parts = analysis.split(",");
                stemLabel.setText(parts.length > 0 ? parts[0].trim() : "--");
                lemmaLabel.setText(parts.length > 1 ? parts[1].trim() : "--");
                posLabel.setText(parts.length > 2 ? parts[2].trim() : "--");
            }
        } catch (Exception e) {
            // Keep defaults
        }
        
        // Check favorite status
        isFavorite = facade.isWordFavorite(word);
        updateFavoriteButton();
    }

    private void showNotFoundState(String word) {
        resultCard.setVisible(false);
        resultCard.setManaged(false);
        notFoundState.setVisible(true);
        notFoundState.setManaged(true);
        
        subtitleLabel.setText("No results found");
        notFoundWord.setText("\"" + word + "\" was not found in the database");
    }

    private void updateFavoriteButton() {
        favoriteBtn.setText(isFavorite ? "⭐" : "☆");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/fxml/MainView.fxml"));
            Parent root = loader.load();
            Main.getPrimaryStage().getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToggleFavorite() {
        if (currentWord != null) {
            isFavorite = !isFavorite;
            facade.markWordAsFavorite(currentWord, isFavorite);
            updateFavoriteButton();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Favourites");
            alert.setHeaderText(null);
            alert.setContentText(isFavorite ? 
                "\"" + currentWord + "\" added to favourites!" :
                "\"" + currentWord + "\" removed from favourites.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEdit() {
        // Open add word dialog pre-filled
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/fxml/AddWordDialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Word");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.getPrimaryStage());
            
            Scene scene = new Scene(root, 480, 420);
            scene.getStylesheets().add(getClass().getResource("/fx/css/theme-light.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/fx/css/components.css").toExternalForm());
            
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleWebSearch() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Web Search");
        alert.setHeaderText("Web Scraper");
        alert.setContentText("Searching online dictionaries for \"" + currentWord + "\"...\n\nThis feature uses web scraping to find meanings from online sources.");
        alert.showAndWait();
        
        // TODO: Integrate with actual scraper
    }

    @FXML
    private void handleAddManually() {
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
            
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
