package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import bl.BLFacade;
import bl.IBLFacade;
import bl.WordBO;
import bl.UserBO;

/**
 * JavaFX Application Entry Point
 * Premium Multilingual Dictionary
 */
public class Main extends Application {
    
    private static IBLFacade facade;
    private static Stage primaryStage;
    
    @Override
    public void init() {
        // Load custom fonts (optional - app works without them)
        try {
            var regular = getClass().getResourceAsStream("/fx/fonts/Inter-Regular.ttf");
            var medium = getClass().getResourceAsStream("/fx/fonts/Inter-Medium.ttf");
            var semibold = getClass().getResourceAsStream("/fx/fonts/Inter-SemiBold.ttf");
            var bold = getClass().getResourceAsStream("/fx/fonts/Inter-Bold.ttf");
            
            if (regular != null) Font.loadFont(regular, 14);
            if (medium != null) Font.loadFont(medium, 14);
            if (semibold != null) Font.loadFont(semibold, 14);
            if (bold != null) Font.loadFont(bold, 14);
        } catch (Exception e) {
            System.out.println("Note: Inter fonts not found, using system fonts.");
        }
        
        // Initialize business layer
        facade = new BLFacade(new WordBO(), new UserBO());
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        // Load main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/fxml/MainView.fxml"));
        Parent root = loader.load();
        
        // Create scene with theme
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/fx/css/theme-light.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/fx/css/components.css").toExternalForm());
        
        // Configure stage
        stage.setTitle("Multilingual Dictionary");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
        // Cleanup resources
        System.out.println("Application closing...");
    }
    
    public static IBLFacade getFacade() {
        return facade;
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
