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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Favorites View
 */
public class FavoritesController implements Initializable {

    @FXML private StackPane headerPane;
    @FXML private Label countLabel;
    @FXML private ListView<String> favoritesList;
    @FXML private VBox emptyState;

    private IBLFacade facade;
    private ObservableList<String> favoritesData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = Main.getFacade();
        
        // Apply gradient header
        applyHeaderGradient();
        
        // Setup list cell factory
        setupListCellFactory();
        
        // Load favorites
        loadFavorites();
    }

    private void applyHeaderGradient() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#F59E0B")),
            new Stop(1, Color.web("#D97706"))
        );
        headerPane.setBackground(new Background(new BackgroundFill(
            gradient, new CornerRadii(0), Insets.EMPTY
        )));
    }

    private void setupListCellFactory() {
        favoritesList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox cell = new HBox(12);
                    cell.setAlignment(Pos.CENTER_LEFT);
                    cell.setPadding(new Insets(8, 16, 8, 16));
                    
                    Label star = new Label("⭐");
                    star.setStyle("-fx-font-size: 18px;");
                    
                    VBox textBox = new VBox(2);
                    Label wordLabel = new Label(item);
                    wordLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 600;");
                    
                    // Get meaning
                    String meaning = facade.getMeanings(item, "Urdu");
                    Label meaningLabel = new Label(meaning != null ? meaning.substring(0, Math.min(50, meaning.length())) + "..." : "");
                    meaningLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");
                    
                    textBox.getChildren().addAll(wordLabel, meaningLabel);
                    cell.getChildren().addAll(star, textBox);
                    
                    setGraphic(cell);
                    setText(null);
                }
            }
        });
    }

    private void loadFavorites() {
        favoritesData = FXCollections.observableArrayList();
        
        List<Word> favorites = facade.getFavoriteWords();
        if (favorites != null && !favorites.isEmpty()) {
            for (Word word : favorites) {
                favoritesData.add(word.getArabicWord());
            }
            favoritesList.setItems(favoritesData);
            countLabel.setText(favoritesData.size() + " words saved");
            
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            favoritesList.setVisible(true);
        } else {
            favoritesList.setVisible(false);
            favoritesList.setManaged(false);
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            countLabel.setText("0 words saved");
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
    private void handleViewDetails() {
        String selected = favoritesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to view details.");
            return;
        }

        String meaning = facade.getMeanings(selected, "Urdu");
        String persianMeaning = facade.getMeanings(selected, "Persian");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Word Details");
        alert.setHeaderText(selected);
        alert.setContentText(
            "Urdu: " + (meaning != null ? meaning : "N/A") + "\n\n" +
            "Persian: " + (persianMeaning != null ? persianMeaning : "N/A")
        );
        alert.showAndWait();
    }

    @FXML
    private void handleRemove() {
        String selected = favoritesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a word to remove.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Favourite");
        confirm.setHeaderText("Remove \"" + selected + "\" from favourites?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                facade.markWordAsFavorite(selected, false);
                favoritesData.remove(selected);
                countLabel.setText(favoritesData.size() + " words saved");
                
                if (favoritesData.isEmpty()) {
                    favoritesList.setVisible(false);
                    favoritesList.setManaged(false);
                    emptyState.setVisible(true);
                    emptyState.setManaged(true);
                }
            }
        });
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
